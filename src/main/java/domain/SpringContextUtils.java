package domain;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
* <p>Title: SpringContextUtils</p>
* <p>Description: Class for spring bean container</p>
* @author Fan Wang
* @date Jan 29, 2015
 */
public class SpringContextUtils  {

	private static ApplicationContext context; 
	
	static {
		context = new ClassPathXmlApplicationContext(new String[] {"bean.xml"});
	}
	
    public static Object getBean(String beanName){
    	return context.getBean(beanName);

    }
    public static<T> T getBean(String beanName, Class<T> clazz){
    	return context.getBean(beanName, clazz);
    }
	
	
}
