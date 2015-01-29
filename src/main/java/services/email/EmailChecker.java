/**
* 
* <p>Title: EmailChecker</p>
* <p>Description: Class for checking and classifying unread emails </p>
* <p>All Right Reserved</p> 
* @author Fan Wang
* @date Jan 28, 2015
*/
package services.email;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.search.FlagTerm;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.requesthander.RegistrationHandler;
import services.requesthander.RemoveRequestHandler;
import services.requesthander.WatchRequestHandler;
import domain.Request;
import domain.RequestType;
import domain.SpringContextUtils;

public class EmailChecker {
	/**
	 * email connection information is set in /src/main/java/bean.xml
	 */
	private String host;
	private String storeType;
	private String userName;
	private String password;
	private String port;
	private Properties props;
	private static Logger log = LoggerFactory.getLogger(EmailChecker.class);
	private static String[] acceptEmailAddress= new String[]{"@gmail.com","@hotmail.com"};

	public EmailChecker() {

	}

	public EmailChecker(String host, String storeType, String userName,
			String pwd, String port) {
		this.host = host;
		this.storeType = storeType;
		this.userName = userName;
		this.password = pwd;
		this.port = port;

	}

	/**
	* 
	* <p>Title: check</p>
	* <p>Description: Check all unread emails, discard emails which are not accepted(see array acceptEmailAddress) 
	*    Put emails into different request categories according to the subject then handing those requests 
	* </p>
	* @throws MessagingException
	* @throws IOException
	*/
	public void check() throws MessagingException, IOException {
		
        log.debug("Starting to check email");
		Session emailSession = Session.getDefaultInstance(props);

		Store emailStore = null;
		Folder emailFolder = null;
		try {
			emailStore = emailSession.getStore(storeType);

			emailStore.connect(host, userName, password);

			emailFolder = emailStore.getFolder("Inbox");
			emailFolder.open(Folder.READ_WRITE);

			Flags seen = new Flags(Flags.Flag.SEEN);
			FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
			Message[] message = emailFolder.search(unseenFlagTerm);
			
			log.debug("message.length   " + message.length);
            
			parseMessages(message);

			// set unread emails to read
			emailFolder.setFlags(message, new Flags(Flags.Flag.SEEN), true);

		} catch (NoSuchProviderException e) {
			log.error("NoSuchProviderException " + e.getMessage());
		} catch (MessagingException e) {
			log.error("MessagingException " + e.getMessage());

		} finally {
			if (emailFolder != null)
				emailFolder.close(false);
			if (emailStore != null)
				emailStore.close();
		}

	}


   /**
   * 
   * <p>Title: getUrls</p>
   * <p>Description: Get urls from email content part</p>
   * @param  m
   * @return a set of urls
   */
	private Set<String> getUrls(Part m) {
		Set<String> urlSet = new HashSet<>();
		try {
			if (m.isMimeType("multipart/*")) {
				Multipart multiPart = (Multipart) m.getContent();
				int count = multiPart.getCount();
				for (int i = 0; i < count; ++i) {
					BodyPart bodyPart = multiPart.getBodyPart(i);
					if (bodyPart.getContentType().startsWith("TEXT/PLAIN")) {
						urlSet.addAll(getUrlsTextType(bodyPart.getContent()
								.toString()));
					} else if (bodyPart.getContentType()
							.startsWith("TEXT/HTML")) {
						urlSet.addAll(getUrlsHtmlType(bodyPart.getContent()
								.toString()));
					} else {
						urlSet.addAll(getUrls(bodyPart));
					}
				}
			} else if (m.isMimeType("text/*")) {
				urlSet.addAll(getUrlsTextType(m.getContent().toString()));
			} else{

				// TODO add your own code to handle other type
				
			}
		} catch (MessagingException e) {
			log.error("MessagingException " + e.getMessage());
		}catch(IOException e){
			log.error("IOException "+e.getMessage());
		}
		return urlSet;
	}

	/**
	 * 
	* <p>Title: getUrlsHtmlType</p>
	* <p>Description: If email is sent as html, parse html to get urls </p>
	* @param content
	* @return a set of urls
	 */
	private Collection<? extends String> getUrlsHtmlType(String content) {
		Document doc = Jsoup.parse(content);
		Elements elements = doc.select("a[href]");
		Set<String> urlSet = new HashSet<>();
		for (Element e : elements) {
			String url = e.text().trim();

			urlSet.add(url);

		}
		return urlSet;
	}
	
