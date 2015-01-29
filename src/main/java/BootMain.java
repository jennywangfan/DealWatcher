

import java.util.List;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.data.DataService;
import domain.CachedInfoUtils;
import domain.RegisteredUser;
import domain.SpringContextUtils;


public class BootMain {
	
	private static Logger log = LoggerFactory.getLogger(BootMain.class);
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args){
		DataService ds = (DataService) SpringContextUtils.getBean("dataservice");
		List<?> userList = ds.getAllUser();
		CachedInfoUtils.addUserList((List<RegisteredUser>) userList);
		JobDetailImpl job = (JobDetailImpl) SpringContextUtils.getBean("emailcheckingjob");
		Trigger trigger = (Trigger) SpringContextUtils.getBean("crontrigger");
		try {
		
			Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			log.error("SchedulerException "+e.getMessage());
		}
	}

}
