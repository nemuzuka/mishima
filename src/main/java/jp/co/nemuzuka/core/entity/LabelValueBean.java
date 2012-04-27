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
