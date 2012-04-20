package jp.co.nemuzuka.controller.bts.todo.ajax;

import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.TodoSearchForm;

/**
 * TODO検索条件取得Controller.
 * @author kazumune
 */
public class TodoSearchInfoController extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		TodoSearchForm form = new TodoSearchForm();
		form.status = new String[]{TodoStatus.NO_FINISH};
		result.setResult(form);
		return result;
	}

}
