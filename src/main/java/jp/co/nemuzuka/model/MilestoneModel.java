package jp.co.nemuzuka.model;

import java.util.Date;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * マイルストーンを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class MilestoneModel extends AbsModel {

	/** バージョンKey. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** マイルストーン名. */
	@Attribute(unindexed=true)
	private String milestoneName;

	/** 開始日. */
	@Attribute(unindexed=true)
	private Date startDate;
	
	/** 終了日. */
	@Attribute(unindexed=true)
	private Date endDate;
	
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
	 * @return the startDate
	 */
	@JSONHint(ignore=true)
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @return the endDate
	 */
	@JSONHint(ignore=true)
	public Date getEndDate() {
		return endDate;
	}
	
	/**
	 * @return the milestoneName
	 */
	public String getMilestoneName() {
		return milestoneName;
	}
	/**
	 * @param milestoneName the milestoneName to set
	 */
	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
