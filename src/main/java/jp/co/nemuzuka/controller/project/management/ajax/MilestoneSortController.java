package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.MilestoneServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * マイルストーン表示順変更Controller.
 * @author kazumune
 */
public class MilestoneSortController extends JsonController {

	private String[] sortedMilestoneKeys;
	
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
		
		JsonResult errorResult = validate();
		if(errorResult != null) {
			return errorResult;
		}
		
		//登録・更新する
		milestoneService.updateSortNum(sortedMilestoneKeys, getUserInfo().selectedProject);
		ticketMstService.initRefreshStartTime(getUserInfo().selectedProject);

		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * 自前validate.
	 * @return エラーが存在する場合、JsonResultのインスタンス。エラーが存在しない場合、null
	 */
	private JsonResult validate() {
		
		sortedMilestoneKeys = paramValues("sortedMilestoneKeys[]");
		
		//マイルストーンは1つ以上選択すること
		if(sortedMilestoneKeys == null || sortedMilestoneKeys.length == 0) {
			return createErrorMsg("validator.check.required", "マイルストーン");
		}
		return null;
	}
}
