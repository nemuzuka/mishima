package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.service.KindService;
import jp.co.nemuzuka.service.impl.KindServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * 種別表示順変更Controller.
 * @author kazumune
 */
public class KindSortController extends JsonController {

	private String[] sortedKindKeys;
	
	protected KindService kindService = new KindServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult errorResult = validate();
		if(errorResult != null) {
			return errorResult;
		}
		
		//登録・更新する
		kindService.updateSortNum(sortedKindKeys, getUserInfo().selectedProject);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * 自前validate.
	 * @return エラーが存在する場合、JsonResultのインスタンス。エラーが存在しない場合、null
	 */
	private JsonResult validate() {
		
		sortedKindKeys = paramValues("sortedKindKeys[]");
		
		//種別は1つ以上選択すること
		if(sortedKindKeys == null || sortedKindKeys.length == 0) {
			return createErrorMsg("validator.check.required", "種別");
		}
		return null;
	}
}
