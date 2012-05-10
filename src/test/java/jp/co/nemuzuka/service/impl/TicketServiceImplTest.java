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
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import jp.co.nemuzuka.common.PeriodStatus;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.exception.NotExistTicketException;
import jp.co.nemuzuka.exception.ParentSelfTicketException;
import jp.co.nemuzuka.form.TicketCommentForm;
import jp.co.nemuzuka.form.TicketDetailForm;
import jp.co.nemuzuka.form.TicketForm;
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
 * TicketServiceImplのテストクラス.
 * @author kazumune
 */
public class TicketServiceImplTest extends AppEngineTestCase4HRD {

	TicketServiceImpl service = TicketServiceImpl.getInstance();
	TicketDao ticketDao = TicketDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	List<Key> ticketKeyList = new ArrayList<Key>();
	List<Long> ticketNoList = new ArrayList<Long>();
	List<Key> projectKeyList = new ArrayList<Key>();
	
	//親子関係のTicket
	Key parentKey = null;
	Key childKey = null;
	List<Key> grandChildKey = new ArrayList<Key>();

	Long parentNo = null;
	Long childNo = null;
	List<Long> grandChildNo = new ArrayList<Long>();

	/**
	 * putCommentのテスト.
	 * @throws Exception
	 */
	@Test
	public void testPutComment() throws Exception {
		createTestData();
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		TicketForm target = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);
		
		TicketCommentForm form = new TicketCommentForm();
		form.keyToString = target.keyToString;
		form.comment = "コメントだったり刷る";
		form.status = "未対応";
		form.versionNo = target.versionNo;
		service.putComment(form, projectKeyString, "hige@hoge.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		TicketDetailForm actual =  service.getDetail(Datastore.keyToString(ticketKeyList.get(0)), 
				projectKeyString);
		assertThat(actual.form.versionNo, is(form.versionNo));
		assertThat(actual.form.status, is("未対応"));
		assertThat(actual.commentList.size(), is(1));
		
