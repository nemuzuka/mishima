package jp.co.nemuzuka.form;

import java.io.Serializable;

/**
 * メンバー検索画面の入力Form.
 * @author kazumune
 */
public class MemberSearchForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** メンバー名. */
	public String name;
	
	/** メールアドレス. */
	public String mail;

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
}
