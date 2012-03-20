package jp.co.nemuzuka.controller.ajax;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;

/**
 * Tokenを返却するController
 * @author kazumune
 */
public class TokenController extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		JsonResult result = new JsonResult();
		result.setToken(super.setToken());
		return result;
	}

}
