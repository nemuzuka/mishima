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
package jp.co.nemuzuka.service;

import java.util.List;
import java.util.Map;

import com.google.appengine.api.datastore.Key;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.form.PersonForm;
import jp.co.nemuzuka.model.MemberModel;

/**
 * Memberに関するService.
 * @author kazumune
 */
public interface MemberService {
	/**
	 * Member存在チェック.
	 * Memberに登録されていなければMemberとしてputし、
	 * 登録されていれば何も処理を行いません。
	 * @param mail メールアドレス
	 * @param nickName ニックネーム
	 * @param authority 権限
	 */
	void checkAndCreateMember(String mail, String nickName, Authority authority);
	
	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	MemberForm get(String keyString);
	
	/**
	 * put処理.
	 * @param form put対象Form
	 */
	void put(MemberForm form);
	
	/**
	 * delete処理.
	 * keyとバージョンNoが合致するデータを削除します。
	 * @param form delete対象Form
	 */
	void delete(MemberForm form);
	
	/**
	 * 該当レコード取得.
	 * 一覧を取得します。
	 * @param name 検索条件：氏名
	 * @param mail 検索条件：メールアドレス
	 * @return 該当レコード
	 */
	List<MemberModel> getList(String name, String mail);
	
	/**
	 * 全件取得.
	 * 登録されている全件取得します。
	 * @return 該当レコード
	 */
	List<MemberModel> getAllList();

	/**
	 * 個人設定情報取得.
	 * メールアドレスに紐付く個人設定情報を取得します。
	 * @param email メールアドレス
	 * @return 個人設定情報
	 */
	PersonForm getPersonForm(String email);
	
	/**
	 * put処理.
	 * @param form put対象Form
	 */
	void put(PersonForm form);

	/**
	 * Map取得.
	 * 指定したKey配列に合致するデータを取得します。
	 * @param keys key配列
	 * @return 該当Map
	 */
	Map<Key, MemberModel> getMap(Key...keys);
	
	
	/**
	 * Key取得.
	 * 指定したメールアドレスに合致するMemberModelのKeyを取得します。
	 * @param mail メールアドレス
	 * @return 該当MemberKey(存在しない場合、null)
	 */
	Key getKey(String mail);
	
	/**
	 * Key文字列取得.
	 * 指定したメールアドレスに合致するMemberModelのKey文字列を取得します。
	 * @param mail メールアドレス
	 * @return 該当MemberKey文字列(存在しない場合、null)
	 */
	String getKeyString(String mail);
	
	/**
	 * タイムゾーン取得.
	 * 指定したメールアドレスに合致するMemberModelのタイムゾーンを取得します。
	 * @param mail メールアドレス
	 * @return 該当タイムゾーン文字列(存在しない場合、null)
	 */
	String getTimeZone(String mail);
	
	/**
	 * Model取得.
	 * 該当するレコードを取得します。
	 * @param mail メールアドレス
	 * @return MemberModel(存在しない場合、null)
	 */
	MemberModel getModel(String mail);
}
