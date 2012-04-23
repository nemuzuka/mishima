package jp.co.nemuzuka.controller.management.ajax;

import jp.co.nemuzuka.controller.validator.ValidatorUtils;
import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * Member登録・更新Controller.
 * @author kazumune
 */
public class MemberExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected MemberForm form;
	
	protected MemberService memberService = MemberServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@SystemManager
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		//登録・更新する
		memberService.put(form);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("name", v.required(), v.maxlength(64));
		v.add("mail", v.required(), ValidatorUtils.getEmailValidator(v), v.maxlength(256));
		v.add("authority", v.required());
		return v;
	}
}
