package domain;

import java.io.Serializable;
import java.util.Date;
/**
 * 
* <p>Title: WatchedItem</p>
* <p>Description: Class for each Watched Item</p>
* @author Fan Wang
* @date Jan 29, 2015
 */

public class WatchedItem implements Serializable{
	/** long   serialVersionUID */
	private static final long serialVersionUID = 1L;
	private RegisteredUser user;
	private String url;
	private Double initialOriginalPrice;
	private Double currentOriginalPrice;
	private Double lastOriginalPrice;
	private Double lastSalePrice;
	private Double initialSalePrice;
	private Double currentSalePrice;
	private Boolean initialStockStatus;
	private Boolean currentStockStatus;
	private Boolean lastStockStatus;
	private Date lastUpdateTimeStamp;
	private RequestSite siteInfo;
	
	public WatchedItem(){
		this.initialOriginalPrice = -1.0;
		this.currentOriginalPrice = -1.0;
		this.lastOriginalPrice = -1.0;
		this.lastSalePrice = -1.0;
		this.initialSalePrice = -1.0;
		this.currentSalePrice = -1.0;
		this.initialStockStatus = false;
		this.currentStockStatus = false;
		this.lastStockStatus = false;
		this.lastUpdateTimeStamp = new Date();
		
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public RegisteredUser getUser() {
		return user;
	}
	public void setUser(RegisteredUser user) {
		this.user = user;
	}
//	public Integer getItemId() {
//		return itemId;
//	}
//	public void setItemId(Integer itemId) {
//		this.itemId = itemId;
//	}
	public boolean isInitialStockStatus() {
		return initialStockStatus;
	}
	public void setInitialStockStatus(boolean originalStockStatus) {
		this.initialStockStatus = originalStockStatus;
	}
	public boolean isCurrentStockStatus() {
		return currentStockStatus;
	}
	public void setCurrentStockStatus(boolean currentStockStatus) {
		this.currentStockStatus = currentStockStatus;
	}
	public Double getInitialOriginalPrice() {
		return initialOriginalPrice;
	}
	public void setInitialOriginalPrice(Double initialOriginalPrice) {
		this.initialOriginalPrice = initialOriginalPrice;
	}
	public Double getCurrentOriginalPrice() {
		return currentOriginalPrice;
	}
	public void setCurrentOriginalPrice(Double currentOriginalPrice) {
		this.currentOriginalPrice = currentOriginalPrice;
	}
	public Double getInitialSalePrice() {
		return initialSalePrice;
	}
	public void setInitialSalePrice(Double intialSalePrice) {
		this.initialSalePrice = intialSalePrice;
	}
	public Double getCurrentSalePrice() {
		return currentSalePrice;
	}
	public void setCurrentSalePrice(Double currentSalePrice) {
		this.currentSalePrice = currentSalePrice;
	}

	public Double getLastOriginalPrice() {
		return lastOriginalPrice;
	}

	public void setLastOriginalPrice(Double lastOriginalPrice) {
		this.lastOriginalPrice = lastOriginalPrice;
	}

	public Double getLastSalePrice() {
		return lastSalePrice;
	}

	public void setLastSalePrice(Double lastSalePrice) {
		this.lastSalePrice = lastSalePrice;
	}

	public boolean isLastStockStatus() {
		return lastStockStatus;
	}

	public void setLastStockStatus(boolean lastStockStatus) {
		this.lastStockStatus = lastStockStatus;
	}

	public Date getLastUpdateTimeStamp() {
		return lastUpdateTimeStamp;
	}

	public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
		this.lastUpdateTimeStamp = lastUpdateTimeStamp;
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
				+ ((currentOriginalPrice == null) ? 0 : currentOriginalPrice
						.hashCode());
		result = prime
				* result
				+ ((currentSalePrice == null) ? 0 : currentSalePrice.hashCode());
		result = prime
				* result
				+ ((currentStockStatus == null) ? 0 : currentStockStatus
						.hashCode());
		result = prime
				* result
				+ ((initialOriginalPrice == null) ? 0 : initialOriginalPrice
						.hashCode());
		result = prime
				* result
				+ ((initialSalePrice == null) ? 0 : initialSalePrice.hashCode());
		result = prime
				* result
				+ ((initialStockStatus == null) ? 0 : initialStockStatus
						.hashCode());
		result = prime
				* result
				+ ((lastOriginalPrice == null) ? 0 : lastOriginalPrice
						.hashCode());
		result = prime * result
				+ ((lastSalePrice == null) ? 0 : lastSalePrice.hashCode());
		result = prime * result
				+ ((lastStockStatus == null) ? 0 : lastStockStatus.hashCode());
		result = prime
				* result
				+ ((lastUpdateTimeStamp == null) ? 0 : lastUpdateTimeStamp
						.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		WatchedItem other = (WatchedItem) obj;
		if (currentOriginalPrice == null) {
			if (other.currentOriginalPrice != null)
				return false;
		} else if (!currentOriginalPrice.equals(other.currentOriginalPrice))
			return false;
		if (currentSalePrice == null) {
			if (other.currentSalePrice != null)
				return false;
		} else if (!currentSalePrice.equals(other.currentSalePrice))
			return false;
		if (currentStockStatus == null) {
			if (other.currentStockStatus != null)
				return false;
		} else if (!currentStockStatus.equals(other.currentStockStatus))
			return false;
		if (initialOriginalPrice == null) {
			if (other.initialOriginalPrice != null)
				return false;
		} else if (!initialOriginalPrice.equals(other.initialOriginalPrice))
			return false;
		if (initialSalePrice == null) {
			if (other.initialSalePrice != null)
				return false;
		} else if (!initialSalePrice.equals(other.initialSalePrice))
			return false;
		if (initialStockStatus == null) {
			if (other.initialStockStatus != null)
				return false;
		} else if (!initialStockStatus.equals(other.initialStockStatus))
			return false;
		if (lastOriginalPrice == null) {
			if (other.lastOriginalPrice != null)
				return false;
		} else if (!lastOriginalPrice.equals(other.lastOriginalPrice))
			return false;
		if (lastSalePrice == null) {
			if (other.lastSalePrice != null)
				return false;
		} else if (!lastSalePrice.equals(other.lastSalePrice))
			return false;
		if (lastStockStatus == null) {
			if (other.lastStockStatus != null)
				return false;
		} else if (!lastStockStatus.equals(other.lastStockStatus))
			return false;
		if (lastUpdateTimeStamp == null) {
			if (other.lastUpdateTimeStamp != null)
				return false;
		} else if (!lastUpdateTimeStamp.equals(other.lastUpdateTimeStamp))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	

}
