package jp.co.nemuzuka.form;

import java.io.Serializable;

/**
 * テスト用Form.
 * @author kazumune
 */
public class TestForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** ダミー. */
	private String dummy;
	/** memo. */
	private String memo;
	/**
	 * @return dummy
	 */
	public String getDummy() {
		return dummy;
	}
	/**
	 * @param dummy セットする dummy
	 */
	public void setDummy(String dummy) {
		this.dummy = dummy;
	}
	/**
	 * @return memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo セットする memo
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
