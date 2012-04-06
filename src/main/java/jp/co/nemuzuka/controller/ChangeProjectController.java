package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * プロジェクト変更時Controller.
 * UserInfoに選択プロジェクトに関する情報を設定し、
 * TOP画面を表示します。
 * @author kazumune
 */
public class ChangeProjectController extends HtmlController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	protected Navigation execute() throws Exception {
		
		//リクエストからデータを取得
//		String projectKey = asString("projectKey");
		return forward("index.jsp");
	}

}
