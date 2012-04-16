package jp.co.nemuzuka.controller.project.management;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * バージョン管理メインController.
 * @author kazumune
 */
public class VersionController extends HtmlController {
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Navigation execute() throws Exception {
		return forward("/project/management/version.jsp");
	}

}