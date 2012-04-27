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
