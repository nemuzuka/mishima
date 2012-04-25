package jp.co.nemuzuka.controller.bts.todo;

import jp.co.nemuzuka.core.controller.HtmlController;

import org.slim3.controller.Navigation;

/**
 * TODOメインController.
 * @author kazumune
 */
public class IndexController extends HtmlController {
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@Override
	protected Navigation execute() throws Exception {
		return forward("/bts/todo/todo.jsp");
	}

}