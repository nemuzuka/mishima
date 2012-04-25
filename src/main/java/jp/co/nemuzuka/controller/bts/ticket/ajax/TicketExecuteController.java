package jp.co.nemuzuka.controller.bts.ticket.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.exception.NotExistTicketException;
import jp.co.nemuzuka.form.TicketForm;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.service.impl.TicketServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * Ticket登録・更新Controller.
 * @author kazumune
 */
public class TicketExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected TicketForm form;
	
	protected TicketService ticketService = TicketServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectMember
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		
		JsonResult result = null;
		//登録・更新する
		try {
			ticketService.put(form, getUserInfo().selectedProject);
		} catch(NotExistTicketException e) {
			//リクエストパラメータに紐付く親Keyが存在しない場合、エラーとして返却
			result = new JsonResult();
			result.setStatus(JsonResult.STATUS_NG);
			result.getErrorMsg().add(ApplicationMessage.get("errors.not.exist.ticket"));
		}
		if(result == null) {
			result = new JsonResult();
			result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		}
		return result;
	}

	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("status", v.required(), v.maxlength(128));
		v.add("title", v.required(), v.maxlength(128));
		v.add("content", v.maxlength(1024));
		v.add("endCondition", v.maxlength(1024));
		v.add("period", v.dateType("yyyyMMdd"));
		v.add("priority", v.maxlength(128));
		v.add("targetKind", v.maxlength(128));
		v.add("category", v.maxlength(128));
		v.add("targetVersion", v.maxlength(128));
		v.add("parentKey", v.longType());
		return v;
	}
}
