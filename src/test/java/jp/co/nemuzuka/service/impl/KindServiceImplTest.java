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
import jp.co.nemuzuka.dao.KindDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.form.KindForm;
import jp.co.nemuzuka.model.KindModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * KindServiceImplのテストクラス.
 * @author kazumune
 */
public class KindServiceImplTest extends AppEngineTestCase4HRD {

	KindServiceImpl service = KindServiceImpl.getInstance();
	KindDao kindDao = KindDao.getInstance();
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
		KindForm actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getKindName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//該当データが存在しない場合
		actual = service.get("");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getKindName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//登録する
		actual.keyToString = null;
		actual.kindName = "種別1\r\n種別2\n種別3";
		service.put(actual, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//登録後、取得
		actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), is(Datastore.keyToString(Datastore.createKey(KindModel.class, projectKeyString))));
		assertThat(actual.getKindName(), is("種別1\r\n種別2\n種別3"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//更新
		actual.kindName = "おほほー";
		service.put(actual, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//更新後、取得
		actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), 
				is(Datastore.keyToString(Datastore.createKey(KindModel.class, projectKeyString))));
		assertThat(actual.getKindName(), is("おほほー"));
		assertThat(actual.getVersionNo(), is("2"));

		//バージョン不正
		actual.kindName = "おほほー123";
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
		KindForm form = new KindForm();
		form.kindName = "種別1\r\n種別2\n種別3";
		service.put(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		List<LabelValueBean> actualList = service.getList(projectKeyString);
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getLabel(), is(""));
		assertThat(actualList.get(0).getValue(), is(""));
		assertThat(actualList.get(1).getLabel(), is("種別1"));
		assertThat(actualList.get(1).getValue(), is("種別1"));
		assertThat(actualList.get(2).getLabel(), is("種別2"));
		assertThat(actualList.get(2).getValue(), is("種別2"));
		assertThat(actualList.get(3).getLabel(), is("種別3"));
		assertThat(actualList.get(3).getValue(), is("種別3"));
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
