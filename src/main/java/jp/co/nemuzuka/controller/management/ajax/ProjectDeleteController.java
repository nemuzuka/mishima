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
package jp.co.nemuzuka.controller.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.ProjectForm;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.impl.ProjectServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * Project削除Controller.
 * @author kazumune
 */
public class ProjectDeleteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected ProjectForm form;
	
	protected ProjectService projectService = ProjectServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@SystemManager
	protected Object execute() throws Exception {
		//削除する
		projectService.delete(form);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}
}
