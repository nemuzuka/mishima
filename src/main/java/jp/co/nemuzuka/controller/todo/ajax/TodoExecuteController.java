package jp.co.nemuzuka.controller.todo.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.TodoForm;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.service.impl.TodoServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * TODO登録・更新Controller.
 * @author kazumune
 */
public class TodoExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected TodoForm form;
	
	protected TodoService todoService = TodoServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		
		//登録・更新する
		todoService.put(form, userService.getCurrentUser().getEmail());
		
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
		v.add("todoStatus", v.required());
		v.add("title", v.required(), v.maxlength(128));
		v.add("content", v.maxlength(1024));
		v.add("period", v.dateType("yyyyMMdd"));
		return v;
	}
}
