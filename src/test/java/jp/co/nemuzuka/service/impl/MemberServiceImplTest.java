package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.exception.AlreadyExistKeyException;
import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.form.PersonForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * MemberServiceImplのテストクラス.
 * @author kazumune
 */
public class MemberServiceImplTest extends AppEngineTestCase4HRD {

	MemberServiceImpl service = MemberServiceImpl.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	
	List<Key> memberKeyList;
	
	/**
	 * checkAndCreateAdminMemberのテスト.
	 * 該当データが存在しない場合、新しく１レコード追加される
	 */
	@Test
	public void testCheckAndCreateAdminMember() {
		
		assertThat(memberDao.getAllList().size(), is(0));
		
		service.checkAndCreateAdminMember("hoge@hage.hige", "ニックネーム");
		GlobalTransaction.transaction.get().commit();
		
		List<MemberModel> actualList = memberDao.getAllList();
		assertThat(actualList.size(), is(1));
		MemberModel actual = actualList.get(0);
		assertThat(actual.getMail(), is("hoge@hage.hige"));
		assertThat(actual.getName(), is("ニックネーム"));
		assertThat(actual.getAuthority(), is(Authority.admin));
		assertThat(actual.getVersion(), is(1L));
	}
	
	/**
	 * checkAndCreateAdminMemberのテスト.
	 * すでに存在するので、更新されない
	 */
	@Test
	public void testCheckAndCreateAdminMember2() {
		
		//テスト用のデータを作成
		MemberModel model = new MemberModel();
		model.createKey("hoge@hige.hage");
		model.setName("富樫　義博");
		model.setAuthority(Authority.normal);
		model.setVersion(10L);
		memberDao.put(model);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		service.checkAndCreateAdminMember("hoge@hige.hage", "にっくねーむ");
		GlobalTransaction.transaction.get().commit();
		
		List<MemberModel> actualList = memberDao.getAllList();
		assertThat(actualList.size(), is(1));
		MemberModel actual = actualList.get(0);
		assertThat(actual.getMail(), is("hoge@hige.hage"));
		assertThat(actual.getName(), is("富樫　義博"));
		assertThat(actual.getAuthority(), is(Authority.normal));
		assertThat(actual.getVersion(), is(11L));
	}
	
	/**
	 * getのテスト.
	 */
	@Test
	public void testGet() {
		createInitData();

		//新規の場合
		MemberForm actual = service.get("");
		assertThat(actual.keyToString, is(nullValue()));
		assertThat(actual.mail, is(nullValue()));
		assertThat(actual.name, is(nullValue()));
		assertThat(actual.authority, is(Authority.normal.getCode()));
		assertThat(actual.versionNo, is(nullValue()));
		
		//更新の場合
		String keyString = Datastore.keyToString(memberKeyList.get(3));
		actual = service.get(keyString);
		assertThat(actual.keyToString, is(keyString));
		assertThat(actual.mail, is("mail3@gmail.com"));
		assertThat(actual.name, is("name3"));
		assertThat(actual.authority, is(Authority.normal.getCode()));
		assertThat(actual.versionNo, is("1"));
		
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//存在しない場合
		MemberModel model = new MemberModel();
		model.createKey("noregist@gmail.com");
		keyString = Datastore.keyToString(model.getKey());
		actual = service.get(keyString);
		assertThat(actual.keyToString, is(nullValue()));
		assertThat(actual.mail, is(nullValue()));
		assertThat(actual.name, is(nullValue()));
		assertThat(actual.authority, is(Authority.normal.getCode()));
		assertThat(actual.versionNo, is(nullValue()));
	}
	
	/**
	 * putとdeleteのテスト.
	 */
	@Test
	public void testPutAndDelete() {
		createInitData();
		
		String keyString = testPut();
		testDelete(keyString);
	}
	
