package jp.co.nemuzuka.service.impl;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;


import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.ProjectMemberDao;
import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.form.CategoryForm;
import jp.co.nemuzuka.form.KindForm;
import jp.co.nemuzuka.form.MilestoneForm;
import jp.co.nemuzuka.form.PriorityForm;
import jp.co.nemuzuka.form.StatusForm;
import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.ProjectMemberModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.service.CategoryService;
import jp.co.nemuzuka.service.KindService;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.service.PriorityService;
import jp.co.nemuzuka.service.StatusService;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.tester.AppEngineTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * TicketMstServiceImplのテストクラス.
 * @author kazumune
 */
public class TicketMstServiceImplTest extends AppEngineTestCase4HRD {

	TicketMstServiceImpl service = TicketMstServiceImpl.getInstance();
	ProjectDao projectDao = ProjectDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	ProjectMemberDao projectMemberDao = ProjectMemberDao.getInstance();
	
	Key projectKey;

	/**
	 * getTicketMstのテストクラス.
	 */
	@Test
	public void testGetTicketMst() throws Exception {
		createTestData();
		String projectKeyString = Datastore.keyToString(projectKey);

		//キャッシュされていないので、作り変える
		TicketMst actual = service.getTicketMst(projectKeyString);
		
		assertThat(actual, is(not(nullValue())));
		assertThat(actual.priorityList.size(), is(4));
		assertThat(actual.statusList.size(), is(6));
		assertThat(actual.statusList.get(0).getValue(), is(""));
		assertThat(actual.statusList.get(1).getValue(), is("未対応"));
		assertThat(actual.statusList.get(2).getValue(), is("対応中"));
		assertThat(actual.statusList.get(3).getValue(), is("対応済み"));
		assertThat(actual.statusList.get(4).getValue(), is("完了"));
		assertThat(actual.statusList.get(5).getValue(), is("キャンセル"));
		
		assertThat(actual.openStatus, is(new String[]{"未対応","対応中","対応済み"}));
		
		assertThat(actual.searchStatusList.size(), is(6));
		assertThat(actual.searchStatusList.get(0).getValue(), is("-1"));
		assertThat(actual.searchStatusList.get(0).getLabel(), is("未完了"));
		assertThat(actual.searchStatusList.get(1).getValue(), is("未対応"));
		assertThat(actual.searchStatusList.get(2).getValue(), is("対応中"));
		assertThat(actual.searchStatusList.get(3).getValue(), is("対応済み"));
		assertThat(actual.searchStatusList.get(4).getValue(), is("完了"));
		assertThat(actual.searchStatusList.get(5).getValue(), is("キャンセル"));

		assertThat(actual.kindList.size(), is(4));
		assertThat(actual.categoryList.size(), is(5));
		assertThat(actual.milestoneList.size(), is(3));
		assertThat(actual.versionList.size(), is(6));
		
		//もう一度呼び出す際にはキャッシュされている
		actual = service.getTicketMst(projectKeyString);
		
		
		//初期化された超えた場合
		service.initRefreshStartTime(projectKeyString);
		//再作成
		actual = service.getTicketMst(projectKeyString);
	}
	
	
	/**
	 * テストデータ作成.
	 * @throws Exception 例外
	 */
	private void createTestData() throws Exception {
		
		ProjectModel model = new ProjectModel();
		model.setProjectId("test_project");
		model.setProjectName("テストプロジェクト");
		model.setProjectSummary(new Text("テスト用プロジェクトですってば"));
		projectDao.put(model);
		projectKey = model.getKey();
		
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		CategoryService categoryService = CategoryServiceImpl.getInstance();
		KindService kindService = KindServiceImpl.getInstance();
		PriorityService priorityService = PriorityServiceImpl.getInstance();
		StatusService statusService = StatusServiceImpl.getInstance();
		VersionService versionService = VersionServiceImpl.getInstance();
		MilestoneService milestoneService = MilestoneServiceImpl.getInstance();
		String projectKeyString = Datastore.keyToString(projectKey);
		
		CategoryForm categoryForm = new CategoryForm();
		categoryForm.categoryName = "カテゴリ名\nカテゴリ名2\nカテゴリ名3\nカテゴリ名4";
		categoryService.put(categoryForm, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		KindForm kindForm = new KindForm();
		kindForm.kindName = "種別名\n種別名1\n種別2\n";
		kindService.put(kindForm, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		PriorityForm priorityForm = new PriorityForm();
		priorityForm.priorityName = "高\n中\n低";
		priorityService.put(priorityForm, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();

		StatusForm statusForm = new StatusForm();
		statusForm.statusName = "未対応\n対応中\n対応済み\n完了\nキャンセル";
		statusForm.closeStatusName = "完了\nキャンセル";
		statusService.put(statusForm, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		VersionForm versionForm = new VersionForm();
		versionForm.versionName = "1.0.0\n1.0.1\n1.0.2\n2.0.0\n2.0.1\n";
		versionService.put(versionForm, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		MilestoneForm milestoneForm = new MilestoneForm();
		milestoneForm.milestoneName = "まいるすとん1";
		milestoneService.put(milestoneForm, projectKeyString);
		milestoneForm = new MilestoneForm();
		milestoneForm.milestoneName = "まいるすとん2";
		milestoneService.put(milestoneForm, projectKeyString);
		GlobalTransaction.transaction.get().commit();
		GlobalTransaction.transaction.get().begin();
		
		//メンバーとプロジェクトメンバーを登録
		List<Key> memberKeyList = new ArrayList<Key>();
		for(int i = 0; i < 4; i++) {
			MemberModel memberModel = new MemberModel();
			memberModel.createKey("mail" + i + "@gmail.com");
			memberModel.setName("name" + i);
			Authority authority = null;
			if(i == 0 || i == 2) {
				authority = Authority.admin;
			} else {
				authority = Authority.normal;
			}
			memberModel.setAuthority(authority);
			memberDao.put(memberModel);
			memberKeyList.add(memberModel.getKey());
			
			GlobalTransaction.transaction.get().commit();
			GlobalTransaction.transaction.get().begin();
		}

		ProjectMemberModel projectMemberModel = new ProjectMemberModel();
		projectMemberModel.createKey(projectKey, memberKeyList.get(0));
		projectMemberModel.setProjectAuthority(ProjectAuthority.type1);
		projectMemberDao.put(projectMemberModel);
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
