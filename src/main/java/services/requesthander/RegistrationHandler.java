package services.requesthander;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.data.DataService;
import services.email.EmailSender;
import domain.CachedInfoUtils;
import domain.EmailConstant;
import domain.EmailTemplate;
import domain.RegisteredUser;
import domain.Request;
import domain.SpringContextUtils;

/**
 * 
* <p>Title: RegistrationHandler</p>
* <p>Description: Class for handling SignUp request</p>
* @author Fan Wang
* @date Jan 28, 2015
 */

public class RegistrationHandler implements RequestHandler {

	private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);

	public void requestHander(List<Request> requestList) {
		
		log.debug("handling registration requests");
		
		List<RegisteredUser> userList = new ArrayList<RegisteredUser>();
	
		if (requestList != null && requestList.size() > 0) {
			for (Request r : requestList) {
				String emailAddress = r.getUserEmail();
				RegisteredUser ru = new RegisteredUser();
				ru.setEmailAddress(emailAddress);
				if(!CachedInfoUtils.hasUser(ru)){
				    userList.add(ru);
				    //for keep cached data sync with database 
				    CachedInfoUtils.addUser(ru);
				}
		

			}
			DataService ds = (DataService)SpringContextUtils.getBean("dataservice");
			ds.saveUserList(userList);
			EmailSender mailSender = (EmailSender) SpringContextUtils.getBean("emailsender");
			for(RegisteredUser ru : userList){
				EmailTemplate email = new EmailTemplate();
				email.setTo(ru.getEmailAddress());
				email.setSubject(EmailConstant.registerSuccess);
				email.setContent(EmailConstant.registerSuccessContent);
				mailSender.sendMailByAsynchronousMode(email);
			}
			
		}

	}

}
