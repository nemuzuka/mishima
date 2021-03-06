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
package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.ProjectMemberForm;
import jp.co.nemuzuka.service.ProjectMemberService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.ProjectMemberServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * ProjectMember設定Controller.
 * @author kazumune
 */
public class MemberSettingController extends JsonController {

	/** ActionForm. */
	//本メソッド内でインスタンス生成.
	protected ProjectMemberForm form;
	
	protected ProjectMemberService projectMemberService = ProjectMemberServiceImpl.getInstance();
	protected TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	protected Object execute() throws Exception {
		
		JsonResult result = validate();
		if(result != null) {
			return result;
		}
		
		//登録・更新する
		projectMemberService.updateProjectMember(getUserInfo().selectedProject, form);
		//キャッシュの初期化
		ticketMstService.initRefreshStartTime(getUserInfo().selectedProject);
		
		result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * 自前validate.
	 * ActionFormにデータを設定して、入力チェックを行います。
	 * ajaxで配列パラメータを受ける際の処理がslim3の通常の仕組みでは想定通りの振る舞いをしないので
	 * 個別対応をしています。
	 * @return エラーが存在する場合、JsonResultのインスタンス。エラーが存在しない場合、null
	 */
	private JsonResult validate() {
		
		form = new ProjectMemberForm();
		form.memberKeyArray = paramValues("memberKeyArray[]");
		form.authorityCodeArray = paramValues("authorityCodeArray[]");
		
		//チェックボックスは1つ以上選択すること
		if(form.memberKeyArray == null || form.memberKeyArray.length == 0 || 
				form.authorityCodeArray == null || form.authorityCodeArray.length == 0) {
			return createErrorMsg("validator.check.required", "プロジェクトメンバー");
		}
		
		//サイズが合致しないのは不正
		if(form.memberKeyArray.length != form.authorityCodeArray.length) {
			return createErrorMsg("validator.error.size", "プロジェクトメンバー", "権限");
		}
		
		//管理者が1件も存在しないのは、不正
		boolean admin = false;
		for(String target : form.authorityCodeArray) {
			if(ProjectAuthority.type1.getCode().equals(target)) {
				admin = true;
				break;
			}
		}
		if(admin == false) {
			return createErrorMsg("validator.check.required", "プロジェクト管理者");
		}
		return null;
	}
}
