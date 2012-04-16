package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.PriorityDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.form.PriorityForm;
import jp.co.nemuzuka.model.PriorityModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * PriorityServiceImplのテストクラス.
 * @author kazumune
 */
public class PriorityServiceImplTest extends AppEngineTestCase4HRD {

	PriorityServiceImpl service = new PriorityServiceImpl();
	PriorityDao priorityDao = new PriorityDao();
	ProjectDao projectDao = new ProjectDao();
	
	Key projectKey;

	/**
	 * get/putのテスト.
	 */
	@Test
	public void testGetAndPut() {
		createTestData();
		
		//該当データが存在しない場合
		String projectKeyString = Datastore.keyToString(projectKey);
		PriorityForm actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getPriorityName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//該当データが存在しない場合
		actual = service.get("");
		assertThat(actual.getKeyToString(), is(nullValue()));
		assertThat(actual.getPriorityName(), is(nullValue()));
		assertThat(actual.getVersionNo(), is(nullValue()));
		
		//登録する
		actual.keyToString = null;
		actual.priorityName = "高\r\n中\r低";
		service.put(actual, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//登録後、取得
		actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), is(Datastore.keyToString(Datastore.createKey(PriorityModel.class, projectKeyString))));
		assertThat(actual.getPriorityName(), is("高\r\n中\r低"));
		assertThat(actual.getVersionNo(), is("1"));
		
		//更新
		actual.priorityName = "おほほー";
		service.put(actual, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//更新後、取得
		actual = service.get(projectKeyString);
		assertThat(actual.getKeyToString(), 
				is(Datastore.keyToString(Datastore.createKey(PriorityModel.class, projectKeyString))));
		assertThat(actual.getPriorityName(), is("おほほー"));
		assertThat(actual.getVersionNo(), is("2"));

		//バージョン不正
		actual.versionNo = "-1";
		try {
			service.put(actual, projectKeyString);
			fail();
		} catch (ConcurrentModificationException e) {}
	}
	
	/**
	 * getListのテスト.
	 */
	@Test
	public void testGetList() {
		createTestData();

		//テスト用データ作成
		String projectKeyString = Datastore.keyToString(projectKey);
		PriorityForm form = new PriorityForm();
		form.priorityName = "高\r\n中\r低";
		service.put(form, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		List<LabelValueBean> actualList = service.getList(projectKeyString);
		assertThat(actualList.size(), is(4));
		assertThat(actualList.get(0).getLabel(), is(""));
		assertThat(actualList.get(0).getValue(), is(""));
		assertThat(actualList.get(1).getLabel(), is("高"));
		assertThat(actualList.get(1).getValue(), is("高"));
		assertThat(actualList.get(2).getLabel(), is("中"));
		assertThat(actualList.get(2).getValue(), is("中"));
		assertThat(actualList.get(3).getLabel(), is("低"));
		assertThat(actualList.get(3).getValue(), is("低"));
	}

	/**
	 * テストデータ作成.
	 * プロジェクト1件を作成します。
	 */
	private void createTestData() {
		
		ProjectModel model = new ProjectModel();
		model.setProjectId("test_project");
		model.setProjectName("テストプロジェクト");
		model.setProjectSummary(new Text("テスト用プロジェクトですってば"));
		projectDao.put(model);
		projectKey = model.getKey();
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
