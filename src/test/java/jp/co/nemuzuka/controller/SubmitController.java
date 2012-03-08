package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.form.TestForm;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

/**
 * テスト用のController.
 * JSPをレスポンスに設定するControllerのテスト
 * @author kazumune
 */
public class SubmitController extends HtmlController {

	/** ActionForm. */
	@ActionForm
	protected TestForm testForm;
	
	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("dummy", v.required());
		return v;
	}
	
	/**
	 * メイン処理.
	 * @return フォワード文字列
	 * @throws Exception 例外
	 */
	@Validation(method="validate",input="error")
	@Override
	protected Navigation execute() throws Exception {
		return forward("index.jsp");
	}
	
	/**
	 * エラー時処理.
	 * @return フォワード文字列
	 */
	protected Navigation error() {
		return forward("error.jsp");
	}
}
