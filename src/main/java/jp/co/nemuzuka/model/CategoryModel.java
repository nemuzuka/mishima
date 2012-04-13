package jp.co.nemuzuka.model;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * カテゴリを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class CategoryModel extends AbsModel {

	/** カテゴリKey. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** カテゴリ名. */
	@Attribute(unindexed=true)
	private String categoryName;

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
	 * @return categoryName
	 */
	public String getCategoryName() {
		return categoryName;
	}
	/**
	 * @param categoryName セットする categoryName
	 */
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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
