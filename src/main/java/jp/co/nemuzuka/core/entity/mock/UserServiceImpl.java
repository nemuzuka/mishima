package jp.co.nemuzuka.core.entity.mock;

import java.util.Set;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

public class UserServiceImpl implements UserService {

	private UserService userService;
	
	/**
	 * コンストラクタ.
	 * @param userService 本来のUserService
	 */
	public UserServiceImpl(UserService userService) {
		this.userService = userService;
	}
	
	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#createLoginURL(java.lang.String)
	 */
	@Override
	public String createLoginURL(String destinationURL) {
		return userService.createLoginURL(destinationURL);
	}

	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#createLoginURL(java.lang.String, java.lang.String)
	 */
	@Override
	public String createLoginURL(String destinationURL, String authDomain) {
		return userService.createLoginURL(destinationURL, authDomain);
	}

	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#createLoginURL(java.lang.String, java.lang.String, java.lang.String, java.util.Set)
	 */
	@Override
	public String createLoginURL(String destinationURL, String authDomain,
			String federatedIdentity, Set<String> attributesRequest) {
		return userService.createLoginURL(destinationURL, authDomain, federatedIdentity, attributesRequest);
	}

	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#createLogoutURL(java.lang.String)
	 */
	@Override
	public String createLogoutURL(String destinationURL) {
		return userService.createLogoutURL(destinationURL);
	}

	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#createLogoutURL(java.lang.String, java.lang.String)
	 */
	@Override
	public String createLogoutURL(String destinationURL, String authDomain) {
		return userService.createLogoutURL(destinationURL, authDomain);
	}

	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#getCurrentUser()
	 */
	@Override
	public User getCurrentUser() {
		User user = new User("hoge@hoge.hoge", "");
		return user;
	}

	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#isUserAdmin()
	 */
	@Override
	public boolean isUserAdmin() {
		return false;
	}

	/* (non-Javadoc)
	 * @see com.google.appengine.api.users.UserService#isUserLoggedIn()
	 */
	@Override
	public boolean isUserLoggedIn() {
		return false;
	}
}
