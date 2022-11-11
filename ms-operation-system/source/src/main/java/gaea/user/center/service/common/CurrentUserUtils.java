package gaea.user.center.service.common;

public class CurrentUserUtils {

	private static ThreadLocal<CurrentUser> userLocal = new ThreadLocal<>();
	
	public static CurrentUser getCurrentUser(){
		return userLocal.get();
	}
	
	public static void setCurrentUser(CurrentUser currentUser){
		userLocal.set(currentUser);
	}
	
	public static void clearCurrentUser(){
		userLocal.remove();
	}
}
