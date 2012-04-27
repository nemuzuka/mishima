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

import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.utils.HtmlStringUtils;

/**
 * プロジェクトメンバー画面表示用Entity.
 * 指定したプロジェクトに対する全メンバーの設定情報を管理します。
 * @author kazumune
 */
public class ProjectMemberModelEx {
	/** MemberModel. */
	public MemberModel member;
	/** プロジェクトメンバーであれば、true. */
	public boolean projectMember;
	/** 権限コード. */
	public String authorityCode = ProjectAuthority.type3.getCode();
	/** 権限名称. */
	public String authorityName = ProjectAuthority.type3.getLabel();
	
	/**
	 * @return 表示用メモ取得
	 */
	public String getMemo() {
		if(member.getMemo() != null) {
			return HtmlStringUtils.escapeTextAreaString(member.getMemo().getValue());
		}
		return "";
	}
	
	/**
	 * @return member
	 */
	public MemberModel getMember() {
		return member;
	}
	/**
	 * @param member セットする member
	 */
	public void setMember(MemberModel member) {
		this.member = member;
	}
	/**
	 * @return projectMember
	 */
	public boolean isProjectMember() {
		return projectMember;
	}
	/**
	 * @param projectMember セットする projectMember
	 */
	public void setProjectMember(boolean projectMember) {
		this.projectMember = projectMember;
	}
	/**
	 * @return authorityCode
	 */
	public String getAuthorityCode() {
		return authorityCode;
	}
	/**
	 * @param authorityCode セットする authorityCode
	 */
	public void setAuthorityCode(String authorityCode) {
		this.authorityCode = authorityCode;
	}
	/**
	 * @return the authorityName
	 */
	public String getAuthorityName() {
		return authorityName;
	}
	/**
	 * @param authorityName the authorityName to set
	 */
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
}
