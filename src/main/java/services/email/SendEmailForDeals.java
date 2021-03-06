package services.email;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.data.DataService;
import domain.EmailConstant;
import domain.EmailTemplate;
import domain.RequestSite;
import domain.SpringContextUtils;
import domain.WatchedItem;

/**
 * 
 * <p>
 * Title: SendEmailForDeals
 * </p>
 * <p>
 * Description: Class for sending notifications for those price changed items
 * </p>
 * @author Fan Wang
 * @date Jan 28, 2015
 */
public class SendEmailForDeals {
	private static Logger log = LoggerFactory
			.getLogger(SendEmailForDeals.class);

	/**
	 * 
	 * <p>
	 * Title: sendNotifyEmail
	 * </p>
	 * <p>
	 * Description: Get all price changed items and send email to notify users
	 * </p>
	 */
	public void sendNotifyEmail() {
		DataService ds = (DataService) SpringContextUtils
				.getBean("dataservice");
		List<?> dealList = ds.getPriceChangedItems();
		EmailSender emailSender = (EmailSender) SpringContextUtils
				.getBean("emailsender");
		for (Object o : dealList) {
			WatchedItem wi = (WatchedItem) o;
			EmailTemplate email;
			try {
				email = prepareEmail(wi);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info(e.getMessage());
				continue;
			}
			emailSender.sendMailByAsynchronousMode(email);
		}
	}
	 /**
	 *<p>
	 * Title: sendNotifyEmail
	 * </p>
	 * <p>
	 * Description: Get all price changed items and send email to notify users
	 * </p>
	 */
	public void sendNotifyEmail(RequestSite siteInfo) {
		DataService ds = (DataService) SpringContextUtils
				.getBean("dataservice");
		List<?> dealList = ds.getPriceChangedItems(siteInfo);
		EmailSender emailSender = (EmailSender) SpringContextUtils
				.getBean("emailsender");
		for (Object o : dealList) {
			WatchedItem wi = (WatchedItem) o;
			EmailTemplate email;
			try {
				email = prepareEmail(wi);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.info(e.getMessage());
				continue;
			}
			emailSender.sendMailByAsynchronousMode(email);
		}
	}
	/**
	 * 
	 * <p>
	 * Title: prepareEmail
	 * </p>
	 * <p>
	 * Description: prepare an email for a WatchedItem
	 * </p>
	 * 
	 * @param wi
	 * @return EmailTemplate for sending
	 * @throws Exception
	 */
	private EmailTemplate prepareEmail(WatchedItem wi) throws Exception {
		log.debug("Start to Prepare Email");
		EmailTemplate email = new EmailTemplate();
		email.setTo(wi.getUser().getEmailAddress());
		//email.setSubject(EmailConstant.itemPriceChanged);
		StringBuilder content = new StringBuilder();
		String space = "   ";
		content.append(wi.getUrl());
		content.append(EmailConstant.lineSeperator);
		
		if(wi.isCurrentStockStatus()){
			if(!wi.isInitialStockStatus()){
				email.setSubject(EmailConstant.itemBackInStock);
				content.append(EmailConstant.inStock);
				content.append(EmailConstant.lineSeperator);
				if (wi.getCurrentOriginalPrice().equals(
						wi.getCurrentSalePrice())) {
					content.append(EmailConstant.PriceIs);
					content.append(wi.getCurrentOriginalPrice());
				} else {
					content.append(EmailConstant.originalPriceIs);
					content.append(wi.getCurrentOriginalPrice());
					content.append(EmailConstant.lineSeperator);
					content.append(EmailConstant.salePriceIs);
					content.append(wi.getCurrentSalePrice());
				}
			}else{
				email.setSubject(EmailConstant.itemPriceChanged);
				boolean boPrice = wi.getCurrentOriginalPrice().equals(wi.getInitialOriginalPrice());
				boolean bsPrice = wi.getCurrentSalePrice().equals(wi.getInitialSalePrice());
				if(boPrice && bsPrice)
				   throw new Exception("Nothing Changed for this item");
				if(!boPrice){
					content.append(EmailConstant.originalPriceWas);
					content.append(wi.getInitialOriginalPrice());
					content.append(space);
					content.append(EmailConstant.originalPriceIs);
					content.append(wi.getCurrentOriginalPrice());
				}
				if(!bsPrice){
					content.append(EmailConstant.lineSeperator);
					content.append(EmailConstant.salePriceWas);
					content.append(wi.getInitialSalePrice());
					content.append(space);
					content.append(EmailConstant.salePriceIs);
					content.append(wi.getCurrentSalePrice());
				}
			}
		}else{
			if(wi.isInitialStockStatus()){
				email.setSubject(EmailConstant.itemOutOfStock);
				content.append(EmailConstant.outOfStock);
			}
			else
				throw new Exception("Nothing Changed for this item");
		}
		
		
		email.setContent(content.toString());
		log.debug("Email prepared " + email.toString());
		return email;
	}

}
