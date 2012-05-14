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
package jp.co.nemuzuka.form;

import java.io.Serializable;
import java.text.SimpleDateFormat;

import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

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
	
	/** チケットNo. */
	public String no;
	
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
	
	/**
	 * @return the no
	 */
	public String getNo() {
		return no;
	}

	/**
	 * @param no the no to set
	 */
	public void setNo(String no) {
		this.no = no;
	}

	/**
	 * 検索条件作成.
	 * @param projectKeyString 対象プロジェクトKey文字列
	 * @param openStatus 未完了を意味するステータス
	 * @return 検索条件
	 */
	public TicketDao.Param createParam(String projectKeyString, String[] openStatus) {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		TicketDao.Param param = new TicketDao.Param();
		param.status = status;
		param.title = title;
		param.kind = kind;
		param.category = category;
		param.version = version;
		param.milestone = milestone;
		param.priority = priority;
		param.targetMember = targetMember;
		param.projectKeyString = projectKeyString;
		param.openStatus = openStatus;
		param.fromPeriod = ConvertUtils.toDate(fromPeriod, sdf);
		param.toPeriod = ConvertUtils.toDate(toPeriod, sdf);
		param.no = ConvertUtils.toLong(no);
		return param;
	}
}
