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
* <p>Title: BloomingdalesParser</p>
* <p>Description: Class for parsing Bloomingdales link</p>
* @author Fan Wang
* @date Jan 28, 2015
 */
public class BloomingdalesParser implements ParseDealSite {
	
	private static Logger log = LoggerFactory.getLogger(QuartzJobBean.class);


	@Override
	public SimpleItemInfo parseURL(String url) throws IOException {
		log.debug("Parsing url " + url);

	    String response = getContent(url);

		return handleResponse(response,url);
		}

	private SimpleItemInfo handleResponse(String response, String url) {
		SimpleItemInfo info = new SimpleItemInfo();
		if(response != null){
			Document doc = Jsoup.parse(response);
			Elements soldOutElements = doc.select("div[id=pdp_unavailable]");;
			info.setUrl(url);
			if(soldOutElements != null && soldOutElements.size() > 0){
				info.setStockStatus(false);
				info.setOriginalPrice(-1.0);
				info.setSalePrice(-1.0);
				
			}
			else{
				info.setStockStatus(true);
				Elements orginalPrice = doc.select("span[class=priceBig]");
				Elements salePrice = doc.select("span[class=priceSale]");
				
				double oPrice = -1.0;
				if(orginalPrice != null && orginalPrice.size() > 0)
				    oPrice = parseToDouble(orginalPrice.first().text().trim());
				info.setOriginalPrice(oPrice);
				
				
				double sPrice = -1.0;
				if(salePrice != null && salePrice.size() > 0)
					sPrice = parseToDouble(salePrice.first().text().trim());
				else
					sPrice = oPrice;
				info.setSalePrice(sPrice);
				
			}
			info.setLastestUpdateDate(new Date());
		}
		info.setSiteInfo(RequestSite.BLOOMINGDALES);
		return info;
	}

	private double parseToDouble(String oPrice) {
		if (oPrice == null || oPrice.length() == 0)
			return -1;
		int length = oPrice.length();
		StringBuilder sb = new StringBuilder();
		boolean isFirstDot = true;
		boolean isFirstDollar = false;
		for (int i = 0; i < length; ++i) {
			char c = oPrice.charAt(i);
			if (!isFirstDollar ){
				if(c == '$')
					isFirstDollar = true;
				continue;
			}
			
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
	}

