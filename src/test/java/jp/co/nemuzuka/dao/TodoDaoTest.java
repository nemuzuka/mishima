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

import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * TodoDaoのテストクラス.
 * @author kazumune
 */
public class TodoDaoTest extends AppEngineTestCase4HRD {

	TodoDao todoDao = TodoDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	MemberService memberService = MemberServiceImpl.getInstance();
	List<Key> todoKeyList = new ArrayList<Key>();
	
	/**
	 * getListのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetList() throws Exception {
		createTestData();
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");

		//メールアドレスが違うので取得できない
		TodoDao.Param param = new TodoDao.Param();
		String memberKeyString = memberService.getKeyString("hoge3@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		List<TodoModel> actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(0));
		
		//取得可能(検索条件未設定)
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(6));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(1)));
		assertThat(actualList.get(2).getKey(), is(todoKeyList.get(2)));
		assertThat(actualList.get(3).getKey(), is(todoKeyList.get(3)));
		assertThat(actualList.get(4).getKey(), is(todoKeyList.get(4)));
		assertThat(actualList.get(5).getKey(), is(todoKeyList.get(5)));
		
		//limit指定
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.limit = 3;
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(3));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(1)));
		assertThat(actualList.get(2).getKey(), is(todoKeyList.get(2)));
		
		//件名指定
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.title = "Like用件名";
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(2)));
		
		
		//タグ名指定
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.tag = "名";
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(1)));
		
		//期限From指定
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.fromPeriod = ConvertUtils.toDate("20120106", sdf);
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(2)));

		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.fromPeriod = ConvertUtils.toDate("20120105", sdf);
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(3));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(2)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(4)));
		assertThat(actualList.get(2).getKey(), is(todoKeyList.get(5)));

		//期限To設定
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.toPeriod = ConvertUtils.toDate("20120105", sdf);
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(4)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(5)));
		
		//期間From～To設定
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.fromPeriod = ConvertUtils.toDate("20120106", sdf);
		param.toPeriod = ConvertUtils.toDate("20120106", sdf);
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(2)));

		
		//ステータスの設定
		String[] status = new String[]{
				TodoStatus.finish.getCode(),
				TodoStatus.doing.getCode()};
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.status = status;
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(3));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(3)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(4)));
		assertThat(actualList.get(2).getKey(), is(todoKeyList.get(5)));
		
		//未完了を選択された場合
		status = new String[]{TodoStatus.NO_FINISH};
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.status = status;
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(0)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(1)));
		assertThat(actualList.get(2).getKey(), is(todoKeyList.get(2)));
		assertThat(actualList.get(3).getKey(), is(todoKeyList.get(5)));
		
		//存在しないステータスコードを指定された
		status = new String[]{"4989"};
		param = new TodoDao.Param();
		memberKeyString = memberService.getKeyString("hoge@hige.hage");
		param.targetMemberKeyString = memberKeyString;
		param.status = status;
		actualList = todoDao.getList(param);
		assertThat(actualList.size(), is(6));
	}
	
	/**
	 * getDashbordListのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGetDashbordList() throws Exception {
		createTestData();

		String memberKeyString = memberService.getKeyString("hoge2@hige.hage");
		List<TodoModel> actualList = todoDao.getDashbordList(20, memberKeyString);
		assertThat(actualList.size(), is(3));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(6)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(8)));
		assertThat(actualList.get(2).getKey(), is(todoKeyList.get(7)));
		
		//期限ありのものだけで取得件数を満たした場合
		memberKeyString = memberService.getKeyString("hoge2@hige.hage");
		actualList = todoDao.getDashbordList(2, memberKeyString);
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(6)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(8)));

		//期限ありのもの＋期限なしのもので取得件数を満たした場合
		memberKeyString = memberService.getKeyString("hoge2@hige.hage");
		actualList = todoDao.getDashbordList(3, memberKeyString);
		assertThat(actualList.size(), is(3));
		assertThat(actualList.get(0).getKey(), is(todoKeyList.get(6)));
		assertThat(actualList.get(1).getKey(), is(todoKeyList.get(8)));
		assertThat(actualList.get(2).getKey(), is(todoKeyList.get(7)));
		
		//該当レコードが存在しない場合
		memberKeyString = memberService.getKeyString("hoge3@hige.hage");
		actualList = todoDao.getDashbordList(4, memberKeyString);
		assertThat(actualList.size(), is(0));
		
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
			
			if(i == 0) {
				model.setTag("タグ名");
			} else if(i == 1) {
				model.setTag("名");
			} else {
				model.setTag(",さる名");
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
