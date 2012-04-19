package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;

/**
 * 各種初期値情報取得Controller.
 * @author kazumune
 */
public class InitInfoController extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		String type = asString("type");
		JsonResult result = new JsonResult();
		//Form情報を取得
		String data = System.getProperty("jp.co.nemuzuka.default." + type, "");
		result.setResult(data);
		return result;
	}

}
