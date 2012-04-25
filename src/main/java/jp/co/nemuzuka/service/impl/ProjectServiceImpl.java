package jp.co.nemuzuka.service.impl;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.core.entity.UserInfo;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectDao;
import jp.co.nemuzuka.dao.ProjectMemberDao;
import jp.co.nemuzuka.entity.ProjectModelEx;
import jp.co.nemuzuka.form.ProjectForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.ProjectMemberModel;
import jp.co.nemuzuka.model.ProjectModel;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.HtmlStringUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

public class ProjectServiceImpl implements ProjectService {

	ProjectDao projectDao = ProjectDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	ProjectMemberDao projectMemberDao = ProjectMemberDao.getInstance();
	
	private static ProjectServiceImpl impl = new ProjectServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static ProjectServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private ProjectServiceImpl(){}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#get(java.lang.String)
	 */
	@Override
	public ProjectForm get(String keyString) {
		
		ProjectForm form = new ProjectForm();
		
		if(StringUtils.isNotEmpty(keyString)) {
			ProjectModel model = projectDao.get(keyString);
			setForm(form, model);
		}
		//Formに対して画面表示情報を設定する
		setFormCommon(form);
		return form;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#put(jp.co.nemuzuka.form.ProjectForm)
	 */
	@Override
	public void put(ProjectForm form) {
		ProjectModel model = null;
		boolean isAdd = false;
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//versionとKeyで情報を取得
			model = projectDao.get(key, version);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new ProjectModel();
			isAdd = true;
		}
		setModel(model, form);
		memberDao.put(model);
		
		//新規の場合、管理者情報としてメンバーを登録する
		if(isAdd) {
			addProjectMember4Admin(model.getKey(), form.adminMemberId);
		}
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#delete(jp.co.nemuzuka.form.ProjectForm)
	 */
	@Override
	public void delete(ProjectForm form) {
		//versionとKeyで情報を取得
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		ProjectModel model = projectDao.get(key, version);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		//削除
		projectDao.delete(model.getKey());
		
		//プロジェクトメンバーも削除
		List<ProjectMemberModel> list = projectMemberDao.getList(form.keyToString, null);
		List<Key> keyList = new ArrayList<Key>();
		for(ProjectMemberModel target : list) {
			keyList.add(target.getKey());
		}
		projectMemberDao.delete(keyList.toArray(new Key[0]));
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#getList(java.lang.String)
	 */
	@Override
	public List<ProjectModelEx> getList(String projectName) {
		
		List<ProjectModel> list = projectDao.getList(projectName);
		List<ProjectModelEx> retList = new ArrayList<ProjectModelEx>();
		for(ProjectModel target : list) {
			retList.add(createProjectModelEx(target));
		}
		return retList;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#getUserProjectList(java.lang.String, boolean)
	 */
	@Override
	public TargetProjectResult getUserProjectList(String email, boolean gaeAdmin){
		
		TargetProjectResult result = new TargetProjectResult();
		
		MemberModel model = memberDao.get(email);
		if(model == null) {
			return result;
		}
		
		if(gaeAdmin || model.getAuthority() == Authority.admin) {
			result.admin = true;
			
			//管理者権限を有する場合、全てのプロジェクトが対象
			List<ProjectModel> list = projectDao.getAllList();
			result.projectList = createUserProjectList(list);
		} else {
			//一般の場合、ログインユーザが紐付くプロジェクト情報を取得する
			List<ProjectMemberModel> list = projectMemberDao.getList(null, model.getKeyToString());
			
			Set<Key> projectKeySet = new LinkedHashSet<Key>();
			for(ProjectMemberModel target : list) {
				projectKeySet.add(target.getProjectKey());
			}
			result.projectList = createUserProjectList(projectKeySet);
		}
		return result;
	}


	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#getUserProjectList(java.lang.String)
	 */
	@Override
	public List<ProjectModelEx> getUserProjectList(String email) {
		
		List<ProjectModelEx> retList = new ArrayList<ProjectModelEx>();
		MemberModel model = memberDao.get(email);
		if(model == null) {
			return retList;
		}

		//ログインユーザが紐付くプロジェクト情報を取得する
		List<ProjectMemberModel> list = projectMemberDao.getList(null, model.getKeyToString());
		Set<Key> projectKeySet = new LinkedHashSet<Key>();
		for(ProjectMemberModel target : list) {
			projectKeySet.add(target.getProjectKey());
		}
		if(projectKeySet.size() == 0) {
			return retList;
		}
		
		//戻りListを作成する
		List<ProjectModel> projectList = projectDao.getList(projectKeySet.toArray(new Key[0]));
		for(ProjectModel target : projectList) {
			retList.add(createProjectModelEx(target));
		}
		return retList;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#getAllList()
	 */
	@Override
	public List<ProjectModel> getAllList() {
		return projectDao.getAllList();
	}


	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#setUserInfo(java.lang.String, java.lang.String, jp.co.nemuzuka.core.entity.UserInfo)
	 */
	@Override
	public void setUserInfo(String projectKeyString, String mail,
			UserInfo userInfo) {
		//初期化
		userInfo.initProjectInfo();
		userInfo.setSelectedProject(projectKeyString);

		if(StringUtils.isEmpty(projectKeyString)) {
			//選択プロジェクトが未選択にされた場合、初期化して終了
			return;
		}
		
		MemberModel memberModel = memberDao.get(mail);
		if(memberModel == null) {
			return;
		}
		//管理者ユーザである場合、管理者権限ありと設定する
		if(userInfo.isSystemManager()) {
			userInfo.setProjectManager(true);
		}
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		//プロジェクトに紐付くメンバー情報を取得する
		ProjectMemberModel projectMemberModel = projectMemberDao.get(projectKey, memberModel.getKey());
		if(projectMemberModel == null) {
			return;
		}
		
		//データが取得できたので、プロジェクトメンバーである
		userInfo.setProjectMember(true);
		
		//GAE、アプリケーション管理者でなく、プロジェクト管理者の権限を取得している場合
		//プロジェクト管理者であると設定
		if(userInfo.isProjectManager() == false) {
			if(projectMemberModel.getProjectAuthority() == ProjectAuthority.type1) {
				userInfo.setProjectManager(true);
			}
		}
		userInfo.setProjectAuthority(projectMemberModel.getProjectAuthority());
	}

	/**
	 * プロジェクトLabelValueBean情報取得.
	 * @param projectKeySet プロジェクトKeySet
	 * @return プロジェクトLabelValueBeanList(先頭には空データ格納)
	 */
	protected List<LabelValueBean> createUserProjectList(Set<Key> projectKeySet) {
		
		List<ProjectModel> projectList = projectDao.getList(projectKeySet.toArray(new Key[0]));
		return createUserProjectList(projectList);
	}
	
	/**
	 * プロジェクトLabelValueBeanList作成.
	 * @param projectList プロジェクトList
	 * @return 生成List
	 */
	private List<LabelValueBean> createUserProjectList(List<ProjectModel> projectList) {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		list.add(new LabelValueBean("--プロジェクトを選択--", ""));
		for(ProjectModel target : projectList) {
			list.add(new LabelValueBean(target.getProjectName(), target.getKeyToString()));
		}
		return list;
	}

	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定元Model
	 */
	private void setForm(ProjectForm form, ProjectModel model) {
		if(model == null) {
			return;
		}
		
		form.keyToString = model.getKeyToString();
		form.projectName = model.getProjectName();
		form.projectId = model.getProjectId();
		form.projectSummary = model.getProjectSummary().getValue();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * 共通Form情報設定.
	 * 画面表示用情報を設定します。
	 * @param form 設定対象Form
	 */
	private void setFormCommon(ProjectForm form) {
		List<LabelValueBean> targetList = new ArrayList<LabelValueBean>();
		
		//登録されているメンバー情報を取得
		List<MemberModel> list = memberDao.getAllList();
		for(MemberModel target : list) {
			targetList.add(new LabelValueBean(target.getName(), target.getKeyToString()));
		}
		form.setMemberList(targetList);
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定Form
	 */
	private void setModel(ProjectModel model, ProjectForm form) {
		model.setProjectName(form.projectName);
		model.setProjectId(form.projectId);
		model.setProjectSummary(new Text(form.projectSummary));
	}

	/**
	 * 管理者用メンバー追加.
	 * 引数の情報を元に管理者権限を所有するプロジェクトメンバーを登録します。
	 * @param projectKey プロジェクトKey
	 * @param adminMemberId 管理者Key文字列
	 */
	private void addProjectMember4Admin(Key projectKey, String adminMemberId) {
		
		Key memberKey = Datastore.stringToKey(adminMemberId);
		ProjectMemberModel model = new ProjectMemberModel();
		model.createKey(projectKey, memberKey);
		model.setProjectAuthority(ProjectAuthority.type1);
		projectMemberDao.put(model);
	}
	
	/**
	 * ProjectModelEx生成.
	 * プロジェクト概要を設定した後、Modelの値を初期化します。
	 * @param target 生成元Model
	 * @return 生成Model
	 */
	private ProjectModelEx createProjectModelEx(ProjectModel target) {
		ProjectModelEx model = new ProjectModelEx();
		model.setModel(target);
		model.setProjectSummaryView(HtmlStringUtils.escapeTextAreaString(target.getProjectSummary().getValue()));
		target.setProjectSummary(null);
		return model;
	}
}
