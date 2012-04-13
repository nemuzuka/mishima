package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.service.impl.VersionServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * バージョン削除Controller.
 * @author kazumune
 */
public class VersionDeleteController extends JsonController {

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
	protected Object execute() throws Exception {
		//削除する
		versionService.delete(form, getUserInfo().selectedProject);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}
}
