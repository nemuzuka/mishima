package jp.co.nemuzuka.form;

import java.io.Serializable;

import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;

/**
 * チケット登録・更新Form
 * @author k-katagiri
 */
public class TicketForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 文字列化Key. */
	public String keyToString;
	
	/** ステータス. */
	public String status;

	/** 件名. */
	public String title;
	
	/** 内容. */
	public String content;

	/** 終了条件. */
	public String endCondition;

	/** 期限. */
	public String period;
	
	/** 優先度. */
	public String priority;
	
	/** 種別. */
	public String targetKind;
	
	/** カテゴリ. */
	public String category;

	/** マイルストーン. */
	public String milestone;
	
	/** 対応バージョン. */
	public String targetVersion;
	
	/** 対応メンバーKey. */
	public String targetMember;

	/** 親チケットKey. */
	public String parentKey;
	
	/** バージョンNo. */
	public String versionNo;
	
	//構成情報
	/** チケットマスタ情報. */
	public TicketMst ticketMst;

	/**
	 * @return the keyToString
	 */
	public String getKeyToString() {
		return keyToString;
	}

	/**
	 * @param keyToString the keyToString to set
	 */
	public void setKeyToString(String keyToString) {
		this.keyToString = keyToString;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the endCondition
	 */
	public String getEndCondition() {
		return endCondition;
	}

	/**
	 * @param endCondition the endCondition to set
	 */
	public void setEndCondition(String endCondition) {
		this.endCondition = endCondition;
	}

	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @param priority the priority to set
	 */
	public void setPriority(String priority) {
		this.priority = priority;
	}

	/**
	 * @return the targetKind
	 */
	public String getTargetKind() {
		return targetKind;
	}

	/**
	 * @param targetKind the targetKind to set
	 */
	public void setTargetKind(String targetKind) {
		this.targetKind = targetKind;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @return the milestone
	 */
	public String getMilestone() {
		return milestone;
	}

	/**
	 * @param milestone the milestone to set
	 */
	public void setMilestone(String milestone) {
		this.milestone = milestone;
	}

	/**
	 * @return the targetVersion
	 */
	public String getTargetVersion() {
		return targetVersion;
	}

	/**
	 * @param targetVersion the targetVersion to set
	 */
	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	/**
	 * @return the targetMember
	 */
	public String getTargetMember() {
		return targetMember;
	}

	/**
	 * @param targetMember the targetMember to set
	 */
	public void setTargetMember(String targetMember) {
		this.targetMember = targetMember;
	}

	/**
	 * @return the parentKey
	 */
	public String getParentKey() {
		return parentKey;
	}

	/**
	 * @param parentKey the parentKey to set
	 */
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}

	/**
	 * @return the versionNo
	 */
	public String getVersionNo() {
		return versionNo;
	}

	/**
	 * @param versionNo the versionNo to set
	 */
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * @return the ticketMst
	 */
	public TicketMst getTicketMst() {
		return ticketMst;
	}

	/**
	 * @param ticketMst the ticketMst to set
	 */
	public void setTicketMst(TicketMst ticketMst) {
		this.ticketMst = ticketMst;
	}
}
