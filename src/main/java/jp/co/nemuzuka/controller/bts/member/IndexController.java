package jp.co.nemuzuka.controller.bts.member;

import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * プロジェクトメンバーメインController.
 * @author kazumune
 */
public class IndexController extends HtmlController {
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@ProjectMember
	@Override
	protected Navigation execute() throws Exception {
		return forward("/bts/member/member.jsp");
	}

}