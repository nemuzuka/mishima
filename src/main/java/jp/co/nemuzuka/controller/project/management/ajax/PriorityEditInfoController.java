package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.PriorityForm;
import jp.co.nemuzuka.service.PriorityService;
import jp.co.nemuzuka.service.impl.PriorityServiceImpl;

/**
 * 優先度登録・更新情報取得Controller.
 * @author kazumune
 */
public class PriorityEditInfoController extends JsonController {

	protected PriorityService priorityService = new PriorityServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		PriorityForm form = priorityService.get(getUserInfo().selectedProject);
		result.setToken(this.setToken());
		result.setResult(form);

		return result;
	}

}
