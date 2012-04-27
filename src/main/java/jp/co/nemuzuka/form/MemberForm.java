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
 * MemberForm.
 * @author kazumune
 */
public class MemberForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 文字列化Key. */
	public String keyToString;
	
	/** メールアドレス. */
	public String mail;
	
	/** 氏名. */
	public String name;
	
	/** 権限. */
	public String authority = "normal";

	/** バージョンNo. */
	public String versionNo;
	
	/**
	 * @return keyToString
	 */
	public String getKeyToString() {
		return keyToString;
	}

	/**
	 * @param keyToString セットする keyToString
	 */
	public void setKeyToString(String keyToString) {
		this.keyToString = keyToString;
	}

	/**
	 * @return mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail セットする mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return authority
	 */
	public String getAuthority() {
		return authority;
	}

	/**
	 * @param authority セットする authority
	 */
	public void setAuthority(String authority) {
		this.authority = authority;
	}

	/**
	 * @return versionNo
	 */
	public String getVersionNo() {
		return versionNo;
	}

	/**
	 * @param versionNo セットする versionNo
	 */
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
}