	/**
	 * getAllListのテスト
	 */
	@Test
	public void testGetAllList() {
		createInitData();
		
		List<MemberModel> actualList = service.getAllList();
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getAuthorityLabel(), is("管理者"));
		assertThat(actualList.get(1).getAuthorityLabel(), is("一般"));
	}
	
	
	/**
	 * deleteのテスト.
	 * @param keyString 対象Key文字列
	 */
	private void testDelete(String keyString) {
		
		//バージョン違い
		MemberForm form = service.get(keyString);
		form.setVersionNo("-1");
		try {
			service.delete(form);
			fail();
		} catch (ConcurrentModificationException e) {}
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//削除
		form = service.get(keyString);
		service.delete(form);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		List<MemberModel> list = service.getList("name123", null);
		assertThat(list.size(), is(0));
	}

	/**
	 * putのテスト.
	 * @return 登録Key文字列
	 */
	private String testPut() {
		
		MemberForm form = service.get("");
		form.setMail("mail0123@hige.hage");
		form.setName("name123");
		form.setAuthority(Authority.admin.getCode());
		service.put(form);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//登録されていることの確認
		List<MemberModel> list = service.getList("name123", null);
		assertThat(list.size(), is(1));
		assertThat(list.get(0).getMail(), is("mail0123@hige.hage"));
		assertThat(list.get(0).getName(), is("name123"));
		assertThat(list.get(0).getAuthority(), is(Authority.admin));
		String keyString = Datastore.keyToString(list.get(0).getKey());
		
		//更新
		form = service.get(keyString);
		form.setMail("mail0123_2@hige.hage");
		form.setName("name123456");
		form.setAuthority(Authority.normal.getCode());
		service.put(form);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		list = service.getList(null, "mail0123@hige.hage");
		assertThat(list.size(), is(1));
		//メールアドレスは更新されない
		assertThat(list.get(0).getMail(), is("mail0123@hige.hage"));
		assertThat(list.get(0).getName(), is("name123456"));
		assertThat(list.get(0).getAuthority(), is(Authority.normal));

		//更新(その2：存在しないユーザ権限を設定された)
		form = service.get(keyString);
		form.setAuthority("not_anthority!");
		service.put(form);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		list = service.getList(null, "mail0123@hige.hage");
		assertThat(list.size(), is(1));
		//一般として登録される
		assertThat(list.get(0).getAuthority(), is(Authority.normal));
		
		
		//バージョン違い
		form = service.get(keyString);
		form.setVersionNo("-1");
		try {
			service.put(form);
			fail();
		} catch(ConcurrentModificationException e) {}
		
		//既に同じメールアドレスが登録されている
		form = service.get("");
		form.setMail("mail0123@hige.hage");
		form.setName("登録できないデータ！");
		form.setAuthority(Authority.admin.getCode());
		try {
			service.put(form);
			fail();
		} catch(AlreadyExistKeyException e) {}
		
		return keyString;
	}
	
	/**
	 * getとPutのテスト.
	 */
	@Test
	public void testGetPut() {
		createInitData();

		//データが存在しない場合
		PersonForm actual = service.getPersonForm("noregist@gmail.com");
		assertThat(actual.keyToString, is(""));
		assertThat(actual.name, is(""));
		assertThat(actual.memo, is(""));
		assertThat(actual.versionNo, is(""));

		
		//データが存在する場合
		actual = service.getPersonForm("mail0@gmail.com");
		assertThat(actual.keyToString, is(Datastore.keyToString(memberKeyList.get(0))));
		assertThat(actual.name, is("name0"));
		assertThat(actual.memo, is("メモですよん"));
		assertThat(actual.versionNo, is("1"));
		
		//データ更新
		actual = service.getPersonForm("mail2@gmail.com");
		actual.name = "変更後ニックネーム";
		actual.memo = "変更後メモンガ";
		service.put(actual);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		actual = service.getPersonForm("mail2@gmail.com");
		assertThat(actual.keyToString, is(Datastore.keyToString(memberKeyList.get(2))));
		assertThat(actual.name, is("変更後ニックネーム"));
		assertThat(actual.memo, is("変更後メモンガ"));
		assertThat(actual.versionNo, is("2"));
		
		//データが存在しない
		actual = service.getPersonForm("mail2@gmail.com");
		actual.versionNo = "-1";
		try {
			service.put(actual);
			fail();
		} catch(ConcurrentModificationException e) {}
	}

	
	/**
	 * 事前データ作成.
	 * ユーザを4人作成します。
	 */
	private void createInitData() {
		memberKeyList = new ArrayList<Key>();
		for(int i = 0; i < 4; i++) {
			MemberModel model = new MemberModel();
			model.createKey("mail" + i + "@gmail.com");
			model.setName("name" + i);
			Authority authority = null;
			if(i == 0 || i == 2) {
				authority = Authority.admin;
			} else {
				authority = Authority.normal;
			}
			model.setAuthority(authority);
			if(i == 0) {
				model.setMemo(new Text("メモですよん"));
			}
			memberDao.put(model);
			memberKeyList.add(model.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
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
