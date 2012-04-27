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
package jp.co.nemuzuka.controller.bts.todo.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.TodoCommentForm;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.service.impl.TodoServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * TODOコメント登録Controller.
 * @author kazumune
 */
public class TodoCommentExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected TodoCommentForm form;
	
	protected TodoService todoService = TodoServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		
		//登録・更新する
		todoService.putComment(form, userService.getCurrentUser().getEmail());
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("status", v.required());
		v.add("comment", v.required(), v.maxlength(1024));
		return v;
	}
}
