package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.ProjectMemberDao;
import jp.co.nemuzuka.entity.ProjectModelEx;
import jp.co.nemuzuka.form.ProjectForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.ProjectMemberModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * ProjectServiceImplのテストクラス.
 * @author kazumune
 */
public class ProjectServiceImplTest extends AppEngineTestCase4HRD {

	ProjectServiceImpl service = new ProjectServiceImpl();
	ProjectDao dao = new ProjectDao();
	MemberDao memberDao = new MemberDao();
	ProjectMemberDao projectMemberDao = new ProjectMemberDao();
	
	/**
	 * getのテスト.
	 */
	@Test
	public void testGet() {
		Set<Key> keys = createTestData(3);
		Key[] keyArray = keys.toArray(new Key[0]);
		
		String keyString = Datastore.keyToString(keyArray[0]);
		ProjectForm actual = service.get(keyString);
		assertThat(actual.keyToString, is(keyString));
		assertThat(actual.projectName, is("name0"));
		assertThat(actual.projectId, is("project_0"));
		assertThat(actual.projectSummary, is("summary0"));
		assertThat(actual.versionNo, is("1"));
		
		List<LabelValueBean> memberList = actual.memberList;
		assertThat(memberList.size(), is(1));
		assertThat(memberList.get(0).getLabel(), is("なまえ"));
		MemberModel model = new MemberModel();
		assertThat(memberList.get(0).getValue(), is(Datastore.keyToString(model.createKey("hoge@gmail.com"))));
		
		
		//新規の場合
		actual = service.get("");
		assertThat(actual.keyToString, is(nullValue()));
		assertThat(actual.projectName, is(nullValue()));
		assertThat(actual.projectId, is(nullValue()));
		assertThat(actual.projectSummary, is(nullValue()));
		assertThat(actual.versionNo, is(nullValue()));
		
		//指定したが、レコードが存在しなかった場合(不正なKeyを渡された)
		actual = service.get("agpVbml0IFRlc3RzchILEgxQcm9qZWN0TW9kZWwQAYw");
		assertThat(actual.keyToString, is(nullValue()));
		assertThat(actual.projectName, is(nullValue()));
		assertThat(actual.projectId, is(nullValue()));
		assertThat(actual.projectSummary, is(nullValue()));
		assertThat(actual.versionNo, is(nullValue()));
		
		//指定したが、レコードが存在しなかった場合(Keyは正しいが存在しない)
		keyString = Datastore.keyToString(Datastore.createKey(ProjectModel.class, -1L));
		actual = service.get(keyString);
		assertThat(actual.keyToString, is(nullValue()));
		assertThat(actual.projectName, is(nullValue()));
		assertThat(actual.projectId, is(nullValue()));
		assertThat(actual.projectSummary, is(nullValue()));
		assertThat(actual.versionNo, is(nullValue()));
	}
	
	/**
	 * putとdeleteのテスト.
	 */
	@Test
	public void testPutAndDelete() {
		
		createTestData(3);
		
		//新規登録のパターン
		String keyToString = testPutCreate();
		
		//変更のパターン
		testPurUpdate(keyToString);
		
		//削除のパターン
		testDelete(keyToString);
		
	}
	
	/**
	 * 削除のパターン
	 * @param keyToString 登録データのKey文字列
	 */
	private void testDelete(String keyToString) {
		ProjectForm form = service.get(keyToString);
		String beforeVersion = form.versionNo;
		
		//バージョン違いの場合
		form.versionNo = "-1";
		try {
			service.delete(form);
			fail();
		} catch(ConcurrentModificationException e) {}
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		form.versionNo = beforeVersion;
		service.delete(form);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//存在しなくなっていることを確認
		form = service.get(keyToString);
		assertThat(form.keyToString, is(nullValue()));
		//プロジェクトメンバーも削除されていることを確認
		List<ProjectMemberModel> projectMemberList = projectMemberDao.getList(keyToString, null);
		assertThat(projectMemberList.size(), is(0));
	}
	
	
	/**
	 * 変更のパターン
	 * @param keyToString 登録データのKey文字列
	 */
	private void testPurUpdate(String keyToString) {
		ProjectForm form = service.get(keyToString);
		
		form.projectName = "プロジェクトほげほげ2";
		form.projectId = "project_hogehoge2";
		form.projectSummary = "プロジェクト\n概要2";
		MemberModel model = new MemberModel();
		form.adminMemberId = Datastore.keyToString(model.createKey("hoge2@gmail.com"));
		service.put(form);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		List<ProjectModelEx> actualList = service.getList("プロジェクトほげほげ2");
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getModel().getProjectId(), is("project_hogehoge2"));
		assertThat(actualList.get(0).getModel().getProjectName(), is("プロジェクトほげほげ2"));
		assertThat(actualList.get(0).getModel().getProjectSummary(), is(nullValue()));
		assertThat(actualList.get(0).getModel().getVersion(), is(2L));
		assertThat(actualList.get(0).getProjectSummaryView(), is("プロジェクト<br />概要2"));

