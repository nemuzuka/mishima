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

import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.StatusDao;
import jp.co.nemuzuka.form.StatusForm;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.model.StatusModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * StatusServiceImplのテストクラス.
 * @author kazumune
 */
public class StatusServiceImplTest extends AppEngineTestCase4HRD {

	StatusServiceImpl service = StatusServiceImpl.getInstance();
	StatusDao statusDao = StatusDao.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	
	Key projectKey;

	/**
	 * get/putのテスト.
	 */
	@Test
	public void testGetAndPut() {
		createTestData();
		
		//該当データが存在しない場合
		String projectKeyString = Datastore.keyToString(projectKey);
		StatusForm actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getStatusName(), is(nullValue()));
		assertThat(actual.getCloseStatusName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//該当データが存在しない場合
		actual = service.get("");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getStatusName(), is(nullValue()));
		assertThat(actual.getCloseStatusName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//登録する
		actual.keyToString = null;
		actual.statusName = "ステータス1\r\nステータス2\nステータス3";
		actual.closeStatusName = "完了ステータス";
		service.put(actual, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//登録後、取得
		actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), is(Datastore.keyToString(Datastore.createKey(StatusModel.class, projectKeyString))));
		assertThat(actual.getStatusName(), is("ステータス1\r\nステータス2\nステータス3"));
		assertThat(actual.getCloseStatusName(), is("完了ステータス"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//更新
		actual.statusName = "おほほー";
		actual.closeStatusName = "あっはは";
		service.put(actual, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//更新後、取得
		actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), 
				is(Datastore.keyToString(Datastore.createKey(StatusModel.class, projectKeyString))));
		assertThat(actual.getStatusName(), is("おほほー"));
		assertThat(actual.getCloseStatusName(), is("あっはは"));
		assertThat(actual.getVersionNo(), is("2"));

		//バージョン不正
		actual.versionNo = "-1";
		try {
			service.put(actual, projectKeyString);
			fail();
		} catch (ConcurrentModificationException e) {}
	}
	
	/**
	 * getListのテスト.
	 */
	@Test
	public void testGetList() {
		createTestData();

		//テスト用データ作成
		String projectKeyString = Datastore.keyToString(projectKey);
		StatusForm form = new StatusForm();
		form.statusName = "ステータス1\r\nステータス2\nステータス3";
		form.closeStatusName = "";
		service.put(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		List<LabelValueBean> actualList = service.getList(projectKeyString);
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getLabel(), is(""));
		assertThat(actualList.get(0).getValue(), is(""));
		assertThat(actualList.get(1).getLabel(), is("ステータス1"));
		assertThat(actualList.get(1).getValue(), is("ステータス1"));
		assertThat(actualList.get(2).getLabel(), is("ステータス2"));
		assertThat(actualList.get(2).getValue(), is("ステータス2"));
		assertThat(actualList.get(3).getLabel(), is("ステータス3"));
		assertThat(actualList.get(3).getValue(), is("ステータス3"));
	}

	/**
	 * テストデータ作成.
	 * プロジェクト1件を作成します。
	 */
	private void createTestData() {
		
		ProjectModel model = new ProjectModel();
		model.setProjectId("test_project");
		model.setProjectName("テストプロジェクト");
		model.setProjectSummary(new Text("テスト用プロジェクトですってば"));
		projectDao.put(model);
		projectKey = model.getKey();
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
