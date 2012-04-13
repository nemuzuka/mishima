package jp.co.nemuzuka.controller.project.management.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.MilestoneForm;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.service.impl.MilestoneServiceImpl;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * マイルストーン登録・更新Controller.
 * @author kazumune
 */
public class MilestoneExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected MilestoneForm form;
	
	protected MilestoneService milestoneService = new MilestoneServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	@ProjectMember
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		
		JsonResult errorResult = validator();
		if(errorResult != null) {
			return errorResult;
		}

		
		//登録・更新する
		milestoneService.put(form, getUserInfo().selectedProject);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * 相関チェック.
	 * @return エラーが存在する場合、JsonResultのインスタンス。エラーが存在しない場合、null
	 */
	private JsonResult validator() {
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		Date startDate = ConvertUtils.toDate(form.startDate, sdf);
		Date endDate = ConvertUtils.toDate(form.endDate, sdf);
		if(startDate != null && endDate != null) {
			long startTime = startDate.getTime();
			long endTime = endDate.getTime();
			if(startTime > endTime) {
				return createErrorMsg("validator.check.between", "終了日","開始日","日付");
			}
		}
		return null;
	}

	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("milestoneName", v.required(), v.maxlength(128));
		v.add("startDate", v.dateType("yyyyMMdd"));
		v.add("endDate", v.dateType("yyyyMMdd"));
		return v;
	}
}