		//ステータスも変更
		target = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);
		form = new TicketCommentForm();
		form.keyToString = target.keyToString;
		form.comment = "2軒目の登録";
		form.status = "きゃんせる";
		form.versionNo = target.versionNo;
		service.putComment(form, projectKeyString, "hige@hoge.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		actual =  service.getDetail(Datastore.keyToString(ticketKeyList.get(0)), 
				projectKeyString);
		assertThat(actual.form.status, is("きゃんせる"));
		assertThat(actual.commentList.size(), is(2));
		
		//そもそもコメントを付与するデータが存在しない
		try {
			service.putComment(form, Datastore.keyToString(projectKeyList.get(1)), "hige@hoge.hage");
			fail();
		} catch(ConcurrentModificationException e) {}
		
		//他のユーザによって更新された可能性がある
		target = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);
		form = new TicketCommentForm();
		form.keyToString = target.keyToString;
		form.comment = "3軒目の登録";
		form.status = "完了";
		form.versionNo = "-1";
		try {
			service.putComment(form, projectKeyString, "hige@hoge.hage");
			fail();
		} catch(ConcurrentModificationException e) {}
		
		
		//コメント削除
		actual =  service.getDetail(Datastore.keyToString(ticketKeyList.get(0)), 
				projectKeyString);
		service.deleteComment(actual.form.keyToString, 
				actual.commentList.get(0).getModel().getKeyToString(), 
				actual.commentList.get(0).getModel().getVersion(), 
				projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		actual =  service.getDetail(Datastore.keyToString(ticketKeyList.get(0)), 
				projectKeyString);
		assertThat(actual.commentList.size(), is(1));
		
		//コメントの親が存在しない場合
		try {
			service.deleteComment(actual.form.keyToString, 
					actual.commentList.get(0).getModel().getKeyToString(), 
					actual.commentList.get(0).getModel().getVersion(), 
					Datastore.keyToString(projectKeyList.get(1)));
		} catch(ConcurrentModificationException e){}
		
	}
	
	
	/**
	 * updateTicketStatusのテスト.
	 * @throws Exception
	 */
	@Test
	public void testUpdateTicketStatus() throws Exception {
		createTestData();

		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		TicketForm form = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);
		form.status = "かえちまったぜ！";
		service.updateTicketStatus(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		form = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);
		assertThat(form.status, is("かえちまったぜ！"));
		
		//バージョンが異なる場合
		form.versionNo = "-1";
		try {
			service.updateTicketStatus(form, projectKeyString);
			fail();
		} catch (ConcurrentModificationException e) {}
	}
	
	/**
	 * deleteのテスト.
	 * @throws Exception
	 */
	@Test
	public void testDelete() throws Exception {
		createTestData();
		
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		TicketForm form = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);		
		service.delete(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		form = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);
		assertThat(form.keyToString, is(nullValue()));
		
		//プロジェクトコードが異なる場合
		form = service.get(Datastore.keyToString(ticketKeyList.get(1)), projectKeyString);		
		try {
			service.delete(form, Datastore.keyToString(projectKeyList.get(1)));
			fail();
		} catch (ConcurrentModificationException e) {}
		
	}
	
	/**
	 * putのテスト.
	 * @throws Exception
	 */
	@Test
	public void testPut() throws Exception {
		createTestData();
		
		//新規登録
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		TicketForm form = service.get("", projectKeyString);
		form.status = "はげ";
		form.title = "たいとる";
		form.content = "内容はないよう";
		form.endCondition = "終わらせんぞ！";
		form.period = "20120401";
		form.priority = "とにかく急いで！";
		form.targetKind = "実装";
		form.category = "カテゴリ１";
		form.milestone = Datastore.keyToString(Datastore.createKey(MilestoneModel.class, 30L));
		form.targetVersion = "1.0.9";
		form.targetMember = Datastore.keyToString(Datastore.createKey(MemberModel.class, 900L));
		form.parentKey = ConvertUtils.toString(ticketNoList.get(0));
		service.put(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		String[] openStatus = new String[]{"未対応","対応済み"};
		//ステータスの検索条件
		TicketDao.Param param = new TicketDao.Param();
		param.status = new String[]{"はげ"};
		param.title = "たいとる";
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		List<TicketModelEx> actualList = service.getList(param, "", false);
		Key createTicketKey = actualList.get(0).getModel().getKey();
		Long createTicketNo = actualList.get(0).getModel().getNo();

		form = service.get(Datastore.keyToString(createTicketKey), projectKeyString);
		assertThat(form.keyToString, is(Datastore.keyToString(createTicketKey)));
		assertThat(form.status, is("はげ"));
		assertThat(form.title, is("たいとる"));
		assertThat(form.content, is("内容はないよう"));
		assertThat(form.endCondition, is("終わらせんぞ！"));
		assertThat(form.period, is("20120401"));
		assertThat(form.priority, is("とにかく急いで！"));
		assertThat(form.targetKind, is("実装"));
		assertThat(form.category, is("カテゴリ１"));
		assertThat(form.milestone, is(Datastore.keyToString(Datastore.createKey(MilestoneModel.class, 30L))));
		assertThat(form.targetVersion, is("1.0.9"));
		assertThat(form.targetMember, is(Datastore.keyToString(Datastore.createKey(MemberModel.class, 900L))));
		assertThat(form.parentKey, is(ConvertUtils.toString(ticketNoList.get(0))));
		assertThat(form.versionNo, is("1"));
		
		//更新
		form.status = "はげ2";
		form.title = "たいとる2";
		form.content = "内容はないよう2";
		form.endCondition = "終わらせんぞ！2";
		form.period = "20120402";
		form.priority = "とにかく急いで！2";
		form.targetKind = "実装2";
		form.category = "カテゴリ2";
		form.milestone = Datastore.keyToString(Datastore.createKey(MilestoneModel.class, 32L));
		form.targetVersion = "1.0.9.2";
		form.targetMember = Datastore.keyToString(Datastore.createKey(MemberModel.class, 902L));
		form.parentKey = ConvertUtils.toString(ticketNoList.get(1));
		service.put(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		form = service.get(Datastore.keyToString(createTicketKey), projectKeyString);
		assertThat(form.keyToString, is(Datastore.keyToString(createTicketKey)));
		assertThat(form.status, is("はげ2"));
		assertThat(form.title, is("たいとる2"));
		assertThat(form.content, is("内容はないよう2"));
		assertThat(form.endCondition, is("終わらせんぞ！2"));
		assertThat(form.period, is("20120402"));
		assertThat(form.priority, is("とにかく急いで！2"));
		assertThat(form.targetKind, is("実装2"));
		assertThat(form.category, is("カテゴリ2"));
		assertThat(form.milestone, is(Datastore.keyToString(Datastore.createKey(MilestoneModel.class, 32L))));
		assertThat(form.targetVersion, is("1.0.9.2"));
		assertThat(form.targetMember, is(Datastore.keyToString(Datastore.createKey(MemberModel.class, 902L))));
		assertThat(form.parentKey, is(ConvertUtils.toString(ticketNoList.get(1))));
		assertThat(form.versionNo, is("2"));
		
		//空に更新
		form.status = "はげ3";
		form.title = "たいとる3";
		form.content = "内容はないよう3";
		form.endCondition = "終わらせんぞ！3";
		form.period = "20120403";
		form.priority = "とにかく急いで！3";
		form.targetKind = "実装3";
		form.category = "カテゴリ3";
		form.milestone = "";
		form.targetVersion = "1.0.9.3";
		form.targetMember = "";
		form.parentKey = "";
		service.put(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		form = service.get(Datastore.keyToString(createTicketKey), projectKeyString);
		assertThat(form.keyToString, is(Datastore.keyToString(createTicketKey)));
		assertThat(form.status, is("はげ3"));
		assertThat(form.title, is("たいとる3"));
		assertThat(form.content, is("内容はないよう3"));
		assertThat(form.endCondition, is("終わらせんぞ！3"));
		assertThat(form.period, is("20120403"));
		assertThat(form.priority, is("とにかく急いで！3"));
		assertThat(form.targetKind, is("実装3"));
		assertThat(form.category, is("カテゴリ3"));
		assertThat(form.milestone, is(nullValue()));
		assertThat(form.targetVersion, is("1.0.9.3"));
		assertThat(form.targetMember, is(nullValue()));
		assertThat(form.parentKey, is(nullValue()));
		assertThat(form.versionNo, is("3"));
		
		
		//バージョンが異なる
		form = service.get(Datastore.keyToString(createTicketKey), projectKeyString);
		form.versionNo = "-1";
		try {
			service.put(form, projectKeyString);
			fail();
		}catch(ConcurrentModificationException e) {}
		
		//親に指定したチケットが存在しない
		form = service.get(Datastore.keyToString(createTicketKey), projectKeyString);
		form.parentKey = "-1";
		try {
			service.put(form, projectKeyString);
			fail();
		}catch(NotExistTicketException e) {}
		
		
		//自分を親として登録しようとした
		form = service.get(Datastore.keyToString(createTicketKey), projectKeyString);
		form.parentKey = ConvertUtils.toString(createTicketNo);
		try {
			service.put(form, projectKeyString);
			fail();
		}catch(ParentSelfTicketException e) {}
	}
	
	/**
	 * getDetailのテスト.
	 * @throws Exception
	 */
	@Test
	public void testGetDetail() throws Exception {
		createTestData();

		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		TicketDetailForm actual = service.getDetail("", projectKeyString);
		assertThat(actual, is(nullValue()));
		
		//データが存在する場合(チケット関連無し)
		actual = service.getDetail(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);		
		assertThat(actual.form.keyToString, is(Datastore.keyToString(ticketKeyList.get(0))));
		assertThat(actual.parentTicket, is(nullValue()));
		assertThat(actual.childTicketList.size(), is(0));
		
		//データが存在する場合(チケット関連有り)
		actual = service.getDetail(Datastore.keyToString(childKey), projectKeyString);		
		assertThat(actual.form.keyToString, is(Datastore.keyToString(childKey)));
		assertThat(actual.parentTicket.getKey(), is(parentKey));
		assertThat(actual.childTicketList.size(), is(2));
		assertThat(actual.childTicketList.get(0).getKey(), is(grandChildKey.get(0)));
		assertThat(actual.childTicketList.get(1).getKey(), is(grandChildKey.get(1)));

		//プロジェクトが異なる
		actual = service.getDetail(Datastore.keyToString(childKey), 
				Datastore.keyToString(projectKeyList.get(1)));
		assertThat(actual, is(nullValue()));
	}
	
	/**
	 * getのテスト
	 * @throws Exception
	 */
	@Test
	public void testGet() throws Exception {
		createTestData();
		
		//新規登録
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		TicketForm actual = service.get("", projectKeyString);
		assertThat(actual.ticketMst, is(not(nullValue())));
		
		//データが存在する場合
		actual = service.get(Datastore.keyToString(ticketKeyList.get(0)), projectKeyString);		
		assertThat(actual.keyToString, is(Datastore.keyToString(ticketKeyList.get(0))));

		//親の設定がある場合
		actual = service.get(Datastore.keyToString(childKey), projectKeyString);
		assertThat(actual.keyToString, is(Datastore.keyToString(childKey)));
		assertThat(actual.parentKey, is(ConvertUtils.toString(parentNo)));
		assertThat(actual.targetMember, 
				is(Datastore.keyToString(
						Datastore.createKey(MemberModel.class, 2L))));
		assertThat(actual.milestone, 
				is(Datastore.keyToString(
						Datastore.createKey(MilestoneModel.class, 1L))));
	}
	
	/**
	 * getListのテスト.
	 * @throws Exception
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
		List<TicketModelEx> actualList = service.getList(param, "", false);
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getModel().getKey(), is(ticketKeyList.get(0)));
		assertThat(actualList.get(1).getModel().getKey(), is(ticketKeyList.get(1)));
		assertThat(actualList.get(2).getModel().getKey(), is(ticketKeyList.get(2)));
		assertThat(actualList.get(3).getModel().getKey(), is(ticketKeyList.get(5)));

		param.limit = 2;
		actualList = service.getList(param, "hige@hoge.hage", true);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getModel().getKey(), is(ticketKeyList.get(2)));
		
	}
	
	/**
	 * createPeriodStatusのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testCreatePeriodStatus() throws Exception {
		
		String[] openStatus = new String[]{"未対応","対応中"};
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		Date today = sdf.parse("20100101");
		Date targetDate = sdf.parse("20100101");
		PeriodStatus actual = service.createPeriodStatus(today, targetDate, "未対応", openStatus);
		assertThat(actual, is(PeriodStatus.today));
		
		targetDate = sdf.parse("20091231");
		actual = service.createPeriodStatus(today, targetDate, "対応中", openStatus);
		assertThat(actual, is(PeriodStatus.periodDate));
		
		targetDate = sdf.parse("20100102");
		actual = service.createPeriodStatus(today, targetDate, "未対応", openStatus);
		assertThat(actual, is(nullValue()));

		targetDate = sdf.parse("20091231");
		actual = service.createPeriodStatus(today, targetDate, "完了", openStatus);
		assertThat(actual, is(nullValue()));

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
			ticketNoList.add(model.getNo());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		TicketModel parentModel = new TicketModel();
		parentModel.setProjectKey(projectKeyList.get(0));
		parentModel.setTitle("親Ticket");
		ticketDao.put(parentModel);
		parentKey = parentModel.getKey();
		parentNo = parentModel.getNo();
		
		TicketModel childModel = new TicketModel();
		childModel.setProjectKey(projectKeyList.get(0));
		childModel.setTitle("子Ticket");
		childModel.setParentTicketKey(parentKey);
		childModel.setMilestone(Datastore.createKey(MilestoneModel.class, 1L));
		childModel.setTargetMemberKey(Datastore.createKey(MemberModel.class, 2L));
		ticketDao.put(childModel);
		childKey = childModel.getKey();
		childNo = childModel.getNo();
		
		TicketModel grandChildModel = new TicketModel();
		grandChildModel.setProjectKey(projectKeyList.get(0));
		grandChildModel.setTitle("孫Ticket1");
		grandChildModel.setParentTicketKey(childKey);
		ticketDao.put(grandChildModel);
		grandChildKey.add(grandChildModel.getKey());
		grandChildNo.add(grandChildModel.getNo());
		
		grandChildModel = new TicketModel();
		grandChildModel.setProjectKey(projectKeyList.get(0));
		grandChildModel.setTitle("孫Ticket2");
		grandChildModel.setParentTicketKey(childKey);
		ticketDao.put(grandChildModel);
		grandChildKey.add(grandChildModel.getKey());
		grandChildNo.add(grandChildModel.getNo());
		
		MemberModel memberModel = new MemberModel();
		memberModel.createKey("hige@hoge.hage");
		memberDao.put(memberModel);
		
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
