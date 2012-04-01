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
