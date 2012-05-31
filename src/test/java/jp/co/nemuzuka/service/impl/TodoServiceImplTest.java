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
import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoCommentForm;
import jp.co.nemuzuka.form.TodoDetailForm;
import jp.co.nemuzuka.form.TodoForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * TodoServiceImplのテストクラス.
 * @author kazumune
 */
public class TodoServiceImplTest extends AppEngineTestCase4HRD {

	TodoServiceImpl service = TodoServiceImpl.getInstance();
	TodoDao todoDao = TodoDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	MemberService memberService = MemberServiceImpl.getInstance();

	List<Key> todoKeyList = new ArrayList<Key>();

	
	/**
	 * updateTodoStatusのテスト.
	 * @throws Exception
	 */
	@Test
	public void testUpdateTodoStatus() throws Exception {
		createTestData();

		String keyString = Datastore.keyToString(todoKeyList.get(0));
		TodoForm form = service.get(keyString, "hoge@hige.hage");
		form.setTodoStatus(TodoStatus.finish.getCode());
		
		service.updateTodoStatus(form, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		form = service.get(keyString, "hoge@hige.hage");
		assertThat(form.todoStatus, is(TodoStatus.finish.getCode()));
		
		
		//他のユーザが更新してきた場合
		try {
			service.updateTodoStatus(form, "hoge2@hige.hage");
			fail();
		} catch(ConcurrentModificationException e){}
		
	}
	
	/**
	 * getDetailのテスト.
	 * @throws Exception
	 */
	@Test
	public void testGetDetail() throws Exception {
		createTestData();
		
		//該当レコードなし
		TodoDetailForm form = service.getDetail("", "hoge@hige.hage");
		assertThat(form, is(nullValue()));
		
	}
	
	/**
	 * putCommentのテスト.
	 */
	@Test
	public void testPutComment() throws Exception {
		createTestData();
		
		//ステータス変更なしのコメント追加
		TodoCommentForm form = new TodoCommentForm();
		form.keyToString = Datastore.keyToString(todoKeyList.get(0));
		form.status = TodoStatus.nothing.getCode();
		form.comment = "コメントだったりする";
		service.putComment(form, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		TodoDetailForm actualForm = service.getDetail(form.keyToString, "hoge@hige.hage");
		assertThat(actualForm.commentList.size(), is(1));
		
		//ステータス変更有りのコメント追加
		form = new TodoCommentForm();
		form.keyToString = Datastore.keyToString(todoKeyList.get(0));
		form.status = TodoStatus.finish.getCode();
		form.comment = "終了したのよ";
		form.versionNo = actualForm.form.versionNo;
		service.putComment(form, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		actualForm = service.getDetail(form.keyToString, "hoge@hige.hage");
		assertThat(actualForm.commentList.size(), is(2));
		
		//ステータス変更有りだが、該当データが存在しない場合
		actualForm = service.getDetail(form.keyToString, "hoge@hige.hage");
		form = new TodoCommentForm();
		form.keyToString = Datastore.keyToString(todoKeyList.get(0));
		form.status = TodoStatus.nothing.getCode();
		form.comment = "差し戻し";
		form.versionNo = actualForm.form.versionNo;
		try {
			//登録したメンバー以外のコメント追加はできない
			service.putComment(form, "hoge123@hige.hage");
			fail();
		} catch (ConcurrentModificationException e) {}

		
		//ステータス変更有りだが、親のバージョン不正の場合
		form = new TodoCommentForm();
		form.keyToString = Datastore.keyToString(todoKeyList.get(0));
		form.status = TodoStatus.nothing.getCode();
		form.comment = "差し戻し";
		form.versionNo = "-1";
		try {
			service.putComment(form, "hoge@hige.hage");
			fail();
		} catch (ConcurrentModificationException e) {}
		
		//コメントのdelete
		actualForm = service.getDetail(form.keyToString, "hoge@hige.hage");
		String deleteKeyString = actualForm.commentList.get(0).getModel().getKeyToString();
		Long deleteVersion = actualForm.commentList.get(0).getModel().getVersion();
		service.deleteComment(actualForm.form.keyToString, 
				deleteKeyString, 
				deleteVersion, 
				"hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		actualForm = service.getDetail(form.keyToString, "hoge@hige.hage");
		assertThat(actualForm.commentList.size(), is(1));

		//存在しないレコードの削除
		try {
			service.deleteComment(actualForm.form.keyToString, 
					deleteKeyString, 
					deleteVersion, 
					"hoge2@hige.hage");
		} catch(ConcurrentModificationException e) {}
	}
	
	/**
	 * get/putのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetAndPut() throws Exception {
		createTestData();
		
		TodoDao.Param param = new TodoDao.Param();
		String memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		List<TodoModel> todoList = todoDao.getList(param);
		assertThat(todoList.size(), is(6));

		
		//新規の場合
		TodoForm actual = service.get("", "hoge@hige.hage");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getTodoStatus(), is(nullValue()));
		assertThat(actual.getTitle(), is(nullValue()));
		assertThat(actual.getContent(), is(nullValue()));
		assertThat(actual.getPeriod(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//登録する
		actual = new TodoForm();
		actual.keyToString = null;
		actual.todoStatus = TodoStatus.finish.getCode();
		actual.title = "新規登録タイトル";
		actual.tag = "ほげ,ひげ,  はげ ,,さ る";
		actual.content = "詳細情報";
		actual.period = "20150101";
		
		service.put(actual, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		todoList = todoDao.getList(param);
		assertThat(todoList.size(), is(7));
		
		//存在するものを取得
		String keyToString = Datastore.keyToString(todoList.get(6).getKey());
		actual = service.get(keyToString, "hoge@hige.hage");
		assertThat(actual.getKeyToString(), is(keyToString));
		assertThat(actual.getTodoStatus(), is(TodoStatus.finish.getCode()));
		assertThat(actual.getTitle(), is("新規登録タイトル"));
		assertThat(actual.getTag(), is("ほげ,ひげ,  はげ ,,さ る"));
		assertThat(actual.getContent(), is("詳細情報"));
		assertThat(actual.getPeriod(), is("20150101"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//更新
		actual.todoStatus = "4989";
		actual.title = "新規登録タイトル1";
		actual.tag = "";
		actual.content = "詳細情報2";
		actual.period = "20150103";
		
		service.put(actual, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		actual = service.get(keyToString, "hoge@hige.hage");
		assertThat(actual.getKeyToString(), is(keyToString));
		assertThat(actual.getTodoStatus(), is(TodoStatus.nothing.getCode()));
		assertThat(actual.getTitle(), is("新規登録タイトル1"));
		assertThat(actual.getTag(), is(""));
		assertThat(actual.getContent(), is("詳細情報2"));
		assertThat(actual.getPeriod(), is("20150103"));
		assertThat(actual.getVersionNo(), is("2"));
		
		//メールアドレスが不正
		actual = service.get(keyToString, "hoge2@hige.hage");
		assertThat(actual.getKeyToString(), is(nullValue()));
		
		//更新時、メールアドレスが不正
		actual = service.get(keyToString, "hoge@hige.hage");
		try {
			service.put(actual, "hoge2@hige.hage");
			fail();
		} catch (ConcurrentModificationException e) {}
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//削除時、メールアドレスが不正
		try {
			service.delete(actual, "hoge2@hige.hage");
			fail();
		} catch (ConcurrentModificationException e) {}
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//削除成功
		service.delete(actual, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		todoList = todoDao.getList(param);
		assertThat(todoList.size(), is(6));

	}
	
	/**
	 * getListのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetList() throws Exception {
		createTestData();
		
		TodoDao.Param param = new TodoDao.Param();
		String memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		List<TodoModelEx> todoExList = service.getList(param, false);
		assertThat(todoExList.size(), is(6));
		
		assertThat(todoExList.get(0).getModel().getKey(), is(todoKeyList.get(0)));
		assertThat(todoExList.get(1).getModel().getKey(), is(todoKeyList.get(1)));
		assertThat(todoExList.get(2).getModel().getKey(), is(todoKeyList.get(2)));
		assertThat(todoExList.get(3).getModel().getKey(), is(todoKeyList.get(3)));
		assertThat(todoExList.get(3).getPeriod(), is(""));
		assertThat(todoExList.get(4).getModel().getKey(), is(todoKeyList.get(4)));
		assertThat(todoExList.get(4).getPeriod(), is("20120105"));
		assertThat(todoExList.get(5).getModel().getKey(), is(todoKeyList.get(5)));

	}

	/**
	 * getListのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetList2() throws Exception {
		createTestData();
		
		TodoDao.Param param = new TodoDao.Param();
		String memberKeyString = memberService.getKeyString("hoge2@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.limit = 1;
		List<TodoModelEx> todoExList = service.getList(param, true);
		assertThat(todoExList.size(), is(1));
		assertThat(todoExList.get(0).getModel().getKey(), is(todoKeyList.get(6)));
	}
	
	
	/**
	 * createPeriodStatusのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testCreatePeriodStatus() throws Exception {
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		Date today = sdf.parse("20100101");
		Date targetDate = sdf.parse("20100101");
		PeriodStatus actual = service.createPeriodStatus(today, targetDate, TodoStatus.nothing);
		assertThat(actual, is(PeriodStatus.today));
		
		targetDate = sdf.parse("20091231");
		actual = service.createPeriodStatus(today, targetDate, TodoStatus.nothing);
		assertThat(actual, is(PeriodStatus.periodDate));
		
		targetDate = sdf.parse("20100102");
		actual = service.createPeriodStatus(today, targetDate, TodoStatus.nothing);
		assertThat(actual, is(nullValue()));
		
	}
	
	
	/**
	 * テストデータ作成.
	 * @throws Exception 例外
	 */
	private void createTestData() throws Exception {
		
		TodoModel model = null;
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		for(int i = 0; i < 10; i++) {
			
			model = new TodoModel();
			
			String targetEmail;
			if(0 <= i && i <= 5) {
				targetEmail = "hoge@hige.hage";
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
			
			model.setStatus(status);
			Key memberKey = Datastore.createKey(MemberModel.class, targetEmail);
			model.setCreateMemberKey(memberKey);
			
			if(i == 0 || i == 2) {
				model.setTitle("Like用件名" + i);
			} else {
				model.setTitle("件名1" + i);
			}
			
			model.setContent(new Text("詳細" + i));
			model.setPeriod(period);
			todoDao.put(model);
			todoKeyList.add(model.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		MemberModel memberModel = new MemberModel();
		memberModel.createKey("hoge@hige.hage");
		memberDao.put(memberModel);
		
		memberModel = new MemberModel();
		memberModel.createKey("hoge2@hige.hage");
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
