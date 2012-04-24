package jp.co.nemuzuka.controller.bts.ticket.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.TicketCommentForm;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.service.impl.TicketServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * Ticketコメント登録Controller.
 * @author kazumune
 */
public class TicketCommentExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected TicketCommentForm form;
	
	protected TicketService ticketService = TicketServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		
		//登録・更新する
		ticketService.putComment(form, getUserInfo().selectedProject, 
				userService.getCurrentUser().getEmail());
		
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
		v.add("status", v.required());
		v.add("comment", v.required(), v.maxlength(1024));
		return v;
	}
}
