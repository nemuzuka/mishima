/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
