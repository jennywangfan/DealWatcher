package services.jobschedule;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.data.DataService;
import services.websiteparser.BergdorfGoodmanParser;
import services.websiteparser.BloomingdalesParser;
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
			switch (siteInfo) {
			case BERGDORFGOODMAN:
				BergdorfGoodmanParser bgparse = (BergdorfGoodmanParser) SpringContextUtils
						.getBean("bgparser");

				for (String url : urlList) {
					SimpleItemInfo itemInfo = bgparse.parseURL(url);
					ds.savePriceHistory(itemInfo);
					ds.updateWatchedItem(itemInfo);
					// TODO use the following code to hold the thread for a
					// specific time in case the IP is blocked by a web site due
					// to
					// the high volume of request
					 TimeUnit.SECONDS.sleep(5);

				}
				break;
			case BLOOMINGDALES:
				BloomingdalesParser bdparse = (BloomingdalesParser) SpringContextUtils
						.getBean("bdparser");

				for (String url : urlList) {
					SimpleItemInfo itemInfo = bdparse.parseURL(url);
					ds.savePriceHistory(itemInfo);
					ds.updateWatchedItem(itemInfo);
					// TODO use the following code to hold the thread for a
					// specific time in case the IP is blocked by a web site due
					// to
					// the high volume of request
					 TimeUnit.SECONDS.sleep(5);

				}
				break;

			default:
				break;
			}
		} catch (RuntimeException e) {
			log.error("RuntimeException  " + e.getMessage());
			return false;
		}
		return true;
	}

}
