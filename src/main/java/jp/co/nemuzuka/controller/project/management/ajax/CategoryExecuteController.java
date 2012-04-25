package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.controller.validator.MultiDataValidator;
import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.CategoryForm;
import jp.co.nemuzuka.service.CategoryService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.CategoryServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * カテゴリ登録・更新Controller.
 * @author kazumune
 */
public class CategoryExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected CategoryForm form;
	
	protected CategoryService categoryService = CategoryServiceImpl.getInstance();
	protected TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	@ProjectMember
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		//登録・更新する
		categoryService.put(form, getUserInfo().selectedProject);
		ticketMstService.initRefreshStartTime(getUserInfo().selectedProject);

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
		v.add("categoryName", v.required(), v.maxlength(1024), new MultiDataValidator(128));
		return v;
	}
}
