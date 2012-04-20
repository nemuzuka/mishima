package jp.co.nemuzuka.controller.bts.todo.ajax;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.TodoForm;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.service.impl.TodoServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slim3.util.ApplicationMessage;

/**
 * TODO登録・更新ダイアログ情報取得Controller.
 * @author kazumune
 */
public class TodoEditInfoController extends JsonController {

	protected TodoService todoService = TodoServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		String keyToString = asString("keyToString");
		TodoForm form = todoService.get(keyToString, userService.getCurrentUser().getEmail());
		
		if(StringUtils.isNotEmpty(keyToString) && StringUtils.isEmpty(form.keyToString)) {
			//リクエストパラメータに紐付くデータが存在しない場合、エラーとして返却
			result.setStatus(JsonResult.NO_DATA);
			result.getErrorMsg().add(ApplicationMessage.get("info.empty"));
		} else {
			result.setToken(this.setToken());
			result.setResult(form);
		}
		return result;
	}

}
