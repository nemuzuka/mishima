package jp.co.nemuzuka.controller.bts.ticket.ajax;

import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.service.impl.TicketServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * Ticketコメント削除Controller.
 * @author kazumune
 */
public class TicketCommentDeleteController extends JsonController {

	protected TicketService ticketService = TicketServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	protected Object execute() throws Exception {
		
		String keyString = asString("keyToString");
		String commentKeyString = asString("commentKeyString");
		Long commentVersionNo = asLong("commentVersionNo");
		
		//削除する
		ticketService.deleteComment(keyString, commentKeyString,
				commentVersionNo, getUserInfo().selectedProject);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}
}
