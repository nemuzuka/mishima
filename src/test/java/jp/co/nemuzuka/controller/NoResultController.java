package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.controller.JsonController;

/**
 * テスト用のController.
 * @author kazumune
 */
public class NoResultController extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@NoSessionCheck
	@Override
	protected Object execute() throws Exception {
		return null;
	}
}
