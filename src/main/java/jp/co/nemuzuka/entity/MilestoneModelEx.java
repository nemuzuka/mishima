package jp.co.nemuzuka.entity;

import jp.co.nemuzuka.model.MilestoneModel;

/**
 * マイルストーン表示用Entity.
 * @author k-katagiri
 */
public class MilestoneModelEx {

	/** マイルストーンModel. */
	public MilestoneModel model;
	
	/** マイルストーン開始日. */
	//yyyyMMddフォーマットの想定
	public String startDate;
	
	/** マイルストーン終了日. */
	//yyyyMMddフォーマットの想定
	public String endDate;

	/**
	 * @return the model
	 */
	public MilestoneModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(MilestoneModel model) {
		this.model = model;
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
