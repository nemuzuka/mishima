package jp.co.nemuzuka.entity;

import net.arnx.jsonic.JSONHint;
import jp.co.nemuzuka.common.PeriodStatus;
import jp.co.nemuzuka.model.TodoModel;

/**
 * TODO画面表示用Entity.
 * @author k-katagiri
 */
public class TodoModelEx {

	/** TODO情報. */
	private TodoModel model;
	
	/** 期限. */
	//yyyyMMddフォーマット
	private String period;
	
	/** 登録日時. */
	//yyyy/MM/dd HH:mmフォーマット
	private String createdAt;

	/** 期限ステータス. */
	private PeriodStatus periodStatus;
	
	/** TODOステータス. */
	private String todoStatus = "";
	
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
	 * @return the todoStatus
	 */
	public String getTodoStatus() {
		return todoStatus;
	}

	/**
	 * @param todoStatus the todoStatus to set
	 */
	public void setTodoStatus(String todoStatus) {
		this.todoStatus = todoStatus;
	}

	/**
	 * @return the model
	 */
	public TodoModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(TodoModel model) {
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
	 * @return the periodStatus
	 */
	@JSONHint(ignore=true)
	public PeriodStatus getPeriodStatus() {
		return periodStatus;
	}

	/**
	 * @param periodStatus the periodStatus to set
	 */
	public void setPeriodStatus(PeriodStatus periodStatus) {
		this.periodStatus = periodStatus;
	}
}
