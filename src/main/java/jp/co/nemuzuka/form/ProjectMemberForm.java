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
 * ProjectMemberForm.
 * @author k-katagiri
 */
public class ProjectMemberForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** プロジェクトに参加させるメンバーKey文字列配列. */
	public String[] memberKeyArray = new String[0];
	/** 権限Key配列. */
	public String[] authorityCodeArray = new String[0];
	/**
	 * @return the memberKeyArray
	 */
	public String[] getMemberKeyArray() {
		return memberKeyArray;
	}
	/**
	 * @param memberKeyArray the memberKeyArray to set
	 */
	public void setMemberKeyArray(String[] memberKeyArray) {
		this.memberKeyArray = memberKeyArray;
	}
	/**
	 * @return the authorityCodeArray
	 */
	public String[] getAuthorityCodeArray() {
		return authorityCodeArray;
	}
	/**
	 * @param authorityCodeArray the authorityCodeArray to set
	 */
	public void setAuthorityCodeArray(String[] authorityCodeArray) {
		this.authorityCodeArray = authorityCodeArray;
	}
}
