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
