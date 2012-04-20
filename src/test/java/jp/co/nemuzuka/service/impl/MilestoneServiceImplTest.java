package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MilestoneDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.entity.MilestoneModelEx;
import jp.co.nemuzuka.form.MilestoneForm;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * MilestoneServiceImplのテストクラス.
 * @author kazumune
 */
public class MilestoneServiceImplTest extends AppEngineTestCase4HRD {

	MilestoneServiceImpl service = MilestoneServiceImpl.getInstance();
	MilestoneDao milestoneDao = MilestoneDao.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	
	List<Key> milestoneKeyList;
	Key projectKey;

	/**
	 * getのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testGet() throws Exception {
		createTestData();
		
		//該当データが存在する場合
		String keyString = Datastore.keyToString(milestoneKeyList.get(0));
		MilestoneForm actual = service.get(keyString);
		assertThat(actual.getKeyToString(), is(keyString));
		assertThat(actual.getMilestoneName(), is("マイルストン_0"));
		assertThat(actual.getStartDate(), is("20120401"));
		assertThat(actual.getEndDate(), is("20120430"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//新規の場合
		actual = service.get("");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getMilestoneName(), is(nullValue()));
		assertThat(actual.getStartDate(), is(nullValue()));
		assertThat(actual.getEndDate(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		
		//データを削除する
		milestoneDao.delete(milestoneKeyList.get(2));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//keyを指定したが存在しない場合
		keyString = Datastore.keyToString(milestoneKeyList.get(2));
		actual = service.get(keyString);
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getMilestoneName(), is(nullValue()));
		assertThat(actual.getStartDate(), is(nullValue()));
		assertThat(actual.getEndDate(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
	}

	/**
	 * putのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testPut() throws Exception {
		createTestData();
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		String projectKeyToString = Datastore.keyToString(projectKey);
		
		//
		//新規
		//
		
		MilestoneForm form = new MilestoneForm();
		form.milestoneName = "追加マイルストン";
		form.startDate = "20120501";
		form.endDate = "20120502";
		service.put(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//1件追加されていること
		List<MilestoneModelEx> allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(4));
		MilestoneModelEx actual = allList.get(3);
		assertThat(actual.model.getMilestoneName(), is("追加マイルストン"));
		assertThat(actual.model.getStartDate().getTime(), is(sdf.parse("20120501").getTime()));
		assertThat(actual.model.getEndDate().getTime(), is(sdf.parse("20120502").getTime()));
		assertThat(actual.model.getSortNum(), is(Long.MAX_VALUE));
		assertThat(actual.model.getVersion(), is(1L));
		assertThat(actual.getStartDate(), is("20120501"));
		assertThat(actual.getEndDate(), is("20120502"));
		
		//
		//更新
		//
		String keyString = Datastore.keyToString(milestoneKeyList.get(0));
		form = service.get(keyString);
		form.setMilestoneName("変更後マイルストン");
		form.startDate = "";
		form.endDate = "";
		
		service.put(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		allList = service.getAllList(projectKeyToString);
		actual = allList.get(0);
		assertThat(actual.model.getMilestoneName(), is("変更後マイルストン"));
		assertThat(actual.model.getStartDate(), is(nullValue()));
		assertThat(actual.model.getEndDate(), is(nullValue()));
		assertThat(actual.model.getSortNum(), is(Long.MAX_VALUE));
		assertThat(actual.model.getVersion(), is(2L));
		assertThat(actual.getStartDate(), is(""));
		assertThat(actual.getEndDate(), is(""));
		
		//
		//該当レコードが存在しない
		//
		keyString = Datastore.keyToString(milestoneKeyList.get(1));
		form = service.get(keyString);
		form.setMilestoneName("バージョン不正");
		form.setVersionNo("-1");
		try {
			service.put(form, projectKeyToString);
			fail();
		} catch (ConcurrentModificationException e) {}
	}
	
	/**
	 * deleteのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testDelete() throws Exception {
		createTestData();
		
		String projectKeyToString = Datastore.keyToString(projectKey);
		String keyString = Datastore.keyToString(milestoneKeyList.get(0));
		MilestoneForm form = service.get(keyString);
		service.delete(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		form = service.get(keyString);
		assertThat(form.keyToString, is(nullValue()));
		
		//
		//バージョン違い
		//
		keyString = Datastore.keyToString(milestoneKeyList.get(1));
		form = service.get(keyString);
		form.versionNo = "-1";
		try {
			service.delete(form, projectKeyToString);
			fail();
		} catch (ConcurrentModificationException e) {}
	}
	
	
	/**
	 * updateSortNumのテスト.
	 * @throws Exception 例外
	 */
	@Test
	public void testUpdateSortNum() throws Exception {
		createTestData();
		
		String projectKeyToString = Datastore.keyToString(projectKey);
		String[] sortedKeyToString = new String[]{
				Datastore.keyToString(milestoneKeyList.get(0)),
				Datastore.keyToString(milestoneKeyList.get(2)),
				Datastore.keyToString(milestoneKeyList.get(1))
		};
		service.updateSortNum(sortedKeyToString, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		//順番が変わっていること
		List<MilestoneModelEx> allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(3));
		assertThat(allList.get(0).model.getKey(), is(milestoneKeyList.get(0)));
		assertThat(allList.get(1).model.getKey(), is(milestoneKeyList.get(2)));
		assertThat(allList.get(2).model.getKey(), is(milestoneKeyList.get(1)));

		//
		//サイズ0の配列を渡されてもエラーにならないこと
		//
		service.updateSortNum(new String[0], projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		
		//削除された状態で呼び出してもエラーにならないこと
		MilestoneForm form = service.get(Datastore.keyToString(milestoneKeyList.get(0)));
		service.delete(form, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		sortedKeyToString = new String[]{
				Datastore.keyToString(milestoneKeyList.get(0)),
				Datastore.keyToString(milestoneKeyList.get(2)),
				Datastore.keyToString(milestoneKeyList.get(1))
		};
		service.updateSortNum(sortedKeyToString, projectKeyToString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		//順番が変わっていること
		allList = service.getAllList(projectKeyToString);
		assertThat(allList.size(), is(2));
		assertThat(allList.get(0).model.getKey(), is(milestoneKeyList.get(2)));
		assertThat(allList.get(1).model.getKey(), is(milestoneKeyList.get(1)));

	}
	
	
	/**
	 * テストデータ作成.
	 * プロジェクト1件
	 * マイルストーン3件を作成します。
	 * @throws ParseException 例外
	 */
	private void createTestData() throws ParseException {
		
		ProjectModel model = new ProjectModel();
		model.setProjectId("test_project");
		model.setProjectName("テストプロジェクト");
		model.setProjectSummary(new Text("テスト用プロジェクトですってば"));
		projectDao.put(model);
		projectKey = model.getKey();
		
		milestoneKeyList = new ArrayList<Key>();
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		for(int i = 0; i < 3; i++) {
			MilestoneModel milestoneModel = new MilestoneModel();
			milestoneModel.setMilestoneName("マイルストン_" + i);
			Date startDate = null;
			if(i == 0 || i == 2) {
				startDate = sdf.parse("20120401");
			}
			milestoneModel.setStartDate(startDate);
			Date endDate = null;
			if(i == 0 || i == 1) {
				endDate = sdf.parse("20120430");
			}
			milestoneModel.setEndDate(endDate);
			milestoneModel.setProjectKey(projectKey);
			milestoneModel.setSortNum(Long.MAX_VALUE);
			milestoneDao.put(milestoneModel);
			milestoneKeyList.add(milestoneModel.getKey());
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
