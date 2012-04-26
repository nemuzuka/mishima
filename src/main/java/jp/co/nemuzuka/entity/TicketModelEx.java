package jp.co.nemuzuka.entity;

import net.arnx.jsonic.JSONHint;
import jp.co.nemuzuka.common.PeriodStatus;
import jp.co.nemuzuka.model.TicketModel;

/**
 * Ticket画面表示用Entity.
 * @author k-katagiri
 */
public class TicketModelEx {

	/** Ticket情報. */
	private TicketModel model;
	
	/** 期限. */
	//yyyyMMddフォーマット
	private String period;
	
	/** 登録日時. */
	//yyyy/MM/dd HH:mmフォーマット
	private String createdAt;

	/** 期限ステータス. */
	private PeriodStatus periodStatus;
	
	/** 担当者名. */
	private String targetMemberName = "";
	
	/**
	 * @return 期限ステータスコード値.
	 */
	public String getPeriodStatusCode() {
		if(periodStatus != null) {
			return periodStatus.getCode();
		}
		return "";
	}

	/**
	 * @return 期限ステータスラベル.
	 */
	public String getPeriodStatusLabel() {
		if(periodStatus != null) {
			return periodStatus.getLabel();
		}
		return "";
	}
	/**
	 * @return the periodStatus
	 */
	@JSONHint(ignore=true)
	public PeriodStatus getPeriodStatus() {
		return periodStatus;
	}

	/**
	 * @return the model
	 */
	public TicketModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(TicketModel model) {
		this.model = model;
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
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @param periodStatus the periodStatus to set
	 */
	public void setPeriodStatus(PeriodStatus periodStatus) {
		this.periodStatus = periodStatus;
	}

	/**
	 * @return the targetMemberName
	 */
	public String getTargetMemberName() {
		return targetMemberName;
	}

	/**
	 * @param targetMemberName the targetMemberName to set
	 */
	public void setTargetMemberName(String targetMemberName) {
		this.targetMemberName = targetMemberName;
	}
}
