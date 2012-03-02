package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.model.HogeModel;
import jp.co.nemuzuka.service.HogeService;

/**
 * テスト用のController.
 * データストアに登録する
 * Insertしてトランザクションがコミットしていない時にselectしても、
 * データは取得できない。
 * @author kazumune
 */
public class DataStore2Controller extends JsonController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		JsonResult result = new JsonResult();
		HogeModel model = HogeService.newAndPut("katagiri","kazumune");
		result.setResult(model);
		return result;
	}
}
