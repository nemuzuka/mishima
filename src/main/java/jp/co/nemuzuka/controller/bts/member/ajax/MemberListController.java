package jp.co.nemuzuka.controller.bts.member.ajax;

import java.util.List;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.ProjectMemberModelEx;
import jp.co.nemuzuka.service.ProjectMemberService;
import jp.co.nemuzuka.service.impl.ProjectMemberServiceImpl;

/**
 * プロジェクトメンバー一覧Contoroller.
 * @author kazumune
 */
public class MemberListController extends JsonController {

	protected ProjectMemberService projectMemberService = ProjectMemberServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<ProjectMemberModelEx> list = projectMemberService.getProjectMemberOnlyModelList(
				getUserInfo().selectedProject);
		result.setResult(list);
		return result;
	}
}
