package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;

import org.slim3.controller.validator.Validators;

/**
 * テスト用のController.
 * TokenとValidate有り
 * @author kazumune
 */
public class TokenValidateController extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		JsonResult result = new JsonResult();
		return result;
	}
	
	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("dummy", v.required(), v.integerType());
		v.add("arg2", v.integerType(), v.longRange(3, 5));
		return v;
	}
}
