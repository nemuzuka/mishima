package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.service.impl.VersionServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * バージョン登録・更新Controller.
 * @author kazumune
 */
public class VersionExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected VersionForm form;
	
	protected VersionService versionService = new VersionServiceImpl();
	
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
		versionService.put(form, getUserInfo().selectedProject);
		
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
		v.add("versionName", v.required(), v.maxlength(1024));
		return v;
	}
}
