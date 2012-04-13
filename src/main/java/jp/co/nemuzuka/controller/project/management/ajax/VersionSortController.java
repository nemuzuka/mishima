package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.service.impl.VersionServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * バージョン表示順変更Controller.
 * @author kazumune
 */
public class VersionSortController extends JsonController {

	private String[] sortedVersionKeys;
	
	protected VersionService versionService = new VersionServiceImpl();
	
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
		versionService.updateSortNum(sortedVersionKeys, getUserInfo().selectedProject);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * 自前validate.
	 * @return エラーが存在する場合、JsonResultのインスタンス。エラーが存在しない場合、null
	 */
	private JsonResult validate() {
		
		sortedVersionKeys = paramValues("sortedVersionKeys[]");
		
		//カテゴリは1つ以上選択すること
		if(sortedVersionKeys == null || sortedVersionKeys.length == 0) {
			return createErrorMsg("validator.check.required", "バージョン");
		}
		return null;
	}
}
