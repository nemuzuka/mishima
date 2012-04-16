package jp.co.nemuzuka.form;

/**
 * ステータスForm.
 * @author kazumune
 */
public class StatusForm extends AbsProjectAdditionalForm {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** ステータス名. */
	public String statusName;

	/** 完了とみなすステータス名. */
	public String closeStatusName;

	/**
	 * @return the statusName
	 */
	public String getStatusName() {
		return statusName;
	}

	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	/**
	 * @return the closeStatusName
	 */
	public String getCloseStatusName() {
		return closeStatusName;
	}

	/**
	 * @param closeStatusName the closeStatusName to set
	 */
	public void setCloseStatusName(String closeStatusName) {
		this.closeStatusName = closeStatusName;
	}
}
