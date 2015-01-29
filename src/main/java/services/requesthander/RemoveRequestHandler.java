package services.requesthander;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.data.DataService;
import services.email.EmailSender;
import domain.EmailConstant;
import domain.EmailTemplate;
import domain.RegisteredUser;
import domain.Request;
import domain.SpringContextUtils;
/**
 * 
* <p>Title: RemoveRequestHandler</p>
* <p>Description: Class for handling remove requests</p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public class RemoveRequestHandler implements RequestHandler {

	private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);
    
	public void requestHander(List<Request> requestList) {

		log.debug("Handling remove requests");
		DataService ds = (DataService) SpringContextUtils
				.getBean("dataservice");
		if (requestList != null && requestList.size() > 0) {
			for (Request r : requestList) {
				String emailAddress = r.getUserEmail();
				RegisteredUser ru = new RegisteredUser();
				ru.setEmailAddress(emailAddress);
				List<String> urlList = r.getUrllist();
				try{
				ds.removeUrlList(ru, urlList);
				}catch(RuntimeException e){
					log.error(e.getMessage());
					continue;
				}
				EmailSender mailSender = (EmailSender) SpringContextUtils
						.getBean("emailsender");
				EmailTemplate email = new EmailTemplate(emailAddress,
						EmailConstant.watchedListremoved, urlList);
				mailSender.sendMailByAsynchronousMode(email);

			}

		}

	}

}
