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
package jp.co.nemuzuka.controller;

import javax.servlet.http.HttpSession;

import jp.co.nemuzuka.core.annotation.NoRegistCheck;
import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * ログアウトController.
 * @author kazumune
 */
public class LogoutController extends HtmlController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@NoRegistCheck
	@NoSessionCheck
	@Override
	protected Navigation execute() throws Exception {
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		
		String requestURL = request.getRequestURL().toString();
		String requestURI = request.getRequestURI();
		String path = requestURL.replaceAll(requestURI, "/");
		path = userService.createLogoutURL(path);
		return redirect(path);
	}

}
