package domain;
/**
 * 
* <p>Title: RequestSite</p>
* <p>Description: Enum the supported sites </p>
* @author Fan Wang
* @date Jan 29, 2015
 */
public enum RequestSite {
       BERGDORFGOODMAN,BLOOMINGDALES,UNRECOGNIZEDSITE;
       
       public static RequestSite getSiteInfo(String url) {
   		// TODO Auto-generated method stub
   		
   		if(url.contains("bergdorfgoodman"))
   			return RequestSite.BERGDORFGOODMAN;
   		else if(url.contains("bloomingdales"))
   			return RequestSite.BLOOMINGDALES;
   		else
   			return RequestSite.UNRECOGNIZEDSITE;
   	}

   	
}
