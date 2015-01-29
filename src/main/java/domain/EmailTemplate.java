package domain;

import java.io.Serializable;
import java.util.List;

/**
 * 
* <p>Title: EmailTemplate</p>
* <p>Description: Class for storing email information for sending</p>
* @author Fan Wang
* @date Jan 29, 2015
 */
public class EmailTemplate implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String subject;
	private String to;
	private String content;
	private String[] cc;
	private String[] bcc;
	
	
    //private Content[] content;
    private String signiture;
	
    class Content{
    	private String url;
    	private Double iniOriPrice;
    	private Double curOriPrice;
    	private Double iniSalePrice;
    	private Double curSalePrice;
    	private boolean iniStockStatus;
    	private boolean curStockStatus;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public Double getIniOriPrice() {
			return iniOriPrice;
		}
		public void setIniOriPrice(Double iniOriPrice) {
			this.iniOriPrice = iniOriPrice;
		}
		public Double getCurOriPrice() {
			return curOriPrice;
		}
		public void setCurOriPrice(Double curOriPrice) {
			this.curOriPrice = curOriPrice;
		}
		public Double getIniSalePrice() {
			return iniSalePrice;
		}
		public void setIniSalePrice(Double iniSalePrice) {
			this.iniSalePrice = iniSalePrice;
		}
		public Double getCurSalePrice() {
			return curSalePrice;
		}
		public void setCurSalePrice(Double curSalePrice) {
			this.curSalePrice = curSalePrice;
		}
		public boolean isIniStockStatus() {
			return iniStockStatus;
		}
		public void setIniStockStatus(boolean iniStockStatus) {
			this.iniStockStatus = iniStockStatus;
		}
		public boolean isCurStockStatus() {
			return curStockStatus;
		}
		public void setCurStockStatus(boolean curStockStatus) {
			this.curStockStatus = curStockStatus;
		}
		
		public String toString(){
			StringBuilder sb = new StringBuilder();
			sb.append(url);
			String newLine = System.getProperty("line.separator");
			sb.append(newLine);
			if(this.iniOriPrice != this.curOriPrice){
				sb.append("Original Price change from " + this.iniOriPrice +"to " + this.curOriPrice);
				sb.append(newLine);
			}
			if(this.iniSalePrice != this.curSalePrice)
			{
				sb.append("Sale Price changed from " + this.iniSalePrice +"to " + this.curSalePrice);
				sb.append(newLine);
			}
			if(this.iniStockStatus != this.curStockStatus){
				if(iniStockStatus)
				sb.append("Item is sold out!");
				else
				sb.append("Item is in stock!");
				sb.append(newLine);
			}
			return sb.toString();
			
		}
    	
    }

    public EmailTemplate(){
    	
    }
    
	public EmailTemplate(String userEmail, String watchedListAdded,
			List<String> successAddedUrl) {
		// TODO Auto-generated constructor stub
		this.to = userEmail;
		this.subject = watchedListAdded;
		
		StringBuffer sb = new StringBuffer();
		String lineSeperator = System.getProperties().getProperty("line.separator");
		sb.append(watchedListAdded);
		sb.append(lineSeperator);
		for(String url : successAddedUrl){
			sb.append(url);
			sb.append(lineSeperator);
		}
		this.content = sb.toString();
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}


//	public Content[] getContent() {
//		return content;
//	}
//	
//	public String getAllContent(){
//		StringBuilder sb = new StringBuilder();
//		for(Content c : content){
//			sb.append(c.toString());
//		}
//		return sb.toString();
//	}
//
//	public void setContent(Content[] content) {
//		this.content = content;
//	}

	public String getSigniture() {
		return signiture;
	}

	public void setSigniture(String signiture) {
		this.signiture = signiture;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getCc() {
		return cc;
	}

	public void setCc(String[] cc) {
		this.cc = cc;
	}

	public String[] getBcc() {
		return bcc;
	}

	public void setBcc(String[] bcc) {
		this.bcc = bcc;
	}
	
	public String toString(){
		String space = "   ";
		StringBuilder sb = new StringBuilder();
		sb.append("To : " + this.to + space);
		sb.append("Subject : " + this.subject + space);
		sb.append("Content : " +this.content + space);
		return sb.toString();
	}

}
