package domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 
* <p>Title: CachedInfoUtils</p>
* <p>Description: Class for caching all information needed</p>
* @author Fan Wang
* @date Jan 29, 2015
 */
public class CachedInfoUtils {
	private static Set<RegisteredUser> cachedUserSet;
	static{
		cachedUserSet = new HashSet<>();
	}
	public static Set<RegisteredUser> getCachedUserList(){
		return cachedUserSet;
	}
	public static void addUser(RegisteredUser user){
		cachedUserSet.add(user);
	}
	public static boolean hasUser(RegisteredUser user){
		return cachedUserSet.contains(user);
	}
	public static void removeUser(RegisteredUser user) {
		// TODO Auto-generated method stub
		cachedUserSet.remove(user);
	}
	public static void addUserList(List<RegisteredUser> userList){
		cachedUserSet.addAll(userList);
	}

}
