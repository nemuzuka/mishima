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
