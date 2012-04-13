package jp.co.nemuzuka.controller.project.management.ajax;

import java.util.List;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.model.VersionModel;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.service.impl.VersionServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * バージョン検索Contoroller.
 * @author kazumune
 */
public class VersionListController extends JsonController {

	protected VersionService versionService = new VersionServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<VersionModel> list = versionService.getAllList(getUserInfo().selectedProject);
		result.setResult(list);
		
		if(list.size() == 0) {
			result.getInfoMsg().add(ApplicationMessage.get("info.empty"));
		}
		
		return result;
	}

}
