package jp.co.nemuzuka.form;

/**
 * 優先度Form.
 * @author kazumune
 */
public class PriorityForm extends AbsProjectAdditionalForm {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** 優先度名. */
	public String priorityName;

	/**
	 * @return priorityName
	 */
	public String getPriorityName() {
		return priorityName;
	}

	/**
	 * @param priorityName セットする priorityName
	 */
	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

}
