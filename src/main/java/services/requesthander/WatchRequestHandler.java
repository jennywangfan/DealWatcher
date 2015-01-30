package services.requesthander;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.data.DataService;
import services.email.EmailSender;
import services.websiteparser.DealParserFactory;
import services.websiteparser.DealSiteParser;
import domain.EmailConstant;
import domain.EmailTemplate;
import domain.RegisteredUser;
import domain.Request;
import domain.RequestSite;
import domain.SimpleItemInfo;
import domain.SpringContextUtils;
import domain.WatchedItem;
/**
 * 
* <p>Title: WatchRequestHandler</p>
* <p>Description: Class for handling watch requests </p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public class WatchRequestHandler implements RequestHandler {
	
	
	
	private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);

	
	//handle new watch request from users
	public void requestHander(List<Request> requestList) {
		
		log.debug("Handling watch requests");
		for(Request r : requestList){
			
			String userEmail = r.getUserEmail();
			RegisteredUser user = new RegisteredUser();
			user.setEmailAddress(userEmail);
			
			DataService ds = (DataService)SpringContextUtils.getBean("dataservice");
			DealSiteParser bgp = DealParserFactory.getParser(RequestSite.BERGDORFGOODMAN);
			DealSiteParser bdp = DealParserFactory.getParser(RequestSite.BLOOMINGDALES);
			List<String> successAddedUrl = new ArrayList<String>();
			for(String url : r.getUrllist()){
				WatchedItem wi = new WatchedItem();
				wi.setUser(user);
				wi.setUrl(url);
				RequestSite site= RequestSite.getSiteInfo(url);
				SimpleItemInfo info = null;
				switch(site){
				case BERGDORFGOODMAN :
					try {
						info = bgp.parseURL(url);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.error(e.getMessage());
						//e.printStackTrace();
					}
					break;
				case BLOOMINGDALES :
					try {
						info = bdp.parseURL(url);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						log.error(e.getMessage());
					}
					break;
				default:
					break;
				
				
				}
				
				if(info != null){
					ds.savePriceHistory(info);
					Double oPrice = info.getOriginalPrice();
					Double sPrice = info.getSalePrice();
					Boolean inStock = info.getStockStatus();
					wi.setInitialStockStatus(inStock);
					wi.setLastStockStatus(inStock);
					wi.setCurrentStockStatus(inStock);
					wi.setInitialOriginalPrice(oPrice);
					wi.setInitialSalePrice(sPrice);
					wi.setLastOriginalPrice(oPrice);
					wi.setLastSalePrice(sPrice);
					wi.setCurrentOriginalPrice(oPrice);
					wi.setCurrentSalePrice(sPrice);
					wi.setSiteInfo(site);
					ds.saveWatchedItem(wi);
					//email user the 
					successAddedUrl.add(url);
				}
				
			}
			
			EmailTemplate email = new EmailTemplate(userEmail,EmailConstant.watchedListAdded,successAddedUrl);
			EmailSender sender = (EmailSender)SpringContextUtils.getBean("emailsender");		
			sender.sendMailByAsynchronousMode(email);
			
		}
			
			
			
		}

	

}
