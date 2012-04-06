package jp.co.nemuzuka.core.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Session情報.
 * @author kazumune
 */
public class UserInfo implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** 更新開始時刻. */
	//この時間を超えた場合、参照可能プロジェクトListを再設定する
	public Date refreshStartTime;
	
	/** 参照可能プロジェクトList. */
	public List<LabelValueBean> projectList;
	
	/** 選択プロジェクト. */
	public String selectedProject = "";

	/** 選択プロジェクトの管理者であればtrue. */
	//管理系の機能を使用できる
	public boolean projectManager;

	/** 選択プロジェクトのメンバーであればtrue. */
	//チケットの参照ができる
	public boolean projectMember;

	/** システムの管理者(管理者権限を持つ or GAE管理者)であればtrue. */
	public boolean systemManager;
	
	/**
	 * @return projectMember
	 */
	public boolean isProjectMember() {
		return projectMember;
	}

	/**
	 * @param projectMember セットする projectMember
	 */
	public void setProjectMember(boolean projectMember) {
		this.projectMember = projectMember;
	}

	/**
	 * @return projectManager
	 */
	public boolean isProjectManager() {
		return projectManager;
	}

	/**
	 * @param projectManager セットする projectManager
	 */
	public void setProjectManager(boolean projectManager) {
		this.projectManager = projectManager;
	}

	/**
	 * @return systemManager
	 */
	public boolean isSystemManager() {
		return systemManager;
	}

	/**
	 * @param systemManager セットする systemManager
	 */
	public void setSystemManager(boolean systemManager) {
		this.systemManager = systemManager;
	}

	/**
	 * @return refreshStartTime
	 */
	public Date getRefreshStartTime() {
		return refreshStartTime;
	}

	/**
	 * @param refreshStartTime セットする refreshStartTime
	 */
	public void setRefreshStartTime(Date refreshStartTime) {
		this.refreshStartTime = refreshStartTime;
	}

	/**
	 * @return projectList
	 */
	public List<LabelValueBean> getProjectList() {
		return projectList;
	}

	/**
	 * @param projectList セットする projectList
	 */
	public void setProjectList(List<LabelValueBean> projectList) {
		this.projectList = projectList;
	}

	/**
	 * @return selectedProject
	 */
	public String getSelectedProject() {
		return selectedProject;
	}

	/**
	 * @param selectedProject セットする selectedProject
	 */
	public void setSelectedProject(String selectedProject) {
		this.selectedProject = selectedProject;
	}
}
