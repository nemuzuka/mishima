/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
	 * @param limitCnt 一覧表示件数
	 * @return ダッシュボード表示情報
	 */
	Result getDashboardInfo(String mail, String selectedProject, String[] openStatus, int limitCnt);
	
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
