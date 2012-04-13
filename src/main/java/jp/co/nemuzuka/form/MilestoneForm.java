package jp.co.nemuzuka.form;

/**
 * マイルストーンForm.
 * @author kazumune
 */
public class MilestoneForm extends AbsProjectAdditionalForm {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** マイルストーン名. */
	public String milestoneName;
	
	/** 開始日. */
	//yyyyMMddフォーマットの想定
	public String startDate;

	/** 終了日. */
	//yyyyMMddフォーマットの想定
	public String endDate;

	/**
	 * @return the milestoneName
	 */
	public String getMilestoneName() {
		return milestoneName;
	}

	/**
	 * @param milestoneName the milestoneName to set
	 */
	public void setMilestoneName(String milestoneName) {
		this.milestoneName = milestoneName;
	}

	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
