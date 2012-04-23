package jp.co.nemuzuka.form;

import java.io.Serializable;

import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;

/**
 * チケット検索画面の入力Form.
 * @author kazumune
 */
public class TicketSearchForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** ステータス. */
	public String[] status = new String[0];
	
	/** 件名. */
	public String title;

	/** 種別. */
	public String kind;

	/** カテゴリ. */
	public String category;

	/** バージョン. */
	public String version;

	/** マイルストーン. */
	public String milestone;
	
	/** 優先度. */
	public String priority;
	
	/** 担当者. */
	public String targetMember;
	
	/** 期限From. */
	public String fromPeriod;
	
	/** 期限To. */
	public String toPeriod;

	//検索条件構成情報
	/** チケットマスタ情報. */
	public TicketMst ticketMst;

	/**
	 * @return the status
	 */
	public String[] getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String[] status) {
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
	 * @return the kind
	 */
	public String getKind() {
		return kind;
	}

	/**
	 * @param kind the kind to set
	 */
	public void setKind(String kind) {
		this.kind = kind;
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
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
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
	 * @return the fromPeriod
	 */
	public String getFromPeriod() {
		return fromPeriod;
	}

	/**
	 * @param fromPeriod the fromPeriod to set
	 */
	public void setFromPeriod(String fromPeriod) {
		this.fromPeriod = fromPeriod;
	}

	/**
	 * @return the toPeriod
	 */
	public String getToPeriod() {
		return toPeriod;
	}

	/**
	 * @param toPeriod the toPeriod to set
	 */
	public void setToPeriod(String toPeriod) {
		this.toPeriod = toPeriod;
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
