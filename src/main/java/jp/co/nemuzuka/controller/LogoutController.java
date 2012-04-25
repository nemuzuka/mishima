package jp.co.nemuzuka.controller;

import javax.servlet.http.HttpSession;

import jp.co.nemuzuka.core.annotation.NoRegistCheck;
import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * ログアウトController.
 * @author kazumune
 */
public class LogoutController extends HtmlController {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@NoRegistCheck
	@NoSessionCheck
	@Override
	protected Navigation execute() throws Exception {
		HttpSession session = request.getSession(false);
		if(session != null) {
			session.invalidate();
		}
		
		String requestURL = request.getRequestURL().toString();
		String requestURI = request.getRequestURI();
		String path = requestURL.replaceAll(requestURI, "/");
		path = userService.createLogoutURL(path);
		return redirect(path);
	}

}
