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

import java.io.Serializable;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.TestForm;
import jp.co.nemuzuka.service.Slim3Service;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * テスト用のController.
 * データストアに登録する
 * @author kazumune
 */
public class DataStoreController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected TestForm testForm;
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@NoSessionCheck
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		JsonResult result = new JsonResult();
		Slim3Service.newAndPut(asString("dummy"));
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		if(asString("dummy").equals("hoge")) {
			throw new RuntimeException("error!!");
		}
		Detail detail = new Detail();
		detail.setHoge("終了でーす。");
		result.setResult(detail);
		return result;
	}
	
	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("dummy", v.required());
		return v;
	}
	
	class Detail implements Serializable {
		/**
		 * serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;
		private String hoge;
		/**
		 * @return hoge
		 */
		public String getHoge() {
			return hoge;
		}
		/**
		 * @param hoge セットする hoge
		 */
		public void setHoge(String hoge) {
			this.hoge = hoge;
		}
	}
}
