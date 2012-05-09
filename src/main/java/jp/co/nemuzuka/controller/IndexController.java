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

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slim3.controller.Navigation;

import com.google.appengine.api.users.User;

/**
 * メインController.
 * アプリケーション管理者の場合、MemberModelが登録されているか確認し、
 * 登録されていない場合、Modelをputします。
 * @author kazumune
 */
public class IndexController extends HtmlController {

	private MemberService memberService = MemberServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@NoSessionCheck
	@Override
	protected Navigation execute() throws Exception {
		
		if(userService.isUserAdmin()) {
			//アプリケーション管理者の場合、MemberModelに登録されているかチェック
			User currentUser = userService.getCurrentUser();
			memberService.checkAndCreateMember(
					currentUser.getEmail(),
					currentUser.getNickname(),
					Authority.admin);
		} else if(StringUtils.isNotEmpty((String)requestScope(USE_TRIAL_USER))) {
			//アプリケーション管理者でなく、trialユーザを使用する場合、MemberModelに登録されているかチェック
			User currentUser = userService.getCurrentUser();
			memberService.checkAndCreateMember(
					currentUser.getEmail(),
					currentUser.getEmail(),
					Authority.normal);
		}
		return forward("/bts/");
	}

}
