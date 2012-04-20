package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.service.impl.VersionServiceImpl;

/**
 * バージョン登録・更新情報取得Controller.
 * @author kazumune
 */
public class VersionEditInfoController extends JsonController {

	protected VersionService versionService = VersionServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		VersionForm form = versionService.get(getUserInfo().selectedProject);
		result.setToken(this.setToken());
		result.setResult(form);

		return result;
	}

}
