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
	public String[] memberKeyArray = new String[0];
	/** 権限Key配列. */
	public String[] authorityCodeArray = new String[0];
	/**
	 * @return the memberKeyArray
	 */
	public String[] getMemberKeyArray() {
		return memberKeyArray;
	}
	/**
	 * @param memberKeyArray the memberKeyArray to set
	 */
	public void setMemberKeyArray(String[] memberKeyArray) {
		this.memberKeyArray = memberKeyArray;
	}
	/**
	 * @return the authorityCodeArray
	 */
	public String[] getAuthorityCodeArray() {
		return authorityCodeArray;
	}
	/**
	 * @param authorityCodeArray the authorityCodeArray to set
	 */
	public void setAuthorityCodeArray(String[] authorityCodeArray) {
		this.authorityCodeArray = authorityCodeArray;
	}
}
