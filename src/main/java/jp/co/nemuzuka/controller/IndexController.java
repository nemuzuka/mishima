package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;

import org.slim3.controller.Navigation;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * メインController.
 * アプリケーション管理者の場合、MemberModelが登録されているか確認し、
 * 登録されていない場合、Modelをputします。
 * @author kazumune
 */
public class IndexController extends HtmlController {

	private MemberService memberService = new MemberServiceImpl();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	protected Navigation execute() throws Exception {
		
		UserService service = UserServiceFactory.getUserService();
		if(service.isUserAdmin()) {
			//アプリケーション管理者の場合、MemberModelに登録されているかチェック
			memberService.checkAndCreateAdminMember(service.getCurrentUser().getEmail());
		}
		
		
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

}
