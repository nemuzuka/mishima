/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.NoSessionCheck;
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
	@NoSessionCheck
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
