package jp.co.nemuzuka.controller.error.noregist;

import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * 未登録ユーザエラーController.
 * 登録してもらう旨通知するエラー画面を表示します。
 * @author kazumune
 */
public class IndexController extends HtmlController {
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	protected Navigation execute() throws Exception {
		return forward("/error/noregist/index.jsp");
	}

}
