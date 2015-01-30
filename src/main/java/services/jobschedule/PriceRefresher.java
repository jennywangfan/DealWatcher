package services.jobschedule;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.data.DataService;
import services.email.SendEmailForDeals;
import services.websiteparser.DealParserFactory;
import services.websiteparser.DealSiteParser;
import domain.RequestSite;
import domain.SimpleItemInfo;
import domain.SpringContextUtils;

/**
 * 
 * <p>
 * Title: PriceRefresher
 * </p>
 * <p>
 * Description: Class for starting a thread to run for different site to get the
 * latest price information
 * </p>
 * 
 * @author Fan Wang
 * @date Jan 28, 2015
 */
public class PriceRefresher implements Callable<Boolean> {

	private List<String> urlList;
	private RequestSite siteInfo;
	private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);

	public PriceRefresher(List<String> list, RequestSite site) {
		urlList = list;
		siteInfo = site;
	}

	@Override
	public Boolean call() throws Exception {
		log.debug("PriceRefresher thread starting to check updates from"
				+ siteInfo.name());
		DataService ds = (DataService) SpringContextUtils
				.getBean("dataservice");
		try {
			DealSiteParser dealParser = DealParserFactory.getParser(siteInfo);
			for(String url : urlList){
				SimpleItemInfo itemInfo = dealParser.parseURL(url);
				ds.savePriceHistory(itemInfo);
				ds.updateWatchedItem(itemInfo);
				TimeUnit.SECONDS.sleep(RequestSite.getSiteHoldTimeSeconds(siteInfo));
			}
			SendEmailForDeals dealSender = (SendEmailForDeals) SpringContextUtils.getBean("dealmailsender");
			dealSender.sendNotifyEmail(siteInfo);
		} catch (RuntimeException e) {
			log.error("RuntimeException  " + e.getMessage());
			return false;
		}
		return true;
	}

}
