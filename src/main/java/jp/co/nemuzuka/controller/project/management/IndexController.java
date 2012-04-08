package jp.co.nemuzuka.controller.project.management;

import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * プロジェクト管理機能メインController.
 * @author kazumune
 */
public class IndexController extends HtmlController {
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@ProjectAdmin
	@Override
	protected Navigation execute() throws Exception {
		
		//TODO プロジェクトが設定されていない or プロジェクト管理者でない場合、Exceptionをthrowする
		//プロジェクト情報をクリアして、TOPに強制的に遷移
		
		return forward("/project/management/index.jsp");
	}

}
