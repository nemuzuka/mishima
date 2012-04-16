package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.StatusForm;
import jp.co.nemuzuka.service.StatusService;
import jp.co.nemuzuka.service.impl.StatusServiceImpl;

/**
 * ステータス登録・更新情報取得Controller.
 * @author kazumune
 */
public class StatusEditInfoController extends JsonController {

	protected StatusService statusService = new StatusServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		StatusForm form = statusService.get(getUserInfo().selectedProject);
		result.setToken(this.setToken());
		result.setResult(form);

		return result;
	}

}
