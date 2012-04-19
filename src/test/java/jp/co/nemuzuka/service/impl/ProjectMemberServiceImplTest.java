package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.ProjectMemberDao;
import jp.co.nemuzuka.entity.ProjectMemberModelEx;
import jp.co.nemuzuka.form.ProjectMemberForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.ProjectMemberModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * ProjectMemberServiceImplのテストクラス.
 * @author k-katagiri
 */
public class ProjectMemberServiceImplTest extends AppEngineTestCase4HRD {

	ProjectMemberServiceImpl service = new ProjectMemberServiceImpl();
	MemberDao memberDao = new MemberDao();
	ProjectDao projectDao = new ProjectDao();
	ProjectMemberDao projectMemberDao = new ProjectMemberDao();
	
	//テスト用データ
	List<Key> projectKeyList;
	List<Key> memberKeyList;
	
	/**
	 * getProjectMemberModelListのテスト.
	 */
	@Test
	public void testGetProjectMemberModelList() {
		
		createInitData();

		assertThat(memberDao.getAllList().size(), is(4));
		assertThat(projectDao.getAllList().size(), is(3));
		assertThat(projectMemberDao.getAllList().size(), is(4));
		
		checkProject0();
		checkProject1();
		checkProject2();
	}

	/**
	 * プロジェクト2用の登録状況を参照します。
	 */
	private void checkProject2() {
		//プロジェクト0用の状況
		String selectedProject = Datastore.keyToString(projectKeyList.get(2));
		List<ProjectMemberModelEx> actualList = service.getProjectMemberModelList(selectedProject);
		assertThat(actualList.size(), is(4));
		
		ProjectMemberModelEx actual = actualList.get(0);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));
		
		actual = actualList.get(1);
		assertThat(actual.projectMember, is(true));
		assertThat(actual.authorityCode, is(ProjectAuthority.type1.getCode()));
		assertThat(actual.member.getMail(), is(""));

		actual = actualList.get(2);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));

		actual = actualList.get(3);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));
	}
	
	
	/**
	 * プロジェクト1用の登録状況を参照します。
	 */
	private void checkProject1() {
		//プロジェクト0用の状況
		String selectedProject = Datastore.keyToString(projectKeyList.get(1));
		List<ProjectMemberModelEx> actualList = service.getProjectMemberModelList(selectedProject);
		assertThat(actualList.size(), is(4));
		
		ProjectMemberModelEx actual = actualList.get(0);
		assertThat(actual.projectMember, is(true));
		assertThat(actual.authorityCode, is(ProjectAuthority.type2.getCode()));
		assertThat(actual.member.getMail(), is(""));
		
		actual = actualList.get(1);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));

		actual = actualList.get(2);
		assertThat(actual.projectMember, is(true));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));

		actual = actualList.get(3);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));
	}

	/**
	 * プロジェクト0用の登録状況を参照します。
	 */
	private void checkProject0() {
		//プロジェクト0用の状況
		String selectedProject = Datastore.keyToString(projectKeyList.get(0));
		List<ProjectMemberModelEx> actualList = service.getProjectMemberModelList(selectedProject);
		assertThat(actualList.size(), is(4));
		
		ProjectMemberModelEx actual = actualList.get(0);
		assertThat(actual.projectMember, is(true));
		assertThat(actual.authorityCode, is(ProjectAuthority.type1.getCode()));
		assertThat(actual.member.getMail(), is(""));
		
		actual = actualList.get(1);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));

		actual = actualList.get(2);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));

		actual = actualList.get(3);
		assertThat(actual.projectMember, is(false));
		assertThat(actual.authorityCode, is(ProjectAuthority.type3.getCode()));
		assertThat(actual.member.getMail(), is(""));
	}
	

	/**
	 * updateProjectMemberのテスト.
	 */
	@Test
	public void testUpdateProjectMember() {
		createInitData2();
		
		Set<String> memberKeySet = new LinkedHashSet<String>();
		memberKeySet.add(Datastore.keyToString(memberKeyList.get(0)));
		memberKeySet.add(Datastore.keyToString(memberKeyList.get(2)));
		memberKeySet.add(Datastore.keyToString(memberKeyList.get(5)));
		memberKeySet.add(Datastore.keyToString(memberKeyList.get(6)));
		String[] memberKeys = memberKeySet.toArray(new String[0]);
		
		Set<String> authorityCodeSet = new LinkedHashSet<String>();
		authorityCodeSet.add(ProjectAuthority.type1.getCode());
		authorityCodeSet.add(ProjectAuthority.type3.getCode());
		authorityCodeSet.add("不正なコード");
		authorityCodeSet.add(ProjectAuthority.type2.getCode());
		String[] authorityCodes = authorityCodeSet.toArray(new String[0]);
		
		ProjectMemberForm form = new ProjectMemberForm();
		form.memberKeyArray = memberKeys;
		form.authorityCodeArray = authorityCodes;
		
		String projectKeyString = Datastore.keyToString(projectKeyList.get(0));
		
		service.updateProjectMember(projectKeyString, form);
	}
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.tester.AppEngineTestCase4HRD#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();

		TransactionEntity transactionEntity = new TransactionEntity();
		GlobalTransaction.transaction.set(transactionEntity);
	}
	
	/**
	 * 事前データ作成.
	 * ユーザを4人作成します。
	 * プロジェクトを3つ作成します。
	 * 0番目のユーザ(ユーザ0と記述)の参加プロジェクトは、
	 * プロジェクト0：管理者
	 * プロジェクト1：開発者
	 * ユーザ1の参加プロジェクトは、
	 * プロジェクト2:管理者
	 * ユーザ2の参加プロジェクトは、
	 * プロジェクト1:報告者
	 * ユーザ3の参加プロジェクトは
	 * 無し
	 * というデータを作成します。
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
			memberDao.put(model);
			memberKeyList.add(model.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		projectKeyList = new ArrayList<Key>();
		for(int i = 0; i < 3; i++) {
			ProjectModel model = new ProjectModel();
			model.setProjectId("id_" + i);
			model.setProjectName("project_name_" + i);
			model.setProjectSummary(new Text("summary_" + i));
			
			projectDao.put(model);
			projectKeyList.add(model.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		//プロジェクトとメンバーを紐付ける
		//0番目のユーザに合致する情報
		Key targetMemberKey = memberKeyList.get(0);
		ProjectMemberModel model = new ProjectMemberModel();
		model.createKey(projectKeyList.get(0), targetMemberKey);
		model.setProjectAuthority(ProjectAuthority.type1);
		putProjectMemberModel(model);
		
		model = new ProjectMemberModel();
		model.createKey(projectKeyList.get(1), targetMemberKey);
		model.setProjectAuthority(ProjectAuthority.type2);
		putProjectMemberModel(model);

		//1番目のユーザに合致する情報
		targetMemberKey = memberKeyList.get(1);
		model = new ProjectMemberModel();
		model.createKey(projectKeyList.get(2), targetMemberKey);
		model.setProjectAuthority(ProjectAuthority.type1);
		putProjectMemberModel(model);
		
		//2番目のユーザに合致する情報
		targetMemberKey = memberKeyList.get(2);
		model = new ProjectMemberModel();
		model.createKey(projectKeyList.get(1), targetMemberKey);
		model.setProjectAuthority(ProjectAuthority.type3);
		putProjectMemberModel(model);
	}
	
	/**
	 * 事前データ作成.
	 * メンバーを10人作成します。
	 * プロジェクトを3つ作成します。
	 * 全てのプロジェクトにおいて、作成したメンバーが開発者として参加するものとして
	 * データを登録します。
	 */
	private void createInitData2() {
		memberKeyList = new ArrayList<Key>();
		for(int i = 0; i < 10; i++) {
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
			memberDao.put(model);
			memberKeyList.add(model.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		projectKeyList = new ArrayList<Key>();
		for(int i = 0; i < 3; i++) {
			ProjectModel model = new ProjectModel();
			model.setProjectId("id_" + i);
			model.setProjectName("project_name_" + i);
			model.setProjectSummary(new Text("summary_" + i));
			
			projectDao.put(model);
			projectKeyList.add(model.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}
		
		for(Key memberKey : memberKeyList) {
			
			for(Key projectKey : projectKeyList) {
				ProjectMemberModel model = new ProjectMemberModel();
				model.createKey(projectKey, memberKey);
				model.setProjectAuthority(ProjectAuthority.type2);
				putProjectMemberModel(model);
			}
		}
	}
	
	/**
	 * ProjectMemberModel登録.
	 * @param model 登録対象Model
	 */
	private void putProjectMemberModel(ProjectMemberModel model) {
		projectMemberDao.put(model);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
	}
	
}
