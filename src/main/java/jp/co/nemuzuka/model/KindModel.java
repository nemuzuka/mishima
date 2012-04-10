package jp.co.nemuzuka.model;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * 種別を管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class KindModel extends AbsModel {

	/** 種別Key. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** 種別名. */
	private String kindName;

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
	 * @return kindName
	 */
	public String getKindName() {
		return kindName;
	}

	/**
	 * @param kindName セットする kindName
	 */
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	/**
	 * @param key セットする key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return sortNum
	 */
	public Long getSortNum() {
		return sortNum;
	}

	/**
	 * @param sortNum セットする sortNum
	 */
	public void setSortNum(Long sortNum) {
		this.sortNum = sortNum;
	}

	/**
	 * @return projectKey
	 */
	public Key getProjectKey() {
		return projectKey;
	}

	/**
	 * @param projectKey セットする projectKey
	 */
	public void setProjectKey(Key projectKey) {
		this.projectKey = projectKey;
	}
}
