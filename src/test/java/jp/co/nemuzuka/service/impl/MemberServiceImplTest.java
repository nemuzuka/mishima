package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;

/**
 * MemberServiceImplのテストクラス.
 * @author kazumune
 */
public class MemberServiceImplTest extends AppEngineTestCase4HRD {

	MemberServiceImpl sevice = new MemberServiceImpl();
	MemberDao dao = new MemberDao();
	
	/**
	 * checkAndCreateAdminMemberのテスト.
	 * 該当データが存在しない場合、新しく１レコード追加される
	 */
	@Test
	public void testCheckAndCreateAdminMember() {
		
		assertThat(dao.getAllList().size(), is(0));
		
		sevice.checkAndCreateAdminMember("hoge@hage.hige", "ニックネーム");
		GlobalTransaction.transaction.get().commit();
		
		List<MemberModel> actualList = dao.getAllList();
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
		dao.put(model);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		sevice.checkAndCreateAdminMember("hoge@hige.hage", "にっくねーむ");
		GlobalTransaction.transaction.get().commit();
		
		List<MemberModel> actualList = dao.getAllList();
		assertThat(actualList.size(), is(1));
		MemberModel actual = actualList.get(0);
		assertThat(actual.getMail(), is("hoge@hige.hage"));
		assertThat(actual.getName(), is("富樫　義博"));
		assertThat(actual.getAuthority(), is(Authority.normal));
		assertThat(actual.getVersion(), is(11L));
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
