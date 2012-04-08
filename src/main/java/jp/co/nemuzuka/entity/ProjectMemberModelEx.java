package jp.co.nemuzuka.entity;

import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.model.MemberModel;

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
}
