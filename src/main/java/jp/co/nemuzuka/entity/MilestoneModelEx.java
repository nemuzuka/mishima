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
