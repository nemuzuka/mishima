package jp.co.nemuzuka.controller.project.management.ajax;

import java.util.List;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.ProjectMemberModelEx;
import jp.co.nemuzuka.service.ProjectMemberService;
import jp.co.nemuzuka.service.impl.ProjectMemberServiceImpl;

/**
 * プロジェクトメンバー検索Contoroller.
 * @author kazumune
 */
public class MemberListController extends JsonController {

	protected ProjectMemberService projectMemberService = new ProjectMemberServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<ProjectMemberModelEx> list = projectMemberService.getProjectMemberModelList(getUserInfo().selectedProject);
		result.setResult(list);
		return result;
	}

}
