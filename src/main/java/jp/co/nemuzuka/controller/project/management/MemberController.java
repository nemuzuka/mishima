package jp.co.nemuzuka.controller.project.management;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * プロジェクトメンバー管理メインController.
 * @author kazumune
 */
public class MemberController extends HtmlController {
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@ProjectAdmin
	@Override
	protected Navigation execute() throws Exception {
		return forward("/project/management/member.jsp");
	}

}
