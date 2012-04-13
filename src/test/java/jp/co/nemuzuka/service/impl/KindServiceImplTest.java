package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
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

	KindServiceImpl service = new KindServiceImpl();
	KindDao kindDao = new KindDao();
	ProjectDao projectDao = new ProjectDao();
	
	List<Key> kindKeyList;
	Key projectKey;

	/**
	 * getのテスト.
	 */
	@Test
	public void testGet() {
		createTestData();
		
		//該当データが存在する場合
		String keyString = Datastore.keyToString(kindKeyList.get(0));
		KindForm actual = service.get(keyString);
		assertThat(actual.getKeyToString(), is(keyString));
		assertThat(actual.getKindName(), is("種別_0"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//新規の場合
		actual = service.get("");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getKindName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		
		//データを削除する
		kindDao.delete(kindKeyList.get(2));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//keyを指定したが存在しない場合
		keyString = Datastore.keyToString(kindKeyList.get(2));
		actual = service.get(keyString);
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getKindName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
	}

	/**
	 * putのテスト.
	 */
	@Test
	public void testPut() {
		createTestData();
		
		String projectKeyToString = Datastore.keyToString(projectKey);
		
		//
		//新規
		//
		
		KindForm form = new KindForm();
		form.kindName = "追加種別";
		service.put(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//1件追加されていること
		List<KindModel> allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(4));
		KindModel actual = allList.get(3);
		assertThat(actual.getKindName(), is("追加種別"));
		assertThat(actual.getProjectKey(), is(projectKey));
		assertThat(actual.getSortNum(), is(Long.MAX_VALUE));
		assertThat(actual.getVersion(), is(1L));
		
		//
		//更新
		//
		String keyString = Datastore.keyToString(kindKeyList.get(0));
		form = service.get(keyString);
		form.setKindName("変更後種別");
		
		service.put(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		allList = service.getAllList(projectKeyToString);
		actual = allList.get(0);
		assertThat(actual.getKindName(), is("変更後種別"));
		assertThat(actual.getProjectKey(), is(projectKey));
		assertThat(actual.getSortNum(), is(Long.MAX_VALUE));
		assertThat(actual.getVersion(), is(2L));
		
		//
		//該当レコードが存在しない
		//
		keyString = Datastore.keyToString(kindKeyList.get(1));
		form = service.get(keyString);
		form.setKindName("バージョン不正");
		form.setVersionNo("-1");
		try {
			service.put(form, projectKeyToString);
			fail();
		} catch (ConcurrentModificationException e) {}
	}
	
	/**
	 * deleteのテスト.
	 */
	@Test
	public void testDelete() {
		createTestData();
		
		String projectKeyToString = Datastore.keyToString(projectKey);
		String keyString = Datastore.keyToString(kindKeyList.get(0));
		KindForm form = service.get(keyString);
		service.delete(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		form = service.get(keyString);
		assertThat(form.keyToString, is(nullValue()));
		
		//
		//バージョン違い
		//
		keyString = Datastore.keyToString(kindKeyList.get(1));
		form = service.get(keyString);
		form.versionNo = "-1";
		try {
			service.delete(form, projectKeyToString);
			fail();
		} catch (ConcurrentModificationException e) {}
	}
	
	
	/**
	 * updateSortNumのテスト.
	 */
	@Test
	public void testUpdateSortNum() {
		createTestData();
		
		String projectKeyToString = Datastore.keyToString(projectKey);
		String[] sortedKeyToString = new String[]{
				Datastore.keyToString(kindKeyList.get(0)),
				Datastore.keyToString(kindKeyList.get(2)),
				Datastore.keyToString(kindKeyList.get(1))
		};
		service.updateSortNum(sortedKeyToString, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		//順番が変わっていること
		List<KindModel> allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(3));
		assertThat(allList.get(0).getKey(), is(kindKeyList.get(0)));
		assertThat(allList.get(1).getKey(), is(kindKeyList.get(2)));
		assertThat(allList.get(2).getKey(), is(kindKeyList.get(1)));

		//
		//サイズ0の配列を渡されてもエラーにならないこと
		//
		service.updateSortNum(new String[0], projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		
		//削除された状態で呼び出してもエラーにならないこと
		KindForm form = service.get(Datastore.keyToString(kindKeyList.get(0)));
		service.delete(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		sortedKeyToString = new String[]{
				Datastore.keyToString(kindKeyList.get(0)),
				Datastore.keyToString(kindKeyList.get(2)),
				Datastore.keyToString(kindKeyList.get(1))
		};
		service.updateSortNum(sortedKeyToString, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		//順番が変わっていること
		allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(2));
		assertThat(allList.get(0).getKey(), is(kindKeyList.get(2)));
		assertThat(allList.get(1).getKey(), is(kindKeyList.get(1)));

	}
	
	
	/**
	 * テストデータ作成.
	 * プロジェクト1件
	 * 種別3件を作成します。
	 */
	private void createTestData() {
		
		ProjectModel model = new ProjectModel();
		model.setProjectId("test_project");
		model.setProjectName("テストプロジェクト");
		model.setProjectSummary(new Text("テスト用プロジェクトですってば"));
		projectDao.put(model);
		projectKey = model.getKey();
		
		kindKeyList = new ArrayList<Key>();
		for(int i = 0; i < 3; i++) {
			KindModel kindModel = new KindModel();
			kindModel.setKindName("種別_" + i);
			kindModel.setProjectKey(projectKey);
			kindModel.setSortNum(Long.MAX_VALUE);
			kindDao.put(kindModel);
			kindKeyList.add(kindModel.getKey());
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
