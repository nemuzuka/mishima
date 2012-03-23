package jp.co.nemuzuka.controller.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;

/**
 * Member削除Controller.
 * @author kazumune
 */
public class MemberDeleteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected MemberForm form;
	
	protected MemberService memberService = new MemberServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	protected Object execute() throws Exception {
		//削除する
		memberService.delete(form);
		
		JsonResult result = new JsonResult();
		return result;
	}
}
