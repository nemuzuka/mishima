package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.MilestoneForm;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.MilestoneServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * マイルストーン削除Controller.
 * @author kazumune
 */
public class MilestoneDeleteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected MilestoneForm form;
	
	protected MilestoneService milestoneService = MilestoneServiceImpl.getInstance();
	protected TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		//削除する
		milestoneService.delete(form, getUserInfo().selectedProject);
		ticketMstService.initRefreshStartTime(getUserInfo().selectedProject);

		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}
}
