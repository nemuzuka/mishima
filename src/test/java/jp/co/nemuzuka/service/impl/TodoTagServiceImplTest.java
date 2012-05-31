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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.TodoTagDao;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoTagModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * TodoTagServiceImplのテストクラス.
 * @author kazumune
 */
public class TodoTagServiceImplTest extends AppEngineTestCase4HRD {

	TodoTagServiceImpl service = TodoTagServiceImpl.getInstance();
	TodoTagDao todoTagDao = TodoTagDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	MemberService memberService = MemberServiceImpl.getInstance();

	List<Key> todoTagKeyList = new ArrayList<Key>();
	
	
	/**
	 * deleteのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testDelete() throws Exception {
		createTestData();
		//delete前の件数
		List<TodoTagModel> actualList = service.getList("hoge@hige.hage");
		assertThat(actualList.size(), is(6));
		
		String keyString = Datastore.keyToString(todoTagKeyList.get(0));
		service.delete(keyString, 1L, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//delete後の確認
		actualList = service.getList("hoge@hige.hage");
		assertThat(actualList.size(), is(5));
		assertThat(actualList.get(0).getTagName(), is("タグ名1"));
		assertThat(actualList.get(1).getTagName(), is("タグ名2"));
		assertThat(actualList.get(2).getTagName(), is("タグ名3"));
		assertThat(actualList.get(3).getTagName(), is("タグ名4"));
		assertThat(actualList.get(4).getTagName(), is("タグ名5"));

		//該当レコードが存在しない場合
		try {
			service.delete(keyString, 1L, "hoge@hige.hage");
			fail();
		} catch(ConcurrentModificationException e) {}
	}
	
	/**
	 * putのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testPut() throws Exception {
		createTestData();
		
		//put前の件数
		List<TodoTagModel> actualList = service.getList("hoge@hige.hage");
		assertThat(actualList.size(), is(6));
		
		service.put(new String[]{"タグ名0","追加タグHoge"}, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//処理後の件数
		actualList = service.getList("hoge@hige.hage");
		assertThat(actualList.size(), is(7));
		assertThat(actualList.get(6).getTagName(), is("追加タグHoge"));
		
		//
		//全て存在する
		//
		actualList = service.getList("hoge2@hige.hage");
		assertThat(actualList.size(), is(4));

		service.put(new String[]{"タグ名6","タグ名9"}, "hoge2@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		actualList = service.getList("hoge2@hige.hage");
		assertThat(actualList.size(), is(4));

		//
		//引数がサイズ0
		//
		actualList = service.getList("hoge3@hige.hage");
		assertThat(actualList.size(), is(0));

		service.put(new String[0], "hoge3@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		actualList = service.getList("hoge3@hige.hage");
		assertThat(actualList.size(), is(0));

		//
		//引数がnull
		//
		service.put(null, "hoge3@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		actualList = service.getList("hoge3@hige.hage");
		assertThat(actualList.size(), is(0));
		
	}
	
	/**
	 * getListのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetList() throws Exception {
		
		createTestData();
		
		List<TodoTagModel> actualList = service.getList("hoge@hige.hage");
		assertThat(actualList.size(), is(6));
		assertThat(actualList.get(0).getTagName(), is("タグ名0"));
		assertThat(actualList.get(1).getTagName(), is("タグ名1"));
		assertThat(actualList.get(2).getTagName(), is("タグ名2"));
		assertThat(actualList.get(3).getTagName(), is("タグ名3"));
		assertThat(actualList.get(4).getTagName(), is("タグ名4"));
		assertThat(actualList.get(5).getTagName(), is("タグ名5"));
		
		actualList = service.getList("hoge2@hige.hage");
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getTagName(), is("タグ名6"));
		assertThat(actualList.get(1).getTagName(), is("タグ名7"));
		assertThat(actualList.get(2).getTagName(), is("タグ名8"));
		assertThat(actualList.get(3).getTagName(), is("タグ名9"));
		
		actualList = service.getList("hoge3@hige.hage");
		assertThat(actualList.size(), is(0));
	}
	
	
	/**
	 * テストデータ作成.
	 * @throws Exception 例外
	 */
	private void createTestData() throws Exception {
		
		TodoTagModel model = null;
		for(int i = 0; i < 10; i++) {
			
			model = new TodoTagModel();
			
			String targetEmail;
			if(0 <= i && i <= 5) {
				targetEmail = "hoge@hige.hage";
			} else {
				targetEmail = "hoge2@hige.hage";
			}
			
			Key memberKey = Datastore.createKey(MemberModel.class, targetEmail);
			model.setMemberKey(memberKey);
			model.setTagName("タグ名" + i);
			
			todoTagDao.put(model);
			todoTagKeyList.add(model.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		MemberModel memberModel = new MemberModel();
		memberModel.createKey("hoge@hige.hage");
		memberDao.put(memberModel);
		
		memberModel = new MemberModel();
		memberModel.createKey("hoge2@hige.hage");
		memberDao.put(memberModel);

		memberModel = new MemberModel();
		memberModel.createKey("hoge3@hige.hage");
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
