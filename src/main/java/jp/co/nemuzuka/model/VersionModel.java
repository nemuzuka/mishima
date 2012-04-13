package jp.co.nemuzuka.model;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * バージョンを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class VersionModel extends AbsModel {

	/** バージョンKey. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** バージョン名. */
	@Attribute(unindexed=true)
	private String versionName;

	/** プロジェクトKey. */
	private Key projectKey;
	
	/** ソート順. */
	private Long sortNum;
	
	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}
	/**
	 * @return sortNum
	 */
	@JSONHint(ignore=true)
	public Long getSortNum() {
		return sortNum;
	}
	/**
	 * @return projectKey
	 */
	@JSONHint(ignore=true)
	public Key getProjectKey() {
		return projectKey;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionName() {
		return versionName;
	}
	/**
	 * @param versionName the versionName to set
	 */
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	/**
	 * @param key セットする key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @param sortNum セットする sortNum
	 */
	public void setSortNum(Long sortNum) {
		this.sortNum = sortNum;
	}

	/**
	 * @param projectKey セットする projectKey
	 */
	public void setProjectKey(Key projectKey) {
		this.projectKey = projectKey;
	}
}
