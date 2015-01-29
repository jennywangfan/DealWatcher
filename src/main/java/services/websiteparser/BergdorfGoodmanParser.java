package services.websiteparser;

import java.io.IOException;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import domain.RequestSite;
import domain.SimpleItemInfo;

/**
 * 
* <p>Title: BergdorfGoodmanParser</p>
* <p>Description: Class for parsing BergdorfGoodman link</p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public class BergdorfGoodmanParser implements ParseDealSite {
	
	private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);

	public SimpleItemInfo parseURL(String url)
			throws IOException {
		log.debug("Parsing url "+url);
		
	    String response = getContent(url);

		return handleResponse(response,url);

	}
	
	public SimpleItemInfo handleResponse(String content,String url){
		SimpleItemInfo info = new SimpleItemInfo();
		if(content != null){
			Document doc = Jsoup.parse(content);
			Elements soldOutElements = doc.select("img[class=soldOutFlag]");
			info.setUrl(url);
			if(soldOutElements != null && soldOutElements.size() > 0){
				info.setStockStatus(false);
				info.setOriginalPrice(-1.0);
				info.setSalePrice(-1.0);
				
			}
			else{
				info.setStockStatus(true);
				Elements originalPrice1 = doc.select("input[id=rPrice]");
				Elements salePrice1 = doc.select("input[id=sPrice]");
				Elements originalPrice2 = doc.select("div[class=price pos2]");
				Elements salePrice2 = doc
						.select("div[class=price pos1override]");
				String strOriginal = null;
				double oPrice = -1.0;
				if (originalPrice2 != null && originalPrice2.size() > 0) {
					strOriginal = originalPrice2.first().text().trim();
				    oPrice = parseToDouble(strOriginal);
					
				}
				else {
					strOriginal = originalPrice1.first().attr("value").trim();
					oPrice = Double.valueOf(strOriginal);
					
				}
				info.setOriginalPrice(oPrice);
				
				String strSale = null;
				double sPrice = -1.0;
				if (salePrice2 != null && salePrice2.size() > 0) {
					strSale = salePrice2.first().text().trim();
					sPrice = parseToDouble(strSale);
				}
				else{
					strSale = salePrice1.first().attr("value").trim();
					sPrice = Double.valueOf(strSale);
				}
				info.setSalePrice(sPrice);
				
			}
			info.setLastestUpdateDate(new Date());
		}
		info.setSiteInfo(RequestSite.BERGDORFGOODMAN);
		return info;
	}

	private double parseToDouble(String oPrice) {
		// TODO Auto-generated method stub
		if (oPrice == null || oPrice.length() == 0)
			return -1;
		int length = oPrice.length();
		StringBuilder sb = new StringBuilder();
		boolean isFirstDot = true;
		for (int i = 0; i < length; ++i) {
			char c = oPrice.charAt(i);
			if (i == 0 && (c < '0' || c > '9'))
				continue;
			else {
				if (c == ',')
					continue;
				else if (c == '.') {
					if (isFirstDot) {
						sb.append(c);
						isFirstDot = false;
						continue;
					} else
						return -1;
				} else if (c >= '0' && c <= '9')
					sb.append(c);
				else
					return -1;
			}

		}

		return Double.valueOf(sb.toString());
	}

//	public static void main(String[] args) {
//		String url = "http://www.bergdorfgoodman.com/Reed-Krakoff-Boxer-Micro-Tote-Bag-Green-Handbags/prod92530032_cat421106__/p.prod?icid=&searchType=EndecaDrivenCat&rte=%252Fcategory.jsp%253FitemId%253Dcat421106%2526pageSize%253D120%2526No%253D0%2526refinements%253D&eItemId=prod92530032&cmCat=product";
//		String url1 = "http://www.bergdorfgoodman.com/Nancy-Gonzalez-Crocodile-Small-Multi-Pocket-Satchel-Bag-Purple-Handbags/prod100460086_cat421106__/p.prod?icid=&searchType=EndecaDrivenCat&rte=%252Fcategory.jsp%253FitemId%253Dcat421106%2526pageSize%253D120%2526No%253D0%2526refinements%253D&eItemId=prod100460086&cmCat=product";
//		String url2 = "http://www.bergdorfgoodman.com/Proenza-Schouler-PS13-Bicolor-Satchel-Bag-Tan-White-Handbags/prod92140050_cat421106__/p.prod?icid=&searchType=EndecaDrivenCat&rte=%252Fcategory.jsp%253FitemId%253Dcat421106%2526pageSize%253D120%2526No%253D0%2526refinements%253D&eItemId=prod92140050&cmCat=product";
//		BergdorfGoodmanParser bgp = new BergdorfGoodmanParser();
//		try {
//			
//			SimpleItemInfo info = bgp.parseURL(url2);
//			System.out.println(info.toString());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

}
