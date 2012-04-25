package jp.co.nemuzuka.controller.bts.ticket.ajax;

import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.form.TicketSearchForm;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

/**
 * Ticket検索条件取得Controller.
 * @author kazumune
 */
public class TicketSearchInfoController extends JsonController {

	TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		TicketSearchForm form = new TicketSearchForm();
		form.status = new String[]{TicketMst.NO_FINISH};
		form.ticketMst = ticketMstService.getTicketMst(getUserInfo().selectedProject);
		result.setResult(form);
		return result;
	}

}
