/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.nemuzuka.entity;

import jp.co.nemuzuka.common.PeriodStatus;
import jp.co.nemuzuka.model.TicketModel;
import net.arnx.jsonic.JSONHint;

/**
 * Ticket画面表示用Entity.
 * @author k-katagiri
 */
public class TicketModelEx {

	/** Ticket情報. */
	private TicketModel model;

	/** 開始日. */
	//yyyyMMddフォーマット
	private String startDate;
	/** 上書きされた場合、true. */
	private boolean updateStartDate;

	/** 期限. */
	//yyyyMMddフォーマット
	private String period;
	/** 上書きされた場合、true. */
	private boolean updatePeriod;
	
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

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return updateStartDate
	 */
	public boolean isUpdateStartDate() {
		return updateStartDate;
	}

	/**
	 * @param updateStartDate セットする updateStartDate
	 */
	public void setUpdateStartDate(boolean updateStartDate) {
		this.updateStartDate = updateStartDate;
	}

	/**
	 * @return updatePeriod
	 */
	public boolean isUpdatePeriod() {
		return updatePeriod;
	}

	/**
	 * @param updatePeriod セットする updatePeriod
	 */
	public void setUpdatePeriod(boolean updatePeriod) {
		this.updatePeriod = updatePeriod;
	}
}
