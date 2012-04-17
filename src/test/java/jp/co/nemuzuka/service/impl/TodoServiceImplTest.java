package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoModel;
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

	TodoServiceImpl service = new TodoServiceImpl();
	TodoDao todoDao = new TodoDao();
	List<Key> todoKeyList = new ArrayList<Key>();

	/**
	 * get/putのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetAndPut() throws Exception {
		createTestData();
		
		TodoDao.Param param = new TodoDao.Param();
		param.email = "hoge@hige.hage";
		List<TodoModel> todoList = todoDao.getList(param);
		assertThat(todoList.size(), is(6));

		
		//新規の場合
		TodoForm actual = service.get("", "hoge@hige.hage");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getStatus(), is(nullValue()));
		assertThat(actual.getTitle(), is(nullValue()));
		assertThat(actual.getContent(), is(nullValue()));
		assertThat(actual.getPeriod(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//登録する
		actual = new TodoForm();
		actual.keyToString = null;
		actual.status = TodoStatus.finish.getCode();
		actual.title = "新規登録タイトル";
		actual.content = "詳細情報";
		actual.period = "20150101";
		
		service.put(actual, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		param = new TodoDao.Param();
		param.email = "hoge@hige.hage";
		todoList = todoDao.getList(param);
		assertThat(todoList.size(), is(7));
		
		//存在するものを取得
		String keyToString = Datastore.keyToString(todoList.get(6).getKey());
		actual = service.get(keyToString, "hoge@hige.hage");
		assertThat(actual.getKeyToString(), is(keyToString));
		assertThat(actual.getStatus(), is(TodoStatus.finish.getCode()));
		assertThat(actual.getTitle(), is("新規登録タイトル"));
		assertThat(actual.getContent(), is("詳細情報"));
		assertThat(actual.getPeriod(), is("20150101"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//更新
		actual.status = "4989";
		actual.title = "新規登録タイトル1";
		actual.content = "詳細情報2";
		actual.period = "20150103";
		
		service.put(actual, "hoge@hige.hage");
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		actual = service.get(keyToString, "hoge@hige.hage");
		assertThat(actual.getKeyToString(), is(keyToString));
		assertThat(actual.getStatus(), is(TodoStatus.nothing.getCode()));
		assertThat(actual.getTitle(), is("新規登録タイトル1"));
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
		param.email = "hoge@hige.hage";
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
		param.email = "hoge@hige.hage";
		List<TodoModelEx> todoExList = service.getList(param);
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
