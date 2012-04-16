package jp.co.nemuzuka.controller.project.management.ajax;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.CategoryForm;
import jp.co.nemuzuka.service.CategoryService;
import jp.co.nemuzuka.service.impl.CategoryServiceImpl;

/**
 * カテゴリ登録・更新ダイアログ情報取得Controller.
 * @author kazumune
 */
public class CategoryEditInfoController extends JsonController {

	protected CategoryService categoryService = new CategoryServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectAdmin
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		//Form情報を取得
		CategoryForm form = categoryService.get(getUserInfo().selectedProject);
		result.setToken(this.setToken());
		result.setResult(form);

		return result;
	}

}
