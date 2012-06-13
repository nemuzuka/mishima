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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MilestoneDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.TicketDao.Param;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.model.TicketModel;
import jp.co.nemuzuka.service.GanttService;
import jp.co.nemuzuka.service.GanttService.Result;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * GanttServiceImplのテストクラス.
 * @author kazumune
 */
public class GanttServiceServiceImplTest extends AppEngineTestCase4HRD {

	GanttServiceImpl service = GanttServiceImpl.getInstance();
	SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
	MilestoneDao milestoneDao = MilestoneDao.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	
	List<Key> milestoneKeyList;
	Key projectKey;
	
	/**
	 * reCreateTicketListのテスト.
	 * ・親-子の関係
	 * ・親Ticketの指定はあるが、親Ticketが存在しない
	 * ・親のみ
	 * パターンの混在
	 */
	@Test
	public void testReCreateTicketList7() {
		List<TicketModelEx> ticketList = createTicketList7();
		service.reCreateTicketList(ticketList);
		
		assertThat(ticketList.size(), is(6));
		assertThat(ticketList.get(0).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 151L)));
		assertThat(ticketList.get(0).getNestingLevel(), is(0));
		assertThat(ticketList.get(1).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 301L)));
		assertThat(ticketList.get(1).getNestingLevel(), is(0));
		assertThat(ticketList.get(2).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 302L)));
		assertThat(ticketList.get(2).getNestingLevel(), is(0));
		assertThat(ticketList.get(3).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 854L)));
		assertThat(ticketList.get(3).getNestingLevel(), is(0));
		assertThat(ticketList.get(4).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 853L)));
		assertThat(ticketList.get(4).getNestingLevel(), is(1));
		assertThat(ticketList.get(5).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 855L)));
		assertThat(ticketList.get(5).getNestingLevel(), is(0));
	}
	
	/**
	 * テスト用List作成.
	 * @return テスト用List
	 */
	private List<TicketModelEx> createTicketList7() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 151L));
		model.getModel().setParentTicketKey(null);
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 301L));
		model.getModel().setParentTicketKey(null);

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 302L));
		model.getModel().setParentTicketKey(null);

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 853L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 854L));

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 854L));
		model.getModel().setParentTicketKey(null);

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 855L));
		model.getModel().setParentTicketKey(null);

		return list;
	}

	/**
	 * reCreateTicketListのテスト.
	 * ・親-子の関係
	 * ・親Ticketの指定はあるが、親Ticketが存在しない
	 * パターンの混在
	 */
	@Test
	public void testReCreateTicketList6() {
		List<TicketModelEx> ticketList = createTicketList6();
		service.reCreateTicketList(ticketList);
		
		assertThat(ticketList.size(), is(5));
		assertThat(ticketList.get(0).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 1L)));
		assertThat(ticketList.get(0).getNestingLevel(), is(0));
		assertThat(ticketList.get(1).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 2L)));
		assertThat(ticketList.get(1).getNestingLevel(), is(1));
		assertThat(ticketList.get(2).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 3L)));
		assertThat(ticketList.get(2).getNestingLevel(), is(0));
		assertThat(ticketList.get(3).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 10L)));
		assertThat(ticketList.get(3).getNestingLevel(), is(0));
		assertThat(ticketList.get(4).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 4L)));
		assertThat(ticketList.get(4).getNestingLevel(), is(1));
	}
	
	/**
	 * テスト用List作成.
	 * @return テスト用List
	 */
	private List<TicketModelEx> createTicketList6() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 1L));
		model.getModel().setParentTicketKey(null);
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 2L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 1L));

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 3L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 9L));

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 4L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 10L));

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 10L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 11L));

		return list;
	}

	/**
	 * reCreateTicketListのテスト.
	 * 親Ticketの指定はあるが、親Ticketが存在しないパターン
	 */
	@Test
	public void testReCreateTicketList5() {
		List<TicketModelEx> ticketList = createTicketList5();
		service.reCreateTicketList(ticketList);
		
		assertThat(ticketList.size(), is(3));
		assertThat(ticketList.get(0).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 1L)));
		assertThat(ticketList.get(0).getNestingLevel(), is(0));
		assertThat(ticketList.get(1).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 2L)));
		assertThat(ticketList.get(1).getNestingLevel(), is(0));
		assertThat(ticketList.get(2).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 3L)));
		assertThat(ticketList.get(2).getNestingLevel(), is(0));
	}
	
	/**
	 * テスト用List作成.
	 * @return テスト用List
	 */
	private List<TicketModelEx> createTicketList5() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 1L));
		model.getModel().setParentTicketKey(null);
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 2L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 5L));

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 3L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 9L));

		return list;
	}

	/**
	 * reCreateTicketListのテスト.
	 * 親-子-孫-曾孫Ticketが存在するパターン
	 */
	@Test
	public void testReCreateTicketList4() {
		List<TicketModelEx> ticketList = createTicketList4();
		service.reCreateTicketList(ticketList);
		
		assertThat(ticketList.size(), is(4));
		assertThat(ticketList.get(0).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 1L)));
		assertThat(ticketList.get(0).getNestingLevel(), is(0));
		assertThat(ticketList.get(1).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 2L)));
		assertThat(ticketList.get(1).getNestingLevel(), is(1));
		assertThat(ticketList.get(2).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 3L)));
		assertThat(ticketList.get(2).getNestingLevel(), is(2));
		assertThat(ticketList.get(3).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 4L)));
		assertThat(ticketList.get(3).getNestingLevel(), is(3));
	}
	
	/**
	 * テスト用List作成.
	 * @return テスト用List
	 */
	private List<TicketModelEx> createTicketList4() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 1L));
		model.getModel().setParentTicketKey(null);
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 2L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 1L));
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 3L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 2L));

		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 4L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 3L));

		return list;
	}

	/**
	 * reCreateTicketListのテスト.
	 * 親-子-孫Ticketが存在するパターン
	 */
	@Test
	public void testReCreateTicketList3() {
		List<TicketModelEx> ticketList = createTicketList3();
		service.reCreateTicketList(ticketList);
		
		assertThat(ticketList.size(), is(3));
		assertThat(ticketList.get(0).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 1L)));
		assertThat(ticketList.get(0).getNestingLevel(), is(0));
		assertThat(ticketList.get(1).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 2L)));
		assertThat(ticketList.get(1).getNestingLevel(), is(1));
		assertThat(ticketList.get(2).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 3L)));
		assertThat(ticketList.get(2).getNestingLevel(), is(2));
	}
	
	/**
	 * テスト用List作成.
	 * @return テスト用List
	 */
	private List<TicketModelEx> createTicketList3() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 1L));
		model.getModel().setParentTicketKey(null);
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 2L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 1L));
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 3L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 2L));

		return list;
	}

	/**
	 * reCreateTicketListのテスト.
	 * 親-子Ticketが存在するパターン
	 */
	@Test
	public void testReCreateTicketList2() {
		List<TicketModelEx> ticketList = createTicketList2();
		service.reCreateTicketList(ticketList);
		
		assertThat(ticketList.size(), is(2));
		assertThat(ticketList.get(0).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 1L)));
		assertThat(ticketList.get(0).getNestingLevel(), is(0));
		assertThat(ticketList.get(1).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 2L)));
		assertThat(ticketList.get(1).getNestingLevel(), is(1));
	}
	
	/**
	 * テスト用List作成.
	 * @return テスト用List
	 */
	private List<TicketModelEx> createTicketList2() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 1L));
		model.getModel().setParentTicketKey(null);
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 2L));
		model.getModel().setParentTicketKey(Datastore.createKey(TicketModel.class, 1L));
		
		return list;
	}

	/**
	 * reCreateTicketListのテスト.
	 * 親Ticketのみ存在するパターン
	 */
	@Test
	public void testReCreateTicketList1() {
		//nullのものしか存在しないパターン
		List<TicketModelEx> ticketList = createTicketList1();
		service.reCreateTicketList(ticketList);
		
		assertThat(ticketList.size(), is(2));
		assertThat(ticketList.get(0).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 1L)));
		assertThat(ticketList.get(0).getNestingLevel(), is(0));
		assertThat(ticketList.get(1).getModel().getKey(), 
				is(Datastore.createKey(TicketModel.class, 2L)));
		assertThat(ticketList.get(1).getNestingLevel(), is(0));
	}
	
	/**
	 * テスト用List作成.
	 * @return テスト用List
	 */
	private List<TicketModelEx> createTicketList1() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 1L));
		model.getModel().setParentTicketKey(null);
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setKey(Datastore.createKey(TicketModel.class, 2L));
		model.getModel().setParentTicketKey(null);
		
		return list;
	}

	/**
	 * getListのテスト.
	 * @throws Exception
	 */
	@Test
	public void testGetList() throws Exception {
		createTestData();
		
		//マイルストーンが設定されている場合
		Param param = new Param();
		param.milestone = Datastore.keyToString(milestoneKeyList.get(0));
		param.projectKeyString = Datastore.keyToString(projectKey);
		Result actual = service.getList(param);
		assertThat(actual.milestoneName, is("マイルストン_0"));
		assertThat(actual.milestoneStartDate, is("20120401"));
		assertThat(actual.milestoneEndDate, is("20120430"));
		
		//マイルストーンが設定されていない場合
		param = new Param();
		param.projectKeyString = Datastore.keyToString(projectKey);
		actual = service.getList(param);
		assertThat(actual.milestoneName, is(nullValue()));
		assertThat(actual.milestoneStartDate, is(nullValue()));
		assertThat(actual.milestoneEndDate, is(nullValue()));
		
		//設定されているが、存在しない場合
		param = new Param();
		param.milestone = Datastore.keyToString(Datastore.createKey(MilestoneModel.class, -1L));
		param.projectKeyString = Datastore.keyToString(projectKey);
		actual = service.getList(param);
		assertThat(actual.milestoneName, is(nullValue()));
		assertThat(actual.milestoneStartDate, is(nullValue()));
		assertThat(actual.milestoneEndDate, is(nullValue()));
		
	}
	
	/**
	 * setDateのテスト.
	 * マイルストーン未指定、全てのTicket日付null
	 */
	@Test
	public void testSetDate() {
		GanttService.Result result = createTestData1();
		service.setDate(result, null, null);
		//ガントチャート開始日がシステム日であること
		Date currentDate = CurrentDateUtils.getInstance().getCurrentDate();
		assertThat(result.startDate, 
				is(ConvertUtils.toString(currentDate, sdf)));
		//ガントチャート終了日がシステム日＋１月であること
		Date endDate = DateTimeUtils.addMonths(currentDate, 1);
		assertThat(result.endDate, 
				is(ConvertUtils.toString(endDate, sdf)));
		
		//Ticketの開始日、終了日が上書きされていること
		TicketModelEx actual = result.ticketList.get(0);
		assertThat(actual.getStartDate(), is(ConvertUtils.toString(currentDate, sdf)));
		assertThat(actual.isUpdateStartDate(), is(true));
		assertThat(actual.getPeriod(), is(ConvertUtils.toString(endDate, sdf)));
		assertThat(actual.isUpdatePeriod(), is(true));
		
		//マイルストーンの開始日、終了日が上書きされていること
		assertThat(result.milestoneStartDate, is(ConvertUtils.toString(currentDate, sdf)));
		assertThat(result.updateStartDate, is(true));
		assertThat(result.milestoneEndDate, is(ConvertUtils.toString(endDate, sdf)));
		assertThat(result.updateEndDate, is(true));
	}
	
	/**
	 * setDateのテスト.
	 * マイルストーン未指定、Ticket開始日設定、終了日null
	 */
	@Test
	public void testSetDate2() {
		GanttService.Result result = createTestData2();
		service.setDate(result, null, null);
		//ガントチャート開始日がチケットの開始日であること
		assertThat(result.startDate, 
				is("20120101"));
		//ガントチャート終了日がチケットの開始日＋１月であること
		assertThat(result.endDate, 
				is("20120201"));
		
		//Ticketの開始日、終了日が上書きされていること
		TicketModelEx actual = result.ticketList.get(0);
		assertThat(actual.getStartDate(), is("20120101"));
		assertThat(actual.isUpdateStartDate(), is(false));
		assertThat(actual.getPeriod(), is("20120201"));
		assertThat(actual.isUpdatePeriod(), is(true));
		
		//マイルストーンの開始日、終了日が上書きされていること
		assertThat(result.milestoneStartDate, is("20120101"));
		assertThat(result.updateStartDate, is(true));
		assertThat(result.milestoneEndDate, is("20120201"));
		assertThat(result.updateEndDate, is(true));
	}

	/**
	 * setDateのテスト.
	 * マイルストーン未指定、Ticket開始日null、終了日設定
	 */
	@Test
	public void testSetDate3() {
		GanttService.Result result = createTestData3();
		service.setDate(result, null, null);
		//ガントチャート開始日がチケットの終了日であること
		assertThat(result.startDate, 
				is("20120101"));
		//ガントチャート終了日がチケットの開始日＋15日であること
		assertThat(result.endDate, 
				is("20120116"));
		
		//Ticketの開始日が上書きされていること
		TicketModelEx actual = result.ticketList.get(0);
		assertThat(actual.getStartDate(), is("20120101"));
		assertThat(actual.isUpdateStartDate(), is(true));
		assertThat(actual.getPeriod(), is("20120101"));
		assertThat(actual.isUpdatePeriod(), is(false));
		
		//マイルストーンの開始日、終了日が上書きされていること
		assertThat(result.milestoneStartDate, is("20120101"));
		assertThat(result.updateStartDate, is(true));
		assertThat(result.milestoneEndDate, is("20120116"));
		assertThat(result.updateEndDate, is(true));
	}

	/**
	 * setDateのテスト.
	 * マイルストーン未指定、Ticket開始日設定、終了日設定
	 */
	@Test
	public void testSetDate4() {
		GanttService.Result result = createTestData4();
		service.setDate(result, null, null);
		//ガントチャート開始日がチケットの開始日であること
		assertThat(result.startDate, 
				is("20120101"));
		//ガントチャート終了日がチケットの終了日であること
		assertThat(result.endDate, 
				is("20120120"));
		
		//Ticketの開始日、終了日が上書きされていないこと
		TicketModelEx actual = result.ticketList.get(0);
		assertThat(actual.getStartDate(), is("20120101"));
		assertThat(actual.isUpdateStartDate(), is(false));
		assertThat(actual.getPeriod(), is("20120120"));
		assertThat(actual.isUpdatePeriod(), is(false));
		
		//マイルストーンの開始日、終了日が上書きされていること
		assertThat(result.milestoneStartDate, is("20120101"));
		assertThat(result.updateStartDate, is(true));
		assertThat(result.milestoneEndDate, is("20120120"));
		assertThat(result.updateEndDate, is(true));
	}

	/**
	 * setDateのテスト.
	 * マイルストーン未指定、Ticket開始日null、終了日設定(過去)
	 */
	@Test
	public void testSetDate5() {
		GanttService.Result result = createTestData5();
		service.setDate(result, null, null);
		//ガントチャート開始日がチケットの開始日であること
		assertThat(result.startDate, 
				is("20100101"));
		//ガントチャート終了日がチケットの終了日であること
		assertThat(result.endDate, 
				is("20100116"));
		
		//Ticketの終了日が上書きされていないこと
		TicketModelEx actual = result.ticketList.get(0);
		assertThat(actual.getStartDate(), is("20100101"));
		assertThat(actual.isUpdateStartDate(), is(true));
		assertThat(actual.getPeriod(), is("20100101"));
		assertThat(actual.isUpdatePeriod(), is(false));
		
		//マイルストーンの開始日、終了日が上書きされていること
		assertThat(result.milestoneStartDate, is("20100101"));
		assertThat(result.updateStartDate, is(true));
		assertThat(result.milestoneEndDate, is("20100116"));
		assertThat(result.updateEndDate, is(true));
	}

	/**
	 * setDateのテスト.
	 * 複数Ticketより、ガントチャートの開始日、終了日を判断する
	 */
	@Test
	public void testSetDate6() {
		GanttService.Result result = createTestData6();
		service.setDate(result, null, null);
		//ガントチャート開始日がチケットの中で一番過去の開始日であること
		assertThat(result.startDate, 
				is("20091231"));
		//ガントチャート終了日がチケットの中で一番未来の終了日であること
		assertThat(result.endDate, 
				is("20100115"));
		
		TicketModelEx actual = result.ticketList.get(0);
		assertThat(actual.getStartDate(), is("20100101"));
		assertThat(actual.isUpdateStartDate(), is(false));
		assertThat(actual.getPeriod(), is("20100102"));
		assertThat(actual.isUpdatePeriod(), is(false));
		
		actual = result.ticketList.get(1);
		assertThat(actual.getStartDate(), is("20091231"));
		assertThat(actual.isUpdateStartDate(), is(false));
		assertThat(actual.getPeriod(), is("20100115"));
		assertThat(actual.isUpdatePeriod(), is(false));
		
		actual = result.ticketList.get(2);
		assertThat(actual.getStartDate(), is("20091231"));
		assertThat(actual.isUpdateStartDate(), is(true));
		assertThat(actual.getPeriod(), is("20100115"));
		assertThat(actual.isUpdatePeriod(), is(true));

		actual = result.ticketList.get(3);
		assertThat(actual.getStartDate(), is("20100110"));
		assertThat(actual.isUpdateStartDate(), is(false));
		assertThat(actual.getPeriod(), is("20100110"));
		assertThat(actual.isUpdatePeriod(), is(false));

		//マイルストーンの開始日、終了日が上書きされていること
		assertThat(result.milestoneStartDate, is("20091231"));
		assertThat(result.updateStartDate, is(true));
		assertThat(result.milestoneEndDate, is("20100115"));
		assertThat(result.updateEndDate, is(true));
	}

	/**
	 * setDateのテスト.
	 * マイルストーン指定、Ticket開始日設定、終了日設定
	 */
	@Test
	public void testSetDate7() {
		GanttService.Result result = createTestData7();
		service.setDate(result, 
				ConvertUtils.toDate("20120103", sdf), 
				ConvertUtils.toDate("20120110", sdf));
		//ガントチャート開始日がチケットの開始日であること
		assertThat(result.startDate, 
				is("20120101"));
		//ガントチャート終了日がチケットの終了日であること
		assertThat(result.endDate, 
				is("20120120"));
		
		//Ticketの開始日、終了日が上書きされていないこと
		TicketModelEx actual = result.ticketList.get(0);
		assertThat(actual.getStartDate(), is("20120101"));
		assertThat(actual.isUpdateStartDate(), is(false));
		assertThat(actual.getPeriod(), is("20120120"));
		assertThat(actual.isUpdatePeriod(), is(false));
		
		//マイルストーンの開始日、終了日が上書きされていないこと
		assertThat(result.milestoneStartDate, is("20120103"));
		assertThat(result.updateStartDate, is(false));
		assertThat(result.milestoneEndDate, is("20120110"));
		assertThat(result.updateEndDate, is(false));
	}

	/**
	 * テストデータ作成.
	 * マイルストーン未指定のケース
	 * @return テストデータ
	 */
	private Result createTestData7() {
		GanttService.Result result = new GanttService.Result();
		result.milestoneName = "a";
		result.milestoneStartDate = "20120103";
		result.milestoneEndDate = "20120110";
		result.ticketList = createTicketModelEx4();
		return result;
	}

	
	/**
	 * テストデータ作成.
	 * マイルストーン未指定のケース
	 * @return テストデータ
	 */
	private Result createTestData6() {
		GanttService.Result result = new GanttService.Result();
		result.milestoneName = "a";
		result.ticketList = createTicketModelEx6();
		return result;
	}

	/**
	 * テストデータ作成.
	 * 複数Ticket設定
	 * @return テストデータ
	 */
	private List<TicketModelEx> createTicketModelEx6() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setStartDate(ConvertUtils.toDate("20100101", sdf));
		model.getModel().setPeriod(ConvertUtils.toDate("20100102", sdf));
		model.setStartDate("20100101");
		model.setPeriod("20100102");
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setStartDate(ConvertUtils.toDate("20091231", sdf));
		model.getModel().setPeriod(ConvertUtils.toDate("20100115", sdf));
		model.setStartDate("20091231");
		model.setPeriod("20100115");
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		
		model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setStartDate(ConvertUtils.toDate("20100110", sdf));
		model.getModel().setPeriod(ConvertUtils.toDate("20100110", sdf));
		model.setStartDate("20100110");
		model.setPeriod("20100110");
		
		return list;
	}

	/**
	 * テストデータ作成.
	 * マイルストーン未指定のケース
	 * @return テストデータ
	 */
	private Result createTestData5() {
		GanttService.Result result = new GanttService.Result();
		result.milestoneName = "a";
		result.ticketList = createTicketModelEx5();
		return result;
	}

	/**
	 * テストデータ作成.
	 * 開始日、終了日設定
	 * @return テストデータ
	 */
	private List<TicketModelEx> createTicketModelEx5() {
		Date endDate = ConvertUtils.toDate("20100101", sdf);
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setPeriod(endDate);
		model.setPeriod("20100101");
		return list;
	}

	/**
	 * テストデータ作成.
	 * マイルストーン未指定のケース
	 * @return テストデータ
	 */
	private Result createTestData4() {
		GanttService.Result result = new GanttService.Result();
		result.milestoneName = "a";
		result.ticketList = createTicketModelEx4();
		return result;
	}
	/**
	 * テストデータ作成.
	 * 開始日、終了日設定
	 * @return テストデータ
	 */
	private List<TicketModelEx> createTicketModelEx4() {
		Date startDate = ConvertUtils.toDate("20120101", sdf);
		Date endDate = ConvertUtils.toDate("20120120", sdf);
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setStartDate(startDate);
		model.getModel().setPeriod(endDate);
		model.setStartDate("20120101");
		model.setPeriod("20120120");
		return list;
	}

	/**
	 * テストデータ作成.
	 * マイルストーン未指定のケース
	 * @return テストデータ
	 */
	private Result createTestData3() {
		GanttService.Result result = new GanttService.Result();
		result.milestoneName = "a";
		result.ticketList = createTicketModelEx3();
		return result;
	}

	/**
	 * テストデータ作成.
	 * 終了日のみ設定されている
	 * @return テストデータ
	 */
	private List<TicketModelEx> createTicketModelEx3() {
		Date endDate = ConvertUtils.toDate("20120101", sdf);
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setPeriod(endDate);
		model.setPeriod("20120101");
		return list;
	}

	/**
	 * テストデータ作成.
	 * マイルストーン未指定のケース
	 * @return テストデータ
	 */
	private Result createTestData2() {
		GanttService.Result result = new GanttService.Result();
		result.milestoneName = "a";
		result.ticketList = createTicketModelEx2();
		return result;
	}

	/**
	 * テストデータ作成.
	 * 開始日のみ設定されている
	 * @return テストデータ
	 */
	private List<TicketModelEx> createTicketModelEx2() {
		Date startDate = ConvertUtils.toDate("20120101", sdf);
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		model.getModel().setStartDate(startDate);
		model.setStartDate("20120101");
		return list;
	}

	/**
	 * テストデータ作成.
	 * マイルストーン未指定のケース
	 * @return テストデータ
	 */
	private Result createTestData1() {
		GanttService.Result result = new GanttService.Result();
		result.milestoneName = "a";
		result.ticketList = createTicketModelEx1();
		return result;
	}
	
	/**
	 * テストデータ作成.
	 * 全ての開始日と終了日がnullであるケース
	 * @return テストデータ
	 */
	private List<TicketModelEx> createTicketModelEx1() {
		List<TicketModelEx> list = new ArrayList<TicketModelEx>();
		TicketModelEx model = new TicketModelEx();
		list.add(model);
		model.setModel(new TicketModel());
		return list;
	}

	/**
	 * テストデータ作成.
	 * プロジェクト1件
	 * マイルストーン3件を作成します。
	 * @throws ParseException 例外
	 */
	private void createTestData() throws ParseException {
		
		ProjectModel model = new ProjectModel();
		model.setProjectId("test_project");
		model.setProjectName("テストプロジェクト");
		model.setProjectSummary(new Text("テスト用プロジェクトですってば"));
		projectDao.put(model);
		projectKey = model.getKey();
		
		milestoneKeyList = new ArrayList<Key>();
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		for(int i = 0; i < 3; i++) {
			MilestoneModel milestoneModel = new MilestoneModel();
			milestoneModel.setMilestoneName("マイルストン_" + i);
			Date startDate = null;
			if(i == 0 || i == 2) {
				startDate = sdf.parse("20120401");
			}
			milestoneModel.setStartDate(startDate);
			Date endDate = null;
			if(i == 0 || i == 1) {
				endDate = sdf.parse("20120430");
			}
			milestoneModel.setEndDate(endDate);
			milestoneModel.setProjectKey(projectKey);
			milestoneModel.setSortNum(Long.MAX_VALUE);
			milestoneDao.put(milestoneModel);
			milestoneKeyList.add(milestoneModel.getKey());
		}
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
