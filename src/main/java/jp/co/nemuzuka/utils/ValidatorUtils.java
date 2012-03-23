package jp.co.nemuzuka.utils;

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
