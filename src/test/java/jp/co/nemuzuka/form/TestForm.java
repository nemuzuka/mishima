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

import java.io.Serializable;

/**
 * テスト用Form.
 * @author kazumune
 */
public class TestForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** ダミー. */
	private String dummy;
	/** memo. */
	private String memo;
	/**
	 * @return dummy
	 */
	public String getDummy() {
		return dummy;
	}
	/**
	 * @param dummy セットする dummy
	 */
	public void setDummy(String dummy) {
		this.dummy = dummy;
	}
	/**
	 * @return memo
	 */
	public String getMemo() {
		return memo;
	}
	/**
	 * @param memo セットする memo
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
