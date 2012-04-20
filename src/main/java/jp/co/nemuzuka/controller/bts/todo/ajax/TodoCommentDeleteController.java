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
