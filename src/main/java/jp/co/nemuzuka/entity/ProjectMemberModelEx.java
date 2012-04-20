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
