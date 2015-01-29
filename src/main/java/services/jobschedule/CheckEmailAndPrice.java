package services.jobschedule;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.mail.MessagingException;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import services.data.DataService;
import services.email.EmailChecker;
import services.email.SendEmailForDeals;
import domain.RequestSite;
import domain.SpringContextUtils;

/**
 * 
* <p>Title: CheckEmailAndPrice</p>
* <p>Description: Class for implements a scheduled job to check email and price for all Urls </p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public class CheckEmailAndPrice extends QuartzJobBean {
	private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);

    private int timeout;
    
   
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		// TODO Auto-generated method stub
		log.debug("Email checking job started");
		EmailChecker emailChecker = (EmailChecker)SpringContextUtils.getBean("mailchecker");
		DataService ds = (DataService)SpringContextUtils.getBean("dataservice");
		ExecutorService es = Executors.newCachedThreadPool() ; 
		try {
			emailChecker.check();
			//log.info("check finished");
			Map<RequestSite,List<String>> urlMap = ds.getDistinctUrls();
			List<String> bgList = null;
			List<String> bdList = null;
			Future<Boolean> bgSuccess = null;
			Future<Boolean> bdSuccess = null;
			for(RequestSite site : urlMap.keySet()){
				switch(site){
				case BERGDORFGOODMAN:
					bgList = urlMap.get(RequestSite.BERGDORFGOODMAN);
					bgSuccess = es.submit(new PriceRefresher(bgList, RequestSite.BERGDORFGOODMAN));
					break;
				case BLOOMINGDALES:
					bdList = urlMap.get(RequestSite.BLOOMINGDALES);
					bdSuccess = es.submit(new PriceRefresher(bdList, RequestSite.BLOOMINGDALES));
					break;
				default:
					break;
				}
			}
			
			
			
			if((urlMap.containsKey(RequestSite.BERGDORFGOODMAN) && bgSuccess.get() || (urlMap.containsKey(RequestSite.BLOOMINGDALES) && bdSuccess.get()))&&bdSuccess.get()){
				SendEmailForDeals sendNotify = (SendEmailForDeals) SpringContextUtils.getBean("dealmailsender");				
				sendNotify.sendNotifyEmail();
			}
			if(urlMap.containsKey(RequestSite.BERGDORFGOODMAN) && !bgSuccess.get()){
			    log.error("Something wrong when checking bergdorfgoodman Urls");
			}
			if(urlMap.containsKey(RequestSite.BLOOMINGDALES) && !bdSuccess.get())
			{
				log.error("Something wrong when checking bloomingdales Urls");
			}
			
			
		} catch (MessagingException e) {
			log.error("MessagingException "  + e.getMessage());
			
		} catch (InterruptedException e) {
			log.error("InterruptedException "  + e.getMessage());
			
		} catch (ExecutionException e) {
			log.error("ExecutionException "  + e.getMessage());
			
		} catch(IOException e){
			log.error("IOException "  + e.getMessage());
			
		}
		finally{
			if(es != null)
				es.shutdown();
		}
		
	}



	public int getTimeout() {
		return timeout;
	}



	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}


}
