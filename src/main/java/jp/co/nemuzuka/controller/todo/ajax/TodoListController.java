package jp.co.nemuzuka.controller.todo.ajax;

import java.util.List;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoSearchForm;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.service.impl.TodoServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * TODO検索Contoroller.
 * @author kazumune
 */
public class TodoListController extends JsonController {

	protected TodoService todoService = new TodoServiceImpl();
	
	/** ActionForm. */
	@ActionForm
	protected TodoSearchForm form;
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		form.status = paramValues("status[]");
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<TodoModelEx> list = todoService.getList(
				form.createParam(userService.getCurrentUser().getEmail()));
		result.setResult(list);
		
		if(list.size() == 0) {
			result.getInfoMsg().add(ApplicationMessage.get("info.empty"));
		}
		return result;
	}
}
