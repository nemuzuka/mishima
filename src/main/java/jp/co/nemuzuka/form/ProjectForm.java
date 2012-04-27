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
import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;

/**
 * ProjectForm.
 * @author kazumune
 */
public class ProjectForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 文字列化Key. */
	public String keyToString;
	
	/** プロジェクト名. */
	public String projectName;
	
	/** プロジェクト識別子. */
	public String projectId;
	
	/** プロジェクト概要. */
	public String projectSummary;

	/** 管理者ユーザID. */
	//新規の時だけ有効
	public String adminMemberId;
	
	/** バージョンNo. */
	public String versionNo;
	
	/** ユーザ一覧List. */
	public List<LabelValueBean> memberList;

	/**
	 * @return keyToString
	 */
	public String getKeyToString() {
		return keyToString;
	}

	/**
	 * @param keyToString セットする keyToString
	 */
	public void setKeyToString(String keyToString) {
		this.keyToString = keyToString;
	}

	/**
	 * @return projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * @param projectName セットする projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId セットする projectId
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	/**
	 * @return projectSummary
	 */
	public String getProjectSummary() {
		return projectSummary;
	}

	/**
	 * @param projectSummary セットする projectSummary
	 */
	public void setProjectSummary(String projectSummary) {
		this.projectSummary = projectSummary;
	}

	/**
	 * @return adminMemberId
	 */
	public String getAdminMemberId() {
		return adminMemberId;
	}

	/**
	 * @param adminMemberId セットする adminMemberId
	 */
	public void setAdminMemberId(String adminMemberId) {
		this.adminMemberId = adminMemberId;
	}

	/**
	 * @return versionNo
	 */
	public String getVersionNo() {
		return versionNo;
	}

	/**
	 * @param versionNo セットする versionNo
	 */
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}

	/**
	 * @return memberList
	 */
	public List<LabelValueBean> getMemberList() {
		return memberList;
	}

	/**
	 * @param memberList セットする memberList
	 */
	public void setMemberList(List<LabelValueBean> memberList) {
		this.memberList = memberList;
	}
}
