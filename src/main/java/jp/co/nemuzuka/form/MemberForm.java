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
import java.util.ArrayList;
import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.core.entity.LabelValueBean;

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
	
	/** タイムゾーン. */
	public String timeZone = jp.co.nemuzuka.common.TimeZone.GMT_P_9.getCode();
	
	/** 権限. */
	public String authority = Authority.normal.getCode();

	/** バージョンNo. */
	public String versionNo;
	
	/**
	 * タイムゾーンの選択値List取得.
	 * @return タイムゾーンの選択値List
	 */
	public List<LabelValueBean> getTimeZoneList() {
		List<LabelValueBean> retList = new ArrayList<LabelValueBean>();
		jp.co.nemuzuka.common.TimeZone[] timeZones = jp.co.nemuzuka.common.TimeZone.values();
		for(jp.co.nemuzuka.common.TimeZone target : timeZones) {
			retList.add(new LabelValueBean(target.getCode() + ":" + target.getLabel(), target.getCode()));
		}
		return retList;
	}
	
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

	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}
}
