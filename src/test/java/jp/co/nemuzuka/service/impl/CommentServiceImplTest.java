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

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.entity.CommentModelEx;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * CommentServiceImplのテストクラス.
 * @author kazumune
 */
public class CommentServiceImplTest extends AppEngineTestCase4HRD {

	CommentServiceImpl service = CommentServiceImpl.getInstance();
	TodoDao todoDao = TodoDao.getInstance();
	Key todoKey = null;

	MemberDao memberDao = MemberDao.getInstance();
	
	
	/**
	 * putのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testPut() throws Exception {
		createTestData();
		
		List<CommentModelEx> actualList = service.getList(todoKey);
		assertThat(actualList.size(), is(0));
		
		service.put(todoKey, "コメントでんがな", "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		actualList = service.getList(todoKey);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getComment(), is("コメントでんがな"));
		assertThat(actualList.get(0).getCreateMemberName(), is("なにがし"));
		assertThat(actualList.get(0).getCreatedAt(), is(not(nullValue())));
		
		//削除
		String commentKeyString = Datastore.keyToString(actualList.get(0).getModel().getKey());
		service.delete(todoKey, commentKeyString, 1L);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		actualList = service.getList(todoKey);
		assertThat(actualList.size(), is(0));
		
		//削除対象レコードが存在しない場合
		try {
			service.delete(todoKey, commentKeyString, 1L);
			fail();
		} catch(ConcurrentModificationException e) {}
		
		//存在しないmailアドレスで作成された
		service.put(todoKey, "コメントでんがな2", "hoge2@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		//処理は終了するが、登録されない
		actualList = service.getList(todoKey);
		assertThat(actualList.size(), is(0));

		
	}
	
	
	/**
	 * テストデータ作成.
	 * @throws Exception 例外
	 */
	private void createTestData() throws Exception {
		MemberModel memberModel = new MemberModel();
		memberModel.createKey("hoge@hige.hage");
		memberModel.setAuthority(Authority.admin);
		memberModel.setName("なにがし");
		memberDao.put(memberModel);
		
		TodoModel model = new TodoModel();
		model.setStatus(TodoStatus.nothing);
		Key memberKey = Datastore.createKey(MemberModel.class, "hoge@hige.hage");
		model.setCreateMemberKey(memberKey);
		model.setContent(new Text("詳細123"));
		todoDao.put(model);
		todoKey = model.getKey();

		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
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
