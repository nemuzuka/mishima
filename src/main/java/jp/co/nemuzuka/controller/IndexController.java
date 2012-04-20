package jp.co.nemuzuka.controller;

import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;

import org.slim3.controller.Navigation;

import com.google.appengine.api.users.User;

/**
 * メインController.
 * アプリケーション管理者の場合、MemberModelが登録されているか確認し、
 * 登録されていない場合、Modelをputします。
 * @author kazumune
 */
public class IndexController extends HtmlController {

	private MemberService memberService = MemberServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@NoSessionCheck
	@Override
	protected Navigation execute() throws Exception {
		
		if(userService.isUserAdmin()) {
			//アプリケーション管理者の場合、MemberModelに登録されているかチェック
			User currentUser = userService.getCurrentUser();
			memberService.checkAndCreateAdminMember(
					currentUser.getEmail(),
					currentUser.getNickname());
		}
		return forward("/bts/");
	}

}
