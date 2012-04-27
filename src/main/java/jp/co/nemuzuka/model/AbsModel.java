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
package jp.co.nemuzuka.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * Modelの基底クラス.
 * @author kazumune
 */
public abstract class AbsModel {

	//共通的に使用する
	/** バージョンNo. */
	@Attribute(version = true)
	private Long version;

	/**
	 * Key情報の取得.
	 * @return Key
	 */
	public abstract Key getKey();

	/**
	 * Key情報の設定.
	 * @param key 設定対象
	 */
	public abstract void setKey(Key key);
	
	/**
	 * Keyの文字列情報取得.
	 * @return 文字列化したKey
	 */
	public String getKeyToString() {
		return Datastore.keyToString(getKey());
	}

	/**
	 * @return version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version セットする version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}
}
