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

import java.util.List;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.ProjectMemberModelEx;
import jp.co.nemuzuka.service.ProjectMemberService;
import jp.co.nemuzuka.service.impl.ProjectMemberServiceImpl;

/**
 * プロジェクトメンバー検索Contoroller.
 * @author kazumune
 */
public class MemberListController extends JsonController {

	protected ProjectMemberService projectMemberService = ProjectMemberServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@ProjectAdmin
	@Override
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<ProjectMemberModelEx> list = projectMemberService.getProjectMemberModelList(getUserInfo().selectedProject);
		result.setResult(list);
		return result;
	}

}
