package jp.co.nemuzuka.controller.bts.ticket;

import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * TicketメインController.
 * @author kazumune
 */
public class IndexController extends HtmlController {
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@SystemManager
	@Override
	protected Navigation execute() throws Exception {
		return forward("/bts/ticket/ticket.jsp");
	}

}
