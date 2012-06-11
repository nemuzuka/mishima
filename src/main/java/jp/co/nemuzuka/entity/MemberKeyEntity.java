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

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

/**
 * Memberに対するメールアドレス-Key変換テーブル.
 * @author k-katagiri
 */
public class MemberKeyEntity implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 更新開始時刻. */
	//この時間を超えた場合、マスタ値を再設定する
	public Date refreshStartTime;

	/** 
	 * メールアドレス-Key変換テーブルMap.
	 * Keyはメールアドレス、valueはそれに紐付くKey情報になります。
	 */
	public Map<String, Key> map = new HashMap<String, Key>();

	/** 
	 * メールアドレス-Key文字列変換テーブルMap.
	 * Keyはメールアドレス、valueはそれに紐付くKey文字列情報になります。
	 */
	public Map<String, String> keyStringMap = new HashMap<String, String>();

	/**
	 * メールアドレス-タイムゾーン文字列変換テーブルMap.
	 * Keyはメールアドレス、valueはそれに紐付くタイムゾーン文字列情報になります。
	 */
	public Map<String, String> timeZoneMap = new HashMap<String, String>();
}
