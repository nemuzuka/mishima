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
package jp.co.nemuzuka.dao;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.model.TicketModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * TicketDaoのテストクラス.
 * @author kazumune
 */
public class TicketDaoTest extends AppEngineTestCase4HRD {

	TicketDao ticketDao = TicketDao.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	List<Key> ticketKeyList = new ArrayList<Key>();
	List<Key> projectKeyList = new ArrayList<Key>();
	
	//親子関係のTicket
	Key parentKey = null;
	Key childKey = null;
	List<Key> grandChildKey = new ArrayList<Key>();

	/**
	 * getChildListのテスト.
	 * @throws Exception
	 */
	@Test
	public void testGetChildList() throws Exception {
		createTestData();
		List<Key> actual = ticketDao.getChildList(parentKey, projectKeyList.get(0));
		assertThat(actual.size(), is(1));
		assertThat(actual.get(0), is(childKey));
		
		actual = ticketDao.getChildList(childKey, projectKeyList.get(0));
		assertThat(actual.size(), is(2));
		assertThat(actual.get(0), is(grandChildKey.get(0)));
		assertThat(actual.get(1), is(grandChildKey.get(1)));

		actual = ticketDao.getChildList(grandChildKey.get(0), projectKeyList.get(0));
		assertThat(actual.size(), is(0));
	}
	
	/**
	 * getListのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetList() throws Exception {
		createTestData();
		
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		String[] openStatus = new String[]{"未対応","対応済み"};
		//ステータスの検索条件
		TicketDao.Param param = new TicketDao.Param();
		param.status = new String[]{TicketMst.NO_FINISH};
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		List<TicketModel> actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(1)));
		assertThat(actualList.get(2).getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(3).getKey(), is(ticketKeyList.get(5)));
		
		param = new TicketDao.Param();
		param.status = new String[]{"対応済み"};
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(5)));
		
		//件名の検索実行
		param = new TicketDao.Param();
		param.title = "Like用件名";
		param.status = null;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(2)));
		
		String[] status = new String[0];
		//優先度の検索実行
		param = new TicketDao.Param();
		param.priority = "高";
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(3)));
		
		//種別の検索実行
		param = new TicketDao.Param();
		param.kind = "種別１";
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(4)));
		
		//カテゴリの検索実行
		param = new TicketDao.Param();
		param.category = "カテゴリ9";
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(1)));

		//バージョンの検索実行
		param = new TicketDao.Param();
		param.version = "1.0";
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(3)));
		
		//マイルストーンの検索実行
		param = new TicketDao.Param();
		param.milestone = Datastore.keyToString(Datastore.createKey(MilestoneModel.class, 1L));
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(1)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(2)));
		
		//担当者の検索実行
		param = new TicketDao.Param();
		param.targetMember = Datastore.keyToString(Datastore.createKey(MemberModel.class, "hige@hoge.hage"));
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(3)));
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		//期限日From指定
		param = new TicketDao.Param();
		param.fromPeriod = ConvertUtils.toDate("20120106", sdf);
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));

		param = new TicketDao.Param();
		param.fromPeriod = ConvertUtils.toDate("20120105", sdf);
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(3));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(4)));
		assertThat(actualList.get(2).getKey(), is(ticketKeyList.get(5)));
		
		//期間To指定
		param = new TicketDao.Param();
		param.toPeriod = ConvertUtils.toDate("20120105", sdf);
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(4)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(5)));

		//期間From〜To指定
		param = new TicketDao.Param();
		param.fromPeriod = ConvertUtils.toDate("20120106", sdf);
		param.toPeriod = ConvertUtils.toDate("20120106", sdf);
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));

		//件数指定
		param = new TicketDao.Param();
		param.kind = "種別１";
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		param.limit = 1;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		
		param = new TicketDao.Param();
		param.toPeriod = ConvertUtils.toDate("20120105", sdf);
		param.status = status;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		param.limit = 10;
		actualList = ticketDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(4)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(5)));
		
	}
	
	/**
	 * getDashbordListのテスト
	 * @throws Exception
	 */
	@Test
	public void testGetDashbordList() throws Exception {
		createTestData();
		
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		String[] openStatus = new String[]{"未対応","完了"};

		String targetMemberKeyString = Datastore.keyToString(
				Datastore.createKey(MemberModel.class, "hige@hoge.hage"));
		List<TicketModel> actualList = ticketDao.getDashbordList(10, targetMemberKeyString, projectKeyString, openStatus);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(3)));

		targetMemberKeyString = Datastore.keyToString(
				Datastore.createKey(MemberModel.class, "hige@hoge.hage"));
		actualList = ticketDao.getDashbordList(1, targetMemberKeyString, projectKeyString, openStatus);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		
		targetMemberKeyString = Datastore.keyToString(
				Datastore.createKey(MemberModel.class, "hige@hoge.hage"));
		actualList = ticketDao.getDashbordList(2, targetMemberKeyString, projectKeyString, openStatus);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(3)));
		
		targetMemberKeyString = Datastore.keyToString(
				Datastore.createKey(MemberModel.class, "hige@hoge.hage"));
		projectKeyString = Datastore.keyToString(projectKeyList.get(1));
		actualList = ticketDao.getDashbordList(3, targetMemberKeyString, projectKeyString, openStatus);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(ticketKeyList.get(8)));
		assertThat(actualList.get(1).getKey(), is(ticketKeyList.get(9)));
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
			projectDao.put(projectModel);
			projectKeyList.add(projectModel.getKey());
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
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
			if(i == 2 || i == 3 || i == 8 || i == 9) {
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
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		TicketModel parentModel = new TicketModel();
		parentModel.setProjectKey(projectKeyList.get(0));
		parentModel.setTitle("親Ticket");
		ticketDao.put(parentModel);
		parentKey = parentModel.getKey();
		
		TicketModel childModel = new TicketModel();
		childModel.setProjectKey(projectKeyList.get(0));
		childModel.setTitle("子Ticket");
		childModel.setParentTicketKey(parentKey);
		ticketDao.put(childModel);
		childKey = childModel.getKey();
		
		TicketModel grandChildModel = new TicketModel();
		grandChildModel.setProjectKey(projectKeyList.get(0));
		grandChildModel.setTitle("孫Ticket1");
		grandChildModel.setParentTicketKey(childKey);
		ticketDao.put(grandChildModel);
		grandChildKey.add(grandChildModel.getKey());
		
		grandChildModel = new TicketModel();
		grandChildModel.setProjectKey(projectKeyList.get(0));
		grandChildModel.setTitle("孫Ticket2");
		grandChildModel.setParentTicketKey(childKey);
		ticketDao.put(grandChildModel);
		grandChildKey.add(grandChildModel.getKey());
		
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

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
