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

import jp.co.nemuzuka.model.TodoTagModel;

/**
 * Todoタグに関するService.
 * @author kazumune
 */
public interface TodoTagService {

	/**
	 * 一覧取得.
	 * 登録されているTODOタグ一覧を取得します。
	 * @param mail ログインユーザのメールアドレス
	 * @return 該当レコード
	 */
	List<TodoTagModel> getList(String mail);

	/**
	 * put処理.
	 * 登録されていないものに対して、put処理を行います。
	 * @param tagNames 設定TODOタグ名配列
	 * @param mail ログインユーザのメールアドレス
	 */
	void put(String[] tagNames, String mail);
	
	/**
	 * delete処理.
	 * keyとバージョンNoが合致するデータを削除します。
	 * @param keyString TODOタグKey文字列
	 * @param version バージョンNo
	 * @param mail ログインユーザのメールアドレス
	 */
	void delete(String keyString, Long version, String mail);
}
