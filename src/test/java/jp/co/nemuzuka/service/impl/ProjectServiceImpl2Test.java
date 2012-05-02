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
package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.core.entity.UserInfo;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.ProjectMemberDao;
import jp.co.nemuzuka.entity.ProjectModelEx;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.ProjectMemberModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * ProjectServiceImplのテストクラス(その2).
 * @author kazumune
 */
public class ProjectServiceImpl2Test extends AppEngineTestCase4HRD {

	ProjectServiceImpl service = ProjectServiceImpl.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	ProjectMemberDao projectMemberDao = ProjectMemberDao.getInstance();
	
	//テスト用データ
	List<Key> projectKeyList;
	List<Key> memberKeyList;

	/**
	 * getUserProjectListのテスト.
	 * @throws Exception
	 */
	@Test
	public void testGetUserProjectListOnlyMail() throws Exception {
		createInitData();

		//GAE管理者であっても、所属するプロジェクトのみ
		List<ProjectModelEx> actualList = service.getUserProjectList("mail0@gmail.com");
		assertThat(actualList.size(), is(2));
		assertThat(actualList.get(0).getModel().getProjectName(), is("project_name_0"));
		assertThat(actualList.get(1).getModel().getProjectName(), is("project_name_1"));
		
		//存在しないユーザからのアクセス
		actualList = service.getUserProjectList("noRegist@gmail.com");
		assertThat(actualList.size(), is(0));
		
		//参加プロジェクトが1件も存在しない場合
		actualList = service.getUserProjectList("mail3@gmail.com");
		assertThat(actualList.size(), is(0));
		
	}
	
	/**
	 * getUserProjectListのテスト.
	 */
	@Test
	public void testGetUserProjectList() {
		
		createInitData();
		
		//存在しないユーザからのアクセス(ありえない筈)
		ProjectService.TargetProjectResult actual = service.getUserProjectList("noRegist@gmail.com", false);
		assertThat(actual.admin, is(false));
		assertThat(actual.projectList.size(), is(0));
		
		//GAE管理者でなくシステム管理者の場合
		actual = service.getUserProjectList("mail0@gmail.com", false);
		assertThat(actual.admin, is(true));
		assertThat(actual.projectList.size(), is(4));
		assertThat(actual.projectList.get(0).getLabel(), is("--プロジェクトを選択--"));
		assertThat(actual.projectList.get(1).getLabel(), is("project_name_0"));
		assertThat(actual.projectList.get(2).getLabel(), is("project_name_1"));
		assertThat(actual.projectList.get(3).getLabel(), is("project_name_2"));
		
		//GAE管理者でなくシステム管理者でもない場合
		actual = service.getUserProjectList("mail1@gmail.com", false);
		assertThat(actual.admin, is(false));
		assertThat(actual.projectList.size(), is(2));
		assertThat(actual.projectList.get(0).getLabel(), is("--プロジェクトを選択--"));
		assertThat(actual.projectList.get(1).getLabel(), is("project_name_2"));

		//GAE管理者でありシステム管理者でない場合
		//全てのプロジェクトが選択可能
		actual = service.getUserProjectList("mail1@gmail.com", true);
		assertThat(actual.projectList.size(), is(4));
		assertThat(actual.projectList.get(0).getLabel(), is("--プロジェクトを選択--"));
		assertThat(actual.projectList.get(1).getLabel(), is("project_name_0"));
		assertThat(actual.projectList.get(2).getLabel(), is("project_name_1"));
		assertThat(actual.projectList.get(3).getLabel(), is("project_name_2"));
	}
	
	/**
	 * setUserInfoのテスト.
	 */
	@Test
	public void testSetUserInfo() {
		createInitData();
		UserInfo userInfo = new UserInfo();
		userInfo.systemManager = false;
		
		//一般ユーザの場合
		String selectedProject = Datastore.keyToString(projectKeyList.get(1));
		service.setUserInfo(selectedProject, "mail2@gmail.com", userInfo);
		assertThat(userInfo.selectedProject, is(selectedProject));
		assertThat(userInfo.projectManager, is(false));
		assertThat(userInfo.projectMember, is(true));
		assertThat(userInfo.projectAuthority, is(ProjectAuthority.type3));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		//プロジェクト管理者の場合
		userInfo = new UserInfo();
		userInfo.systemManager = false;
		selectedProject = Datastore.keyToString(projectKeyList.get(2));
		service.setUserInfo(selectedProject, "mail1@gmail.com", userInfo);
		assertThat(userInfo.selectedProject, is(selectedProject));
		assertThat(userInfo.projectManager, is(true));
		assertThat(userInfo.projectMember, is(true));
		assertThat(userInfo.projectAuthority, is(ProjectAuthority.type1));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//GAE管理者でメンバーでない場合
		userInfo = new UserInfo();
		userInfo.systemManager = true;
		selectedProject = Datastore.keyToString(projectKeyList.get(2));
		service.setUserInfo(selectedProject, "mail3@gmail.com", userInfo);
		assertThat(userInfo.selectedProject, is(selectedProject));
		assertThat(userInfo.projectManager, is(true));
		assertThat(userInfo.projectMember, is(false));
		assertThat(userInfo.projectAuthority, is(nullValue()));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//存在しないメンバーの場合
		userInfo = new UserInfo();
		userInfo.systemManager = false;
		selectedProject = Datastore.keyToString(projectKeyList.get(2));
		service.setUserInfo(selectedProject, "no_mail@gmail.com", userInfo);
		assertThat(userInfo.selectedProject, is(selectedProject));
		assertThat(userInfo.projectManager, is(false));
		assertThat(userInfo.projectMember, is(false));
		assertThat(userInfo.projectAuthority, is(nullValue()));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//GAE、システム管理者で、プロジェクト管理者でない場合
		userInfo = new UserInfo();
		userInfo.systemManager = true;
		selectedProject = Datastore.keyToString(projectKeyList.get(1));
		service.setUserInfo(selectedProject, "mail0@gmail.com", userInfo);
		assertThat(userInfo.selectedProject, is(selectedProject));
		assertThat(userInfo.projectManager, is(true));
		assertThat(userInfo.projectMember, is(true));
		assertThat(userInfo.projectAuthority, is(ProjectAuthority.type2));
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		
		
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
	 * ProjectMemberModel登録.
	 * @param model 登録対象Model
	 */
	private void putProjectMemberModel(ProjectMemberModel model) {
		projectMemberDao.put(model);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
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