		//更新の場合はプロジェクト管理者として追加登録されないことを確認
		List<ProjectMemberModel> projectMemberList = projectMemberDao.getList(keyToString, null);
		assertThat(projectMemberList.size(), is(1));

		
		//バージョン違いの場合
		form.versionNo = "-1";
		try {
			service.put(form);
			fail();
		} catch(ConcurrentModificationException e) {}
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
	}

	/**
	 * 新規登録のパターン.
	 * @return 登録データのKey文字列
	 */
	private String testPutCreate() {
		ProjectForm form = new ProjectForm();
		form.projectName = "プロジェクトほげほげ";
		form.projectId = "project_hogehoge";
		form.projectSummary = "プロジェクト\n概要";
		MemberModel model = new MemberModel();
		form.adminMemberId = Datastore.keyToString(model.createKey("hoge@gmail.com"));
		service.put(form);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		List<ProjectModelEx> actualList = service.getList("プロジェクトほげほげ");
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getModel().getProjectId(), is("project_hogehoge"));
		assertThat(actualList.get(0).getModel().getProjectName(), is("プロジェクトほげほげ"));
		assertThat(actualList.get(0).getModel().getProjectSummary(), is(nullValue()));
		assertThat(actualList.get(0).getModel().getVersion(), is(1L));
		assertThat(actualList.get(0).getProjectSummaryView(), is("プロジェクト<br />概要"));
		
		String projectKeyString = Datastore.keyToString(actualList.get(0).getModel().getKey());
		
		//プロジェクト管理者として登録されていることを確認
		List<ProjectMemberModel> projectMemberList = projectMemberDao.getList(projectKeyString, null);
		assertThat(projectMemberList.size(), is(1));
		assertThat(projectMemberList.get(0).getMemberKey(), is(model.getKey()));
		assertThat(projectMemberList.get(0).getProjectAuthority(), is(ProjectAuthority.type1));
		
		return projectKeyString;
	}

	/**
	 * createUserProjectListのテスト.
	 */
	@Test
	public void testCreateUserProjectList() {
		assertThat(dao.getAllList().size(), is(0));
		
		Set<Key> keys = createTestData(3);
		List<LabelValueBean> actual = service.createUserProjectList(keys);
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
		
		List<ProjectModel> actual = service.getAllList();
		assertThat(actual.size(), is(3));
		
		assertThat(actual.get(0).getKey().getId(), is(1L));
		assertThat(actual.get(1).getKey().getId(), is(2L));
		assertThat(actual.get(2).getKey().getId(), is(3L));
	}
	
	/**
	 * テストデータ作成.
	 * 作成件数 + 存在しないKeyを作成します。
	 * メンバーも1人分作成します。
	 * @param count 作成件数
	 * @return 作成件数+存在しないKeySet
	 */
	private Set<Key> createTestData(int count) {
		Set<Key> keys = new LinkedHashSet<Key>();
		for(int i = 0; i < count; i++) {
			ProjectModel model = new ProjectModel();
			model.setProjectId("project_" + i);
			model.setProjectName("name" + i);
			model.setProjectSummary(new Text("summary" + i));
			dao.put(model);
			keys.add(model.getKey());
		}
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		keys.add(Datastore.createKey(ProjectModel.class, -1L));
		
		
		MemberModel model = new MemberModel();
		model.createKey("hoge@gmail.com");
		model.setName("なまえ");
		model.setAuthority(Authority.normal);
		memberDao.put(model);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
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
