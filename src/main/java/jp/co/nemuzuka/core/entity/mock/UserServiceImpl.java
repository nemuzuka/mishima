/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.nemuzuka.core.entity.mock;

import java.util.Set;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;

public class UserServiceImpl implements UserService {

	public static final String DUMMY_EMAIL = "hoge@hoge.hoge";
	
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
		User user = new User(DUMMY_EMAIL, "");
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
