package jp.co.nemuzuka.form;

import java.io.Serializable;

/**
 * プロジェクト検索画面の入力Form.
 * @author kazumune
 */
public class ProjectSearchForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** プロジェクト名. */
	public String projectName;

	/**
	 * @return projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName セットする projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
}
