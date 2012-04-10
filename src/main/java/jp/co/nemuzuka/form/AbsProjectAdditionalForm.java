package jp.co.nemuzuka.form;

import java.io.Serializable;

/**
 * プロジェクト付帯情報基底Form.
 * @author kazumune
 */
public abstract class AbsProjectAdditionalForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 文字列化Key. */
	public String keyToString;
	
	/** バージョンNo. */
	public String versionNo;

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
