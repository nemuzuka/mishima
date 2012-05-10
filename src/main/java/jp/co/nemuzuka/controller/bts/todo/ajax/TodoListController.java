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

import java.util.List;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoSearchForm;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;
import jp.co.nemuzuka.service.impl.TodoServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * TODO検索Contoroller.
 * @author kazumune
 */
public class TodoListController extends JsonController {

	protected TodoService todoService = TodoServiceImpl.getInstance();
	protected MemberService memberService = MemberServiceImpl.getInstance();
	
	/** ActionForm. */
	@ActionForm
	protected TodoSearchForm form;
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		
		form.status = paramValues("status[]");
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<TodoModelEx> list = todoService.getList(
				form.createParam(userService.getCurrentUser().getEmail(), memberService), false);
		result.setResult(list);
		
		if(list.size() == 0) {
			result.getInfoMsg().add(ApplicationMessage.get("info.empty"));
		}
		return result;
	}
	
	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("fromPeriod", v.dateType("yyyyMMdd"));
		v.add("toPeriod", v.dateType("yyyyMMdd"));
		return v;
	}
}
