package jp.co.nemuzuka.controller.management;

import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * Projectの検索画面を表示するController.
 * @author kazumune
 */
public class ProjectController extends HtmlController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@SystemManager
	@Override
	protected Navigation execute() throws Exception {
		getUserInfo().initProjectInfo();
		return forward("/management/project.jsp");
	}

}
