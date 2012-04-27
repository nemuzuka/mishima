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
package jp.co.nemuzuka.controller.validator;

import org.slim3.controller.validator.Validator;
import org.slim3.controller.validator.Validators;

/**
 * validatorを生成するUtils.
 * @author k-katagiri
 */
public class ValidatorUtils {

	/**
	 * mailチェック用validator生成.
	 * @param v Validators
	 * @return mailチェック用validator
	 */
	public static Validator getEmailValidator(Validators v) {
		return v.regexp("^([a-zA-Z])+([a-zA-Z0-9\\._-])*@([a-zA-Z])+([0-9a-zA-Z\\._-])+[a-z]+$");
	}
}
