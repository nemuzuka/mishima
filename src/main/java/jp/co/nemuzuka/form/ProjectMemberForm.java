package jp.co.nemuzuka.form;

import java.io.Serializable;

/**
 * ProjectMemberForm.
 * @author k-katagiri
 */
public class ProjectMemberForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** プロジェクトに参加させるメンバーKey文字列配列. */
	public String[] memberKeys = new String[0];
	/** 権限Key配列. */
	public String[] authorityCodes = new String[0];

	/**
	 * @return the memberKeys
	 */
	public String[] getMemberKeys() {
		return memberKeys;
	}
	/**
	 * @param memberKeys the memberKeys to set
	 */
	public void setMemberKeys(String[] memberKeys) {
		this.memberKeys = memberKeys;
	}
	/**
	 * @return the authorityCodes
	 */
	public String[] getAuthorityCodes() {
		return authorityCodes;
	}
	/**
	 * @param authorityCodes the authorityCodes to set
	 */
	public void setAuthorityCodes(String[] authorityCodes) {
		this.authorityCodes = authorityCodes;
	}
}
