package jp.co.nemuzuka.model;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * プロジェクトを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class ProjectModel extends AbsModel {

	/** プロジェクトKey. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** プロジェクト名. */
	private String projectName;
	
	/** プロジェクト識別子. */
	//被っても特に問題はない
	private String projectId;
	
	/** プロジェクト概要. */
	@Attribute(unindexed=true)
	private Text projectSummary;

	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}

	/**
	 * @param key セットする key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

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

	/**
	 * @return projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId セットする projectId
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return projectSummary
	 */
	public Text getProjectSummary() {
		return projectSummary;
	}

	/**
	 * @param projectSummary セットする projectSummary
	 */
	public void setProjectSummary(Text projectSummary) {
		this.projectSummary = projectSummary;
	}
}
