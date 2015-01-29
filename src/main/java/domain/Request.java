package domain;

import java.util.List;

/**
 * 
* <p>Title: Request</p>
* <p>Description: Class for storing information of registration/watch/remove request</p>
* @author Fan Wang
* @date Jan 29, 2015
 */

public class Request {
	
	String userEmail;
	List<String> urllist;
	RequestType reqType;
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public List<String> getUrllist() {
		return urllist;
	}
	public void setUrllist(List<String> urllist) {
		this.urllist = urllist;
	}
	public RequestType getReqType() {
		return reqType;
	}
	public void setReqType(RequestType reqType) {
		this.reqType = reqType;
	}
	

}
