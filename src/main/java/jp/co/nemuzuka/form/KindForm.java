package jp.co.nemuzuka.form;

/**
 * KindForm.
 * @author kazumune
 */
public class KindForm extends AbsProjectAdditionalForm {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** 種別名. */
	public String kindName;

	/**
	 * @return kindName
	 */
	public String getKindName() {
		return kindName;
	}

	/**
	 * @param kindName セットする kindName
	 */
	public void setKindName(String kindName) {
		this.kindName = kindName;
	}
}
