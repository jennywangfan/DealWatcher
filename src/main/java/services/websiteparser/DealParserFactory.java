/**
* <p>Title: ParserFactory.java</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2015</p>
* <p>All Right Reserved</p>
* @author Fan Wang
* @date Jan 29, 2015
* @version 1.0
*/
package services.websiteparser;

import domain.RequestSite;

/**
 * <p>Title: ParserFactory</p>
 * <p>Description: </p>
 * @author Fan Wang
 * @date Jan 29, 2015
 */
public class DealParserFactory {
	private static BergdorfGoodmanParser bgpaser;
	private static BloomingdalesParser bdpaser;
	static{
		bgpaser = new BergdorfGoodmanParser();
		bdpaser = new BloomingdalesParser();
	}

	public static DealSiteParser getParser(RequestSite site){
		switch(site){
		case BERGDORFGOODMAN:
			return bgpaser;
		case BLOOMINGDALES:
			return bdpaser;
		default:
			break;
		}
		return null;
		
	}
}