	/**
	 * 
	* <p>Title: getUrlsTextType</p>
	* <p>Description: If email is sent as text, parse the content to get urls</p>
	* @param content
	* @return a set of urls
	 */
	private Collection<? extends String> getUrlsTextType(String content) {
		Set<String> urlSet = new HashSet<>();

		Scanner scanner = new Scanner(content);
		try {
			while (scanner.hasNext()) {
				String line = scanner.nextLine().trim();
				if (isValidURL(line))
					urlSet.add(line);
			}
		} finally {
			if (scanner != null)
				scanner.close();
		}
		return urlSet;
	}
    
	/**
	 * 
	* <p>Title: parseMessages</p>
	* <p>Description: Parse all unread messages and handle them to different request handler</p>
	* @param message
	* @throws MessagingException
	 */
	private void parseMessages(Message[] message) throws MessagingException {
		
		log.debug("parsing all unread messages");
		List<Request> registrationRequests = new ArrayList<Request>();
		List<Request> watchRequests = new ArrayList<Request>();
		List<Request> removeRequests = new ArrayList<Request>();

		for (Message m : message) {
			String subject = m.getSubject();

			RequestType rType = messageType(subject);
			Address[] from = m.getFrom();
			InternetAddress iaddress = (InternetAddress) from[0];
			String uEmail = iaddress.getAddress();
			if (rType == RequestType.UNRECOGNIZED_REQUEST)
				continue;

			else if(!acceptEmailAddress(uEmail))
				continue;
			
			else {
				Request req = new Request();
				req.setReqType(rType);
				req.setUserEmail(uEmail);
				switch (rType) {
				case REGISTRATION_REQUEST:
					;
					registrationRequests.add(req);
					break;
				case WATCH_REQUEST:
				case REMOVE_REQUEST:

					List<String> urlList = new ArrayList<String>();

					urlList.addAll(getUrls(m));
					req.setUrllist(urlList);
					if (rType == RequestType.WATCH_REQUEST)
						watchRequests.add(req);
					else
						removeRequests.add(req);
					break;

				default:
					break;

				}
			}

		}

		RegistrationHandler regHandler = (RegistrationHandler) SpringContextUtils
				.getBean("registhandler");
		regHandler.requestHander(registrationRequests);
		WatchRequestHandler watHandler = (WatchRequestHandler) SpringContextUtils
				.getBean("watchhandler");
		watHandler.requestHander(watchRequests);
		RemoveRequestHandler remHandler = (RemoveRequestHandler) SpringContextUtils
				.getBean("removehandler");
		remHandler.requestHander(removeRequests);

	}
    /**
     * 
    * <p>Title: acceptEmailAddress</p>
    * <p>Description: </p>
    * @param uEmail
    * @return true if email address is ended up with one of the string in static field acceptEmailAddress
     */
	private boolean acceptEmailAddress(String uEmail) {
		for(String s : acceptEmailAddress){
			if(uEmail.endsWith(s))
				return true;
		}
		return false;
	}

	/**
	 * 
	* <p>Title: isValidURL</p>
	* <p>Description: Check if the string is a valid URL</p>
	* @param line
	* @return true if it is a valid URL
	 */
	private boolean isValidURL(String line) {
		
		if (line == null || line.trim().length() == 0)
			return false;
		String regEx = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

		Pattern urlPattern = Pattern.compile(regEx);
		Matcher urlMatcher = urlPattern.matcher(line);
		return urlMatcher.matches();

	}

	/**
	 * 
	* <p>Title: messageType</p>
	* <p>Description: Check email subject to decide the request type </p>
	* @param subject
	* @return RequestType according to the subject
	 */
	private RequestType messageType(String subject) {
		Pattern signP = Pattern.compile("[/s]*signup[/s]*",
				Pattern.CASE_INSENSITIVE);
		Matcher sMatch = signP.matcher(subject);
		Pattern watchP = Pattern.compile("[/s]*watch[/s]*",
				Pattern.CASE_INSENSITIVE);
		Matcher wMatch = watchP.matcher(subject);
		Pattern removeP = Pattern.compile("[/s]*remove[/s]*",
				Pattern.CASE_INSENSITIVE);
		Matcher rMatch = removeP.matcher(subject);
		boolean sm = sMatch.matches();
		boolean wm = wMatch.matches();
		boolean rm = rMatch.matches();
		if (sm)
			return RequestType.REGISTRATION_REQUEST;
		else if (wm)
			return RequestType.WATCH_REQUEST;
		else if (rm)
			return RequestType.REMOVE_REQUEST;
		else
			return RequestType.UNRECOGNIZED_REQUEST;
	}

   
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
