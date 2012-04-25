package jp.co.nemuzuka.controller.bts.dashboard.ajax;

import org.apache.commons.lang.StringUtils;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.service.DashboardService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.DashboardServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

/**
 * ダッシュボード情報取得Contoroller.
 * @author kazumune
 */
public class DashboardListController extends JsonController {

	protected DashboardService dashboardService = DashboardServiceImpl.getInstance();
	protected TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		String selectedProject = getUserInfo().selectedProject;
		String[] openStatus = new String[0];
		if(StringUtils.isNotEmpty(selectedProject)) {
			openStatus = ticketMstService.getTicketMst(selectedProject).openStatus;
		}
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		result.setResult(
				dashboardService.getDashboardInfo(userService.getCurrentUser().getEmail(), 
						selectedProject, openStatus));
		return result;
	}
}
