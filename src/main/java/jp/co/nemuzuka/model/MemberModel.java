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

import jp.co.nemuzuka.common.Authority;
import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * アプリケーションに登録されているユーザを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class MemberModel extends AbsModel {

	/** メンバーKey. */
	//mailアドレスから生成
	@Attribute(primaryKey=true)
	private Key key;

	/** メールアドレス. */
	private String mail;
	
	/** 氏名. */
	private String name;
	
	/** 
	 * 権限. 
	 * 管理者：システムにユーザを追加できる、プロジェクトを追加できる、プロジェクト管理者扱い
	 * 一般:新規にユーザやプロジェクトは追加できない
	 */
	@Attribute(unindexed=true)
	private Authority authority;
	
	/** メモ. */
	@Attribute(unindexed=true)
	private Text memo;
	
	/** タイムゾーン. */
	@Attribute(unindexed=true)
	private String timeZone;
	
	/**
	 * 権限Label取得.
	 * @return 権限Label
	 */
	public String getAuthorityLabel() {
		return authority.getLabel();
	}
	
	/**
	 * Key生成.
	 * 引数のメールアドレスでKeyを生成します。
	 * メンバ変数のメールアドレスにも設定します。
	 * @param mail メールアドレス
	 * @return 生成Key
	 */
	public Key createKey(String mail) {
		this.mail = mail;
		key = Datastore.createKey(MemberModel.class, mail);
		return key;
	}

	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}
	/**
	 * @return the memo
	 */
	@JSONHint(ignore=true)
	public Text getMemo() {
		return memo;
	}

	/**
	 * @param key セットする key
	 */
	public void setKey(Key key) {
		this.key = key;
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
	public Authority getAuthority() {
		return authority;
	}

	/**
	 * @param authority セットする authority
	 */
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}

	/**
	 * @param memo the memo to set
	 */
	public void setMemo(Text memo) {
		this.memo = memo;
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
