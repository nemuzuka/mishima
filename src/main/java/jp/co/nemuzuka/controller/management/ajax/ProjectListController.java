package jp.co.nemuzuka.controller.management.ajax;

import java.util.List;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.ProjectModelEx;
import jp.co.nemuzuka.form.ProjectSearchForm;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.impl.ProjectServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * プロジェクト検索Contoroller.
 * @author kazumune
 */
public class ProjectListController extends JsonController {

	protected ProjectService projectService = ProjectServiceImpl.getInstance();
	
	/** ActionForm. */
	@ActionForm
	protected ProjectSearchForm form;
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@SystemManager
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<ProjectModelEx> list = projectService.getList(form.projectName);
		result.setResult(list);
		
		if(list.size() == 0) {
			result.getInfoMsg().add(ApplicationMessage.get("info.empty"));
		}
		
		return result;
	}

}
