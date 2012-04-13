package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.KindForm;
import jp.co.nemuzuka.service.KindService;
import jp.co.nemuzuka.service.impl.KindServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * 種別削除Controller.
 * @author kazumune
 */
public class KindDeleteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected KindForm form;
	
	protected KindService kindService = new KindServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		//削除する
		kindService.delete(form, getUserInfo().selectedProject);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}
}
