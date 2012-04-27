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

import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.service.impl.TodoServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * Todoコメント削除Controller.
 * @author kazumune
 */
public class TodoCommentDeleteController extends JsonController {

	protected TodoService todoService = TodoServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	protected Object execute() throws Exception {
		
		String keyString = asString("keyToString");
		String commentKeyString = asString("commentKeyString");
		Long commentVersionNo = asLong("commentVersionNo");
		
		//削除する
		todoService.deleteComment(keyString, commentKeyString,
				commentVersionNo, userService.getCurrentUser().getEmail());
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}
}
