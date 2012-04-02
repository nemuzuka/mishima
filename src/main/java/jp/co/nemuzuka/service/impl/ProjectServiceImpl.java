package jp.co.nemuzuka.service.impl;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.core.entity.LabelValueBean;
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

public class ProjectServiceImpl implements ProjectService {

	ProjectDao projectDao = new ProjectDao();
	MemberDao memberDao = new MemberDao();
	ProjectMemberDao projectMemberDao = new ProjectMemberDao();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectService#get(java.lang.String)
	 */
	@Override
	public ProjectForm get(String keyString) {
		
		ProjectForm form = new ProjectForm();
		
		if(StringUtils.isNotEmpty(keyString)) {
			ProjectModel model = projectDao.get(keyString);
			if(model != null) {
				setForm(form, model);
			}
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
			ProjectModelEx model = new ProjectModelEx();
			model.setModel(target);
			model.setProjectSummaryView(HtmlStringUtils.escapeTextAreaString(target.getProjectSummary()));
			target.setProjectSummary("");
			retList.add(model);
		}
		return retList;
	}

	@Override
	public List<ProjectModel> getUserProjectList(String email) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public List<ProjectModel> getAllList() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
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
		form.projectSummary = model.getProjectSummary();
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
		model.setProjectSummary(form.projectSummary);
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



}
