package jp.co.nemuzuka.service.impl;

import org.apache.commons.lang.StringUtils;

import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.service.DashboardService;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.utils.ConvertUtils;

/**
 * DashboardServiceの実装クラス.
 * @author k-katagiri
 */
public class DashboardServiceImpl implements DashboardService {
	
	TodoService todoService = TodoServiceImpl.getInstance();
	TicketService ticketService = TicketServiceImpl.getInstance();
	ProjectService projectService = ProjectServiceImpl.getInstance();

	int limit = ConvertUtils.toInteger(System.getProperty("jp.co.nemuzuka.dashboard.list.limit", "10"));

	private static DashboardServiceImpl impl = new DashboardServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static DashboardServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private DashboardServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.DashboardService#getDashboardInfo(java.lang.String, java.lang.String, java.lang.String[])
	 */
	@Override
	public Result getDashboardInfo(String mail, String selectedProject,
			String[] openStatus) {

		Result result = new Result();

		//未完了のTODOを取得する
		TodoDao.Param todoParam = new TodoDao.Param();
		todoParam.email = mail;
		todoParam.limit = limit;
		result.todoList = todoService.getList(todoParam, true);
		
		//プロジェクトが指定されている場合
		if(StringUtils.isNotEmpty(selectedProject)) {
			//プロジェクトに紐付くTicketの一覧を取得する
			TicketDao.Param ticketParam = new TicketDao.Param();
			ticketParam.limit = limit;
			ticketParam.projectKeyString = selectedProject;
			ticketParam.openStatus = openStatus;
			result.ticketList = ticketService.getList(ticketParam, mail, true);
		} else {
			//自分が所属しているプロジェクトの一覧を取得する
			result.projectList = projectService.getUserProjectList(mail);
			result.viewProjectList = true;
		}
		return result;
	}
}
