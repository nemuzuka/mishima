package jp.co.nemuzuka.controller.management;

import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * 管理者メインController.
 * @author kazumune
 */
public class IndexController extends HtmlController {
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@SystemManager
	@Override
	protected Navigation execute() throws Exception {
		getUserInfo().initProjectInfo();
		return forward("/management/index.jsp");
	}

}
