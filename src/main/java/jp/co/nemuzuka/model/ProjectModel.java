package jp.co.nemuzuka.model;

import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.CreationUser;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModificationDate;
import org.slim3.datastore.ModificationUser;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

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
	private String projectSummary;
		
	//共通的に使用する
	/** 登録日時. */
	@Attribute(listener = CreationDate.class)
	Date createdAt;
	
	/** 登録ユーザ. */
	@Attribute(listener = CreationUser.class)
	User createUser;
	
	/** 更新日時. */
	@Attribute(listener = ModificationDate.class)
	Date updatedAt;
	
	/** 更新ユーザ. */
	@Attribute(listener = ModificationUser.class)
	User updateUser;

	/** バージョンNo. */
	@Attribute(version = true)
	Long version;

	/**
	 * @return key
	 */
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
	public String getProjectSummary() {
		return projectSummary;
	}

	/**
	 * @param projectSummary セットする projectSummary
	 */
	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}

	/**
	 * @return createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt セットする createdAt
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return createUser
	 */
	public User getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser セットする createUser
	 */
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt セットする updatedAt
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return updateUser
	 */
	public User getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser セットする updateUser
	 */
	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version セットする version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}

}
