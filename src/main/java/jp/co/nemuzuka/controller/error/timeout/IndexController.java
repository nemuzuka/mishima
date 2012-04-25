package jp.co.nemuzuka.controller.error.timeout;

import jp.co.nemuzuka.core.annotation.NoRegistCheck;
import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * SessionタイムアウトエラーController.
 * Sessionタイムアウト画面を表示します。
 * @author kazumune
 */
public class IndexController extends HtmlController {
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@NoSessionCheck
	@NoRegistCheck
	@Override
	protected Navigation execute() throws Exception {
		return forward("/error/timeout/index.jsp");
	}

}
