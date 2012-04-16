package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.StatusForm;
import jp.co.nemuzuka.service.StatusService;
import jp.co.nemuzuka.service.impl.StatusServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * バージョン登録・更新Controller.
 * @author kazumune
 */
public class StatusExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected StatusForm form;
	
	protected StatusService statusService = new StatusServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	@ProjectMember
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		//登録・更新する
		statusService.put(form, getUserInfo().selectedProject);
		
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
		v.add("statusName", v.required(), v.maxlength(1024));
		v.add("closeStatusName", v.maxlength(1024));
		return v;
	}
}