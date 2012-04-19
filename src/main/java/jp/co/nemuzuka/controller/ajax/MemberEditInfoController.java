package jp.co.nemuzuka.controller.ajax;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.PersonForm;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;

/**
 * 個人設定ダイアログ情報取得Controller.
 * @author kazumune
 */
public class MemberEditInfoController extends JsonController {

	protected MemberService memberService = new MemberServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		PersonForm form = memberService.getPersonForm(userService.getCurrentUser().getEmail());

		result.setToken(this.setToken());
		result.setResult(form);
		return result;
	}

}
