package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.MilestoneForm;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.service.impl.MilestoneServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slim3.util.ApplicationMessage;

/**
 * マイルストーン登録・更新ダイアログ情報取得Controller.
 * @author kazumune
 */
public class MilestoneEditInfoController extends JsonController {

	protected MilestoneService milestoneService = new MilestoneServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		String keyToString = asString("keyToString");
		MilestoneForm form = milestoneService.get(keyToString);
		
		if(StringUtils.isNotEmpty(keyToString) && StringUtils.isEmpty(form.keyToString)) {
			//リクエストパラメータに紐付くデータが存在しない場合、エラーとして返却
			result.setStatus(JsonResult.NO_DATA);
			result.getErrorMsg().add(ApplicationMessage.get("info.empty"));
		} else {
			result.setToken(this.setToken());
			result.setResult(form);
		}
		return result;
	}

}
