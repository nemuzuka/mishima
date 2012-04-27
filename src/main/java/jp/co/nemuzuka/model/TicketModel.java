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
package jp.co.nemuzuka.model;

import java.util.Date;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * Ticketを管理するModel.
 * @author k-katagiri
 */
@Model(schemaVersion = 1)
public class TicketModel extends AbsModel {

	/** TicketKey. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** プロジェクトKey. */
	private Key projectKey;
	
	/** 件名. */
	private String title;
	
	/** 内容. */
	@Attribute(unindexed=true)
	private Text content;

	/** 終了条件. */
	@Attribute(unindexed=true)
	private Text endCondition;

	/** 優先度. */
	private String priority;
	
	/** ステータス. */
	private String status;
	
	/** 種別. */
	private String targetKind;
	
	/** カテゴリ. */
	private String category;
	
	/** マイルストーンKey. */
	//マイルストーンはKey参照
	private Key milestone;
	
	/** 対応バージョン. */
	private String targetVersion;
	
	/** 対応メンバーKey. */
	private Key targetMemberKey;
	
	/** 期限. */
	private Date period;
	
	/** 親TicketKey. */
	private Key parentTicketKey;
	
	/** チケットNo. */
	private Long no;
	
	/** 登録日時. */
	@Attribute(listener = CreationDate.class, unindexed=true)
	private Date createdAt;

	/**
	 * @return the key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}
	/**
	 * @return the createdAt
	 */
	@JSONHint(ignore=true)
	public Date getCreatedAt() {
		return createdAt;
	}
	/**
	 * @return the content
	 */
	@JSONHint(ignore=true)
	public Text getContent() {
		return content;
	}
	/**
	 * @return the endCondition
	 */
	@JSONHint(ignore=true)
	public Text getEndCondition() {
		return endCondition;
	}
	/**
	 * @return the targetMemberKey
	 */
	@JSONHint(ignore=true)
	public Key getTargetMemberKey() {
		return targetMemberKey;
	}

	/**
	 * @return the projectKey
	 */
	@JSONHint(ignore=true)
	public Key getProjectKey() {
		return projectKey;
	}
	/**
	 * @return the milestone
	 */
	@JSONHint(ignore=true)
	public Key getMilestone() {
		return milestone;
	}
	/**
	 * @return the period
	 */
	@JSONHint(ignore=true)
	public Date getPeriod() {
		return period;
	}

	/**
	 * @return the parentTicketKey
	 */
	@JSONHint(ignore=true)
	public Key getParentTicketKey() {
		return parentTicketKey;
	}
	
	/**
	 * @param parentTicketKey the parentTicketKey to set
	 */
	public void setParentTicketKey(Key parentTicketKey) {
		this.parentTicketKey = parentTicketKey;
	}
	/**
	 * @param projectKey the projectKey to set
	 */
	public void setProjectKey(Key projectKey) {
		this.projectKey = projectKey;
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
	 * @param content the content to set
	 */
	public void setContent(Text content) {
		this.content = content;
	}

	/**
	 * @param endCondition the endCondition to set
	 */
	public void setEndCondition(Text endCondition) {
		this.endCondition = endCondition;
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
	 * @param milestone the milestone to set
	 */
	public void setMilestone(Key milestone) {
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
	 * @param targetMemberKey the targetMemberKey to set
	 */
	public void setTargetMemberKey(Key targetMemberKey) {
		this.targetMemberKey = targetMemberKey;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Date period) {
		this.period = period;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}
	/**
	 * @return the no
	 */
	public Long getNo() {
		return no;
	}
	/**
	 * @param no the no to set
	 */
	public void setNo(Long no) {
		this.no = no;
	}

}
