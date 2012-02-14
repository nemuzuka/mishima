package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;

/**
 * テスト用のController.
 * @author kazumune
 */
public class SampleController extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		JsonResult result = new JsonResult();
		return result;
	}
}
