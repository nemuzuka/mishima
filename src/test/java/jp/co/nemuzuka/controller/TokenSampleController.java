package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;

/**
 * テスト用のController.
 * ※Token用
 * @author kazumune
 */
public class TokenSampleController extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	protected Object execute() throws Exception {
		JsonResult result = new JsonResult();
		return result;
	}
}
