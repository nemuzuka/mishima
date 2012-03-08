package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * テスト用のController.
 * JSPをレスポンスに設定するControllerのテスト
 * @author kazumune
 */
public class Submit2Controller extends HtmlController {

	/**
	 * メイン処理.
	 * @return フォワード文字列
	 * @throws Exception 例外
	 */
	@Override
	protected Navigation execute() throws Exception {
		return forward("index.jsp");
	}
}
