package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.impl.ProjectServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slim3.controller.Navigation;

/**
 * プロジェクト変更時Controller.
 * UserInfoに選択プロジェクトに関する情報を設定し、
 * TOP画面を表示します。
 * @author kazumune
 */
public class ChangeProjectController extends HtmlController {

	ProjectService service = ProjectServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	protected Navigation execute() throws Exception {
		
		//リクエストからデータを取得
		String projectKey = StringUtils.defaultString(asString("projectKey"), "");
		
		//選択したプロジェクトに対して権限情報を設定
		service.setUserInfo(projectKey, userService.getCurrentUser().getEmail(), getUserInfo());
		return forward("/");
	}
}
