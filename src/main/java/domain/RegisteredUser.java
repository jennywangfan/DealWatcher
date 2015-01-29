package domain;

import services.data.DataService;
/**
 * 
* <p>Title: RegisteredUser</p>
* <p>Description: Class for storing registered user information</p>
* @author Fan Wang
* @date Jan 29, 2015
 */

public class RegisteredUser {
	private String emailAddress;
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public static void main(String[] args){
		RegisteredUser ru = new RegisteredUser();
		ru.setEmailAddress("janeeyre0818@gmail.com");
		DataService ds = new DataService();
		ds.saveUser(ru);
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
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
		RegisteredUser other = (RegisteredUser) obj;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		return true;
	}

	public String toString(){
		return emailAddress;
	}
//	public List<WatchedItem> getWatchedlist() {
//		return watchedlist;
//	}
//
//	public void setWatchedlist(List<WatchedItem> watchedlist) {
//		this.watchedlist = watchedlist;
//	}
	
	
    

}
