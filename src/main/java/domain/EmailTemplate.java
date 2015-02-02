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
	
	
    private String signiture;
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
