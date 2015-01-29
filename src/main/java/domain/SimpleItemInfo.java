package domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
* <p>Title: SimpleItemInfo</p>
* <p>Description: Class for storing item history information </p>
* @author Fan Wang
* @date Jan 29, 2015
 */

public class SimpleItemInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String url;
	private Double originalPrice;
	private Double salePrice;
	private Date lastestUpdateDate;
	private Boolean stockStatus;
	private RequestSite siteInfo;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Double getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}
	public Double getSalePrice() {
		return salePrice;
	}
	public void setSalePrice(Double salePrice) {
		this.salePrice = salePrice;
	}
	public Date getLastestUpdateDate() {
		return lastestUpdateDate;
	}
	public void setLastestUpdateDate(Date lastestUpdateDate) {
		this.lastestUpdateDate = lastestUpdateDate;
	}
	
	public Boolean getStockStatus() {
		return stockStatus;
	}
	public void setStockStatus(Boolean stockStatus) {
		this.stockStatus = stockStatus;
	}
	
    public String toString(){
    	String lineSeperator = System.getProperties().getProperty("line.separator");
    	return this.url +lineSeperator  + this.originalPrice + lineSeperator + this.salePrice + lineSeperator + this.lastestUpdateDate + lineSeperator +this.stockStatus;
    }
	public RequestSite getSiteInfo() {
		return siteInfo;
	}
	public void setSiteInfo(RequestSite siteInfo) {
		this.siteInfo = siteInfo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((lastestUpdateDate == null) ? 0 : lastestUpdateDate
						.hashCode());
		result = prime * result
				+ ((originalPrice == null) ? 0 : originalPrice.hashCode());
		result = prime * result
				+ ((salePrice == null) ? 0 : salePrice.hashCode());
		result = prime * result
				+ ((siteInfo == null) ? 0 : siteInfo.hashCode());
		result = prime * result
				+ ((stockStatus == null) ? 0 : stockStatus.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleItemInfo other = (SimpleItemInfo) obj;
		if (lastestUpdateDate == null) {
			if (other.lastestUpdateDate != null)
				return false;
		} else if (!lastestUpdateDate.equals(other.lastestUpdateDate))
			return false;
		if (originalPrice == null) {
			if (other.originalPrice != null)
				return false;
		} else if (!originalPrice.equals(other.originalPrice))
			return false;
		if (salePrice == null) {
			if (other.salePrice != null)
				return false;
		} else if (!salePrice.equals(other.salePrice))
			return false;
		if (siteInfo != other.siteInfo)
			return false;
		if (stockStatus == null) {
			if (other.stockStatus != null)
				return false;
		} else if (!stockStatus.equals(other.stockStatus))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}
	
	
}
