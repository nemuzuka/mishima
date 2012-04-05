package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * ProjectServiceImplのテストクラス.
 * @author kazumune
 */
public class ProjectServiceImplTest extends AppEngineTestCase4HRD {

	ProjectServiceImpl sevice = new ProjectServiceImpl();
	ProjectDao dao = new ProjectDao();

	/**
	 * createUserProjectListのテスト.
	 */
	@Test
	public void testCreateUserProjectList() {
		assertThat(dao.getAllList().size(), is(0));
		
		Set<Key> keys = createTestData(3);
		List<LabelValueBean> actual = sevice.createUserProjectList(keys);
		assertThat(actual.size(), is(4));
		
		assertThat(actual.get(0).getLabel(), is("--プロジェクトを選択--"));
		assertThat(actual.get(0).getValue(), is(""));

		assertThat(actual.get(1).getLabel(), is("name0"));
		assertThat(actual.get(2).getLabel(), is("name1"));
		assertThat(actual.get(3).getLabel(), is("name2"));

	}
	
	/**
	 * getAllListのテスト.
	 */
	@Test
	public void testGetAllList() {
		assertThat(dao.getAllList().size(), is(0));
		createTestData(3);
		
		List<ProjectModel> actual = sevice.getAllList();
		assertThat(actual.size(), is(3));
		
		assertThat(actual.get(0).getKey().getId(), is(1L));
		assertThat(actual.get(1).getKey().getId(), is(2L));
		assertThat(actual.get(2).getKey().getId(), is(3L));
	}
	
	/**
	 * テストデータ作成.
	 * 作成件数 + 存在しないKeyを作成します。
	 * @param count 作成件数
	 * @return 作成件数+存在しないKeySet
	 */
	private Set<Key> createTestData(int count) {
		Set<Key> keys = new LinkedHashSet<Key>();
		for(int i = 0; i < count; i++) {
			ProjectModel model = new ProjectModel();
			model.setProjectId("project_" + i);
			model.setProjectName("name" + i);
			model.setProjectSummary("summary" + i);
			dao.put(model);
			keys.add(model.getKey());
		}
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		keys.add(Datastore.createKey(ProjectModel.class, -1L));
		try {
			Thread.sleep(2000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return keys;
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
