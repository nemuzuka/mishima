package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.VersionDao;
import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.model.VersionModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * VersionServiceImplのテストクラス.
 * @author kazumune
 */
public class VersionServiceImplTest extends AppEngineTestCase4HRD {

	VersionServiceImpl service = new VersionServiceImpl();
	VersionDao versionDao = new VersionDao();
	ProjectDao projectDao = new ProjectDao();
	
	List<Key> versionKeyList;
	Key projectKey;

	/**
	 * getのテスト.
	 */
	@Test
	public void testGet() {
		createTestData();
		
		//該当データが存在する場合
		String keyString = Datastore.keyToString(versionKeyList.get(0));
		VersionForm actual = service.get(keyString);
		assertThat(actual.getKeyToString(), is(keyString));
		assertThat(actual.getVersionName(), is("1.2.3.0"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//新規の場合
		actual = service.get("");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getVersionName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		
		//データを削除する
		versionDao.delete(versionKeyList.get(2));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//keyを指定したが存在しない場合
		keyString = Datastore.keyToString(versionKeyList.get(2));
		actual = service.get(keyString);
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getVersionName(), is(nullValue()));
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
		
		VersionForm form = new VersionForm();
		form.versionName = "追加バージョン";
		service.put(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//1件追加されていること
		List<VersionModel> allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(4));
		VersionModel actual = allList.get(3);
		assertThat(actual.getVersionName(), is("追加バージョン"));
		assertThat(actual.getProjectKey(), is(projectKey));
		assertThat(actual.getSortNum(), is(Long.MAX_VALUE));
		assertThat(actual.getVersion(), is(1L));
		
		//
		//更新
		//
		String keyString = Datastore.keyToString(versionKeyList.get(0));
		form = service.get(keyString);
		form.setVersionName("変更後バージョン");
		
		service.put(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		allList = service.getAllList(projectKeyToString);
		actual = allList.get(0);
		assertThat(actual.getVersionName(), is("変更後バージョン"));
		assertThat(actual.getProjectKey(), is(projectKey));
		assertThat(actual.getSortNum(), is(Long.MAX_VALUE));
		assertThat(actual.getVersion(), is(2L));
		
		//
		//該当レコードが存在しない
		//
		keyString = Datastore.keyToString(versionKeyList.get(1));
		form = service.get(keyString);
		form.setVersionName("バージョン不正");
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
		String keyString = Datastore.keyToString(versionKeyList.get(0));
		VersionForm form = service.get(keyString);
		service.delete(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		form = service.get(keyString);
		assertThat(form.keyToString, is(nullValue()));
		
		//
		//バージョン違い
		//
		keyString = Datastore.keyToString(versionKeyList.get(1));
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
				Datastore.keyToString(versionKeyList.get(0)),
				Datastore.keyToString(versionKeyList.get(2)),
				Datastore.keyToString(versionKeyList.get(1))
		};
		service.updateSortNum(sortedKeyToString, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		//順番が変わっていること
		List<VersionModel> allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(3));
		assertThat(allList.get(0).getKey(), is(versionKeyList.get(0)));
		assertThat(allList.get(1).getKey(), is(versionKeyList.get(2)));
		assertThat(allList.get(2).getKey(), is(versionKeyList.get(1)));

		//
		//サイズ0の配列を渡されてもエラーにならないこと
		//
		service.updateSortNum(new String[0], projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		
		//削除された状態で呼び出してもエラーにならないこと
		VersionForm form = service.get(Datastore.keyToString(versionKeyList.get(0)));
		service.delete(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		sortedKeyToString = new String[]{
				Datastore.keyToString(versionKeyList.get(0)),
				Datastore.keyToString(versionKeyList.get(2)),
				Datastore.keyToString(versionKeyList.get(1))
		};
		service.updateSortNum(sortedKeyToString, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		//順番が変わっていること
		allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(2));
		assertThat(allList.get(0).getKey(), is(versionKeyList.get(2)));
		assertThat(allList.get(1).getKey(), is(versionKeyList.get(1)));

	}
	
	
	/**
	 * テストデータ作成.
	 * プロジェクト1件
	 * バージョン3件を作成します。
	 */
	private void createTestData() {
		
		ProjectModel model = new ProjectModel();
		model.setProjectId("test_project");
		model.setProjectName("テストプロジェクト");
		model.setProjectSummary(new Text("テスト用プロジェクトですってば"));
		projectDao.put(model);
		projectKey = model.getKey();
		
		versionKeyList = new ArrayList<Key>();
		for(int i = 0; i < 3; i++) {
			VersionModel versionModel = new VersionModel();
			versionModel.setVersionName("1.2.3." + i);
			versionModel.setProjectKey(projectKey);
			versionModel.setSortNum(Long.MAX_VALUE);
			versionDao.put(versionModel);
			versionKeyList.add(versionModel.getKey());
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
