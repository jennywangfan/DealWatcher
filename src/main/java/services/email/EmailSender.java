
package services.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import domain.EmailTemplate;

/**
* 
* <p>Title: EmailSender</p>
* <p>Description: Class for sending email using JavaMailSenderImpl in Java Mail. There are two ways to send email.
*    one is synchronous mode, another is using multithreading to act as asynchronous mode. you could set 
*    threads pool size at /src/main/java/bean.xml </p>
* <p>All Right Reserved</p> 
* @author Fan Wang
* @date Jan 28, 2015
*/
public class EmailSender {

	private JavaMailSender mailSender;
	private TaskExecutor taskExecutor;
	private static Logger log = LoggerFactory.getLogger(EmailSender.class);

	public EmailSender() {

	}
	
	/**
	 * 
	* <p>Title: sendMailBySynchronousMode</p>
	* <p>Description: Send email in synchronous mode</p>
	* @param email
	* @throws MessagingException
	 */

	public void sendMailBySynchronousMode(EmailTemplate email)
			throws MessagingException {

		log.debug("Sending email by synchronous mode ");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setTo(email.getTo());
		String[] cc = email.getCc();
		if (cc != null && cc.length > 0)
			helper.setCc(cc);
		String[] bcc = email.getBcc();
		if (bcc != null && bcc.length > 0)
			helper.setBcc(bcc);
		helper.setSubject(email.getSubject());
		helper.setText(email.getContent());
		mailSender.send(message);

	}
	
	/**
	 * 
	* <p>Title: sendMailByAsynchronousMode</p>
	* <p>Description: Send email in asynchronous mode via org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
	*    which could be set at /src/main/java/bean.xml</p>
	* @param email
	 */

	public void sendMailByAsynchronousMode(final EmailTemplate email) {

		log.debug("Sending email by asynchronous mode ");
		taskExecutor.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					sendMailBySynchronousMode(email);
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		});

	}


	public TaskExecutor getTaskExecutor() {
		return taskExecutor;
	}

	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	public JavaMailSender getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

}
