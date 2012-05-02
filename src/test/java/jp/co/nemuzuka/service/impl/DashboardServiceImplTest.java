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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.ProjectMemberDao;
import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.model.ProjectMemberModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.model.TicketModel;
import jp.co.nemuzuka.model.TodoModel;
import jp.co.nemuzuka.service.DashboardService;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * DashboardServiceImplのテストクラス.
 * @author kazumune
 */
public class DashboardServiceImplTest extends AppEngineTestCase4HRD {

	DashboardServiceImpl service = DashboardServiceImpl.getInstance();
	TicketDao ticketDao = TicketDao.getInstance();
	TodoDao todoDao = TodoDao.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	ProjectMemberDao projectMemberDao = ProjectMemberDao.getInstance();
	List<Key> ticketKeyList = new ArrayList<Key>();
	List<Long> ticketNoList = new ArrayList<Long>();
	List<Key> projectKeyList = new ArrayList<Key>();
	List<Key> todoKeyList = new ArrayList<Key>();

	/**
	 * getDashboardInfoのテスト.
	 * @throws Exception
	 */
	@Test
	public void testGetDashboardInfo() throws Exception {
		createTestData();
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		String[] openStatus = new String[]{"未対応","対応済み"};
		//プロジェクト指定
		DashboardService.Result result = service.getDashboardInfo("hige@hoge.hage", projectKeyString, openStatus, 3);
		assertThat(result.todoList.size(), is(3));
		assertThat(result.todoList.get(0).getModel().getKey(), is(todoKeyList.get(5)));
		assertThat(result.todoList.get(1).getModel().getKey(), is(todoKeyList.get(2)));
		assertThat(result.todoList.get(2).getModel().getKey(), is(todoKeyList.get(0)));
		assertThat(result.ticketList.size(), is(2));
		assertThat(result.ticketList.get(0).getModel().getKey(), is(ticketKeyList.get(5)));
		assertThat(result.ticketList.get(1).getModel().getKey(), is(ticketKeyList.get(2)));
		assertThat(result.projectList, is(nullValue()));
		assertThat(result.viewProjectList, is(false));
		
		//プロジェクト未設定
		result = service.getDashboardInfo("hige@hoge.hage", null, openStatus, 3);
		assertThat(result.todoList.size(), is(3));
		assertThat(result.todoList.get(0).getModel().getKey(), is(todoKeyList.get(5)));
		assertThat(result.todoList.get(1).getModel().getKey(), is(todoKeyList.get(2)));
		assertThat(result.todoList.get(2).getModel().getKey(), is(todoKeyList.get(0)));
		assertThat(result.ticketList, is(nullValue()));
		assertThat(result.projectList.size(), is(1));
		assertThat(result.projectList.get(0).getModel().getKey(), is(projectKeyList.get(0)));
		assertThat(result.viewProjectList, is(true));
	}

	/**
	 * テストデータ作成.
	 * @throws Exception 例外
	 */
	private void createTestData() throws Exception {
		
		ProjectModel projectModel = null;
		for(int i = 0; i < 2; i++) {
			projectModel = new ProjectModel();
			projectModel.setProjectName("プロジェクト" + i);
			projectModel.setProjectId("id" + i);
			projectModel.setProjectSummary(new Text("概要" + i));
			projectDao.put(projectModel);
			projectKeyList.add(projectModel.getKey());
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		MemberModel memberModel = new MemberModel();
		memberModel.createKey("hige@hoge.hage");
		memberModel.setAuthority(Authority.normal);
		memberModel.setMemo(new Text(""));
		memberModel.setName("");
		memberDao.put(memberModel);
		
		ProjectMemberModel projectMemberModel = new ProjectMemberModel();
		projectMemberModel.setProjectKey(projectKeyList.get(0));
		projectMemberModel.setMemberKey(memberModel.getKey());
		projectMemberModel.setProjectAuthority(ProjectAuthority.type1);
		projectMemberDao.put(projectMemberModel);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		TicketModel model = null;
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		for(int i = 0; i < 10; i++) {
			
			model = new TicketModel();
			
			Key projectKey = null;
			if(0 <= i && i <= 5) {
				projectKey = projectKeyList.get(0);
			} else {
				projectKey = projectKeyList.get(1);
			}
			model.setProjectKey(projectKey);
			
			
			String status = null;
			if(0 <= i && i <= 2) {
				status = "未対応";
			} else if(5 <= i && i <= 7) {
				status = "対応済み";
			} else {
				status = "完了";
			}
			model.setStatus(status);

			Date period = null;
			if(4 <= i && i <= 6 ) {
				period = sdf.parse("20120105");
			} else if(i == 2 || (7 <= i && i <= 8)) {
				period = sdf.parse("20120106");
			}
			model.setPeriod(period);

			
			Key memberKey = null;
			if(i == 2 || i == 3 || i == 5 || i == 9) {
				memberKey = Datastore.createKey(MemberModel.class, "hige@hoge.hage");
			}
			model.setTargetMemberKey(memberKey);
			
			if(i == 0 || i == 2) {
				model.setTitle("Like用件名" + i);
			} else {
				model.setTitle("件名1" + i);
			}
			
			model.setContent(new Text("詳細" + i));
			model.setEndCondition(new Text("終了条件" + i));
			
			String priority = "";
			if(i == 0 || i == 3) {
				priority = "高";
			}
			model.setPriority(priority);
			
			String targetKind = "";
			if(i == 2 || i == 4) {
				targetKind = "種別１";
			}
			model.setTargetKind(targetKind);
			
			String category = "";
			if(i == 0 || i == 1) {
				category = "カテゴリ9";
			}
			model.setCategory(category);
			
			Key milestoneKey = null;
			if(i == 1 || i == 2) {
				milestoneKey = Datastore.createKey(MilestoneModel.class, 1L);
			}
			model.setMilestone(milestoneKey);
			
			String targetVersion = "";
			if(i==2 || i==3) {
				targetVersion = "1.0";
			}
			model.setTargetVersion(targetVersion);
			
			ticketDao.put(model);
			ticketKeyList.add(model.getKey());
			ticketNoList.add(model.getNo());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		TodoModel todoModel = null;
		for(int i = 0; i < 10; i++) {
			
			todoModel = new TodoModel();
			
			String targetEmail;
			if(0 <= i && i <= 5) {
				targetEmail = "hige@hoge.hage";
			} else {
				targetEmail = "hoge2@hige.hage";
			}
			
			TodoStatus status = null;
			if(0 <= i && i <= 2) {
				status = TodoStatus.nothing;
			} else if(5 <= i && i <= 8) {
				status = TodoStatus.doing;
			} else {
				status = TodoStatus.finish;
			}
			
			Date period = null;
			if(4 <= i && i <= 6) {
				period = sdf.parse("20120105");
			} else if(i == 2 || (8 <= i && i <= 9)) {
				period = sdf.parse("20120106");
			}
			
			todoModel.setStatus(status);
			Key memberKey = Datastore.createKey(MemberModel.class, targetEmail);
			todoModel.setCreateMemberKey(memberKey);
			
			if(i == 0 || i == 2) {
				model.setTitle("Like用件名" + i);
			} else {
				model.setTitle("件名1" + i);
			}
			
			todoModel.setContent(new Text("詳細" + i));
			todoModel.setPeriod(period);
			todoDao.put(todoModel);
			todoKeyList.add(todoModel.getKey());

			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}

		
		return;
	}

	/* (非 Javadoc)
	 * @see org.slim3.tester.AppEngineTestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		TransactionEntity transactionEntity = new TransactionEntity();
		GlobalTransaction.transaction.set(transactionEntity);
	}
}
