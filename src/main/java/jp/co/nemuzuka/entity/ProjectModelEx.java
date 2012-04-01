package jp.co.nemuzuka.entity;

import jp.co.nemuzuka.model.ProjectModel;

/**
 * プロジェクト画面表示用Entity.
 * @author kazumune
 */
public class ProjectModelEx {
	/** ProjectModel. */
	private ProjectModel model;

	/** プロジェクト概要表示用文字列. */
	private String projectSummaryView;
	
	/**
	 * @return projectSummaryView
	 */
	public String getProjectSummaryView() {
		return projectSummaryView;
	}

	/**
	 * @param projectSummaryView セットする projectSummaryView
	 */
	public void setProjectSummaryView(String projectSummaryView) {
		this.projectSummaryView = projectSummaryView;
	}

	/**
	 * @return model
	 */
	public ProjectModel getModel() {
		return model;
	}

	/**
	 * @param model セットする model
	 */
	public void setModel(ProjectModel model) {
		this.model = model;
	}
}
