package jp.co.nemuzuka.controller.management;

import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * Memberの検索画面を表示するController.
 * @author kazumune
 */
public class MemberController extends HtmlController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	protected Navigation execute() throws Exception {
		return forward("/management/member.jsp");
	}

}