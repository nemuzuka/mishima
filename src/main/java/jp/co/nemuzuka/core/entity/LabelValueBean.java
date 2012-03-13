package jp.co.nemuzuka.core.entity;

import java.io.Serializable;

/**
 * ドロップダウンの構成情報.
 * @author kazumune
 */
public class LabelValueBean implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** Label. */
	private String label;
	/** Value. */
	private String value;
	
	/**
	 * コンストラクタ.
	 * @param label Label
	 * @param value Value
	 */
	public LabelValueBean(String label, String value) {
		this.label = label;
		this.value = value;
	}

	/**
	 * @return label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label セットする label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value セットする value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
