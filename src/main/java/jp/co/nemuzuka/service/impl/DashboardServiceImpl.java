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
package jp.co.nemuzuka.service.impl;

import org.apache.commons.lang.StringUtils;

import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.service.DashboardService;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.service.TodoService;

/**
 * DashboardServiceの実装クラス.
 * @author k-katagiri
 */
public class DashboardServiceImpl implements DashboardService {
	
	TodoService todoService = TodoServiceImpl.getInstance();
	TicketService ticketService = TicketServiceImpl.getInstance();
	ProjectService projectService = ProjectServiceImpl.getInstance();
	MemberService memberService = MemberServiceImpl.getInstance();

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
	 * @see jp.co.nemuzuka.service.DashboardService#getDashboardInfo(java.lang.String, java.lang.String, java.lang.String[], int)
	 */
	@Override
	public Result getDashboardInfo(String mail, String selectedProject,
			String[] openStatus, int limitCnt) {

		Result result = new Result();

		//未完了のTODOを取得する
		TodoDao.Param todoParam = new TodoDao.Param();
		String targetMemberKey = memberService.getKeyString(mail);
		todoParam.targetMemberKeyString = targetMemberKey;
		todoParam.limit = limitCnt;
		result.todoList = todoService.getList(todoParam, true);
		
		//プロジェクトが指定されている場合
		if(StringUtils.isNotEmpty(selectedProject)) {
			//プロジェクトに紐付くTicketの一覧を取得する
			TicketDao.Param ticketParam = new TicketDao.Param();
			ticketParam.limit = limitCnt;
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
