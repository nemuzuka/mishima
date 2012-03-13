package jp.co.nemuzuka.form;

import java.io.Serializable;
import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;

/**
 * MemberForm.
 * @author kazumune
 */
public class MemberForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 文字列化Key. */
	public String keyToString;
	
	/** メールアドレス. */
	public String mail;
	
	/** 氏名. */
	public String name;
	
	/** 権限. */
	public String authority;

	/** バージョンNo. */
	public String versionNo;
	
	//登録、変更画面構成情報
	/** 権限List. */
	public List<LabelValueBean> authorityList;
	
	/**
	 * @return authorityList
	 */
	public List<LabelValueBean> getAuthorityList() {
		return authorityList;
	}

	/**
	 * @param authorityList セットする authorityList
	 */
	public void setAuthorityList(List<LabelValueBean> authorityList) {
		this.authorityList = authorityList;
	}

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
	 * @return mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail セットする mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority セットする authority
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
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
}
