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

import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.impl.ProjectServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slim3.controller.Navigation;

/**
 * プロジェクト変更時Controller.
 * UserInfoに選択プロジェクトに関する情報を設定し、
 * TOP画面を表示します。
 * @author kazumune
 */
public class ChangeProjectController extends HtmlController {

	ProjectService service = ProjectServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	protected Navigation execute() throws Exception {
		
		//リクエストからデータを取得
		String projectKey = StringUtils.defaultString(asString("projectKey"), "");
		
		//選択したプロジェクトに対して権限情報を設定
		service.setUserInfo(projectKey, userService.getCurrentUser().getEmail(), getUserInfo());
		return forward("/");
	}
}
