package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.KindForm;
import jp.co.nemuzuka.service.KindService;
import jp.co.nemuzuka.service.impl.KindServiceImpl;

/**
 * 種別登録・更新ダイアログ情報取得Controller.
 * @author kazumune
 */
public class KindEditInfoController extends JsonController {

	protected KindService kindService = new KindServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		KindForm form = kindService.get(getUserInfo().selectedProject);
		result.setToken(this.setToken());
		result.setResult(form);
		return result;
	}

}
