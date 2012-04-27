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

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * ステータスを管理するModel.
 * プロジェクトにつき1レコード作成されます
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class StatusModel extends AbsModel {

	/** バージョンKey. */
	//projectKeyを元に作成
	@Attribute(primaryKey=true)
	private Key key;

	/** ステータス名. */
	@Attribute(unindexed=true)
	private Text statusName;

	/** 完了とみなすステータス名. */
	@Attribute(unindexed=true)
	private Text closeStatusName;
	
	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}
	/**
	 * @return the closeStatusName
	 */
	@JSONHint(ignore=true)
	public Text getCloseStatusName() {
		return closeStatusName;
	}

	/**
	 * @return the statusName
	 */
	@JSONHint(ignore=true)
	public Text getStatusName() {
		return statusName;
	}

	/**
	 * @param statusName the statusName to set
	 */
	public void setStatusName(Text statusName) {
		this.statusName = statusName;
	}

	/**
	 * @param closeStatusName the closeStatusName to set
	 */
	public void setCloseStatusName(Text closeStatusName) {
		this.closeStatusName = closeStatusName;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}
}
