package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.entity.ProjectModelEx;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.entity.TodoModelEx;

/**
 * ダッシュボードに表示する情報のService.
 * @author k-katagiri
 */
public interface DashboardService {

	/**
	 * ダッシュボード情報取得.
	 * ログインユーザに紐付くダッシュボード情報を取得します。
	 * @param mail ログインユーザのメールアドレス
	 * @param selectedProject 現在選択しているプロジェクト
	 * @param openStatus 現在選択しているプロジェクトにおける、「未完了」を意味するステータス
	 * @return ダッシュボード表示情報
	 */
	Result getDashboardInfo(String mail, String selectedProject, String[] openStatus);
	
	/**
	 * ダッシュボード表示情報.
	 * @author k-katagiri
	 */
	class Result {
		/** 未完了のTODOList. */
		public List<TodoModelEx> todoList;
		
		/** 未完了の自分が担当者となるTicketList */
		public List<TicketModelEx> ticketList;
		
		/** 自分が担当になっているプロジェクトList */
		public List<ProjectModelEx> projectList;
		
		/** プロジェクトListを表示する場合、true. */
		public boolean viewProjectList = false;
	}
}
