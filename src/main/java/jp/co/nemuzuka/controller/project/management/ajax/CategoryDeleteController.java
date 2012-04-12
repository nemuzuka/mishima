package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.CategoryForm;
import jp.co.nemuzuka.service.CategoryService;
import jp.co.nemuzuka.service.impl.CategoryServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * カテゴリ削除Controller.
 * @author kazumune
 */
public class CategoryDeleteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected CategoryForm form;
	
	protected CategoryService categoryService = new CategoryServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@SystemManager
	protected Object execute() throws Exception {
		//削除する
		categoryService.delete(form, getUserInfo().selectedProject);
		
		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}
}
