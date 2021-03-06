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

import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoCommentForm;
import jp.co.nemuzuka.form.TodoDetailForm;
import jp.co.nemuzuka.form.TodoForm;

/**
 * Todoに関するService.
 * @author kazumune
 */
public interface TodoService {

	/**
	 * 一覧取得.
	 * @param param 検索条件
	 * @param isDashboard ダッシュボード表示の場合、true
	 * @return 該当レコード
	 */
	List<TodoModelEx> getList(TodoDao.Param param, boolean isDashboard);

	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @param mail ログインユーザのメールアドレス
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	TodoForm get(String keyString, String mail);
	
	/**
	 * 詳細情報取得.
	 * コメント情報も取得します。
	 * @param keyString キー文字列
	 * @param mail ログインユーザのメールアドレス
	 * @return 詳細用Form。該当レコードがなければnull
	 */
	TodoDetailForm getDetail(String keyString, String mail);
	
	/**
	 * put処理.
	 * @param form put対象Form
	 * @param mail ログインユーザのメールアドレス
	 */
	void put(TodoForm form, String mail);
	
	/**
	 * delete処理.
	 * keyとバージョンNoが合致するデータを削除します。
	 * @param form delete対象Form
	 * @param mail ログインユーザのメールアドレス
	 */
	void delete(TodoForm form, String mail);

	/**
	 * TODOステータス更新.
	 * TODOのステータスのみ更新します。
	 * @param form 更新対象form
	 * @param email ログインユーザのメールアドレス
	 */
	void updateTodoStatus(TodoForm form, String email);
	
	/**
	 * TODOに紐付くコメント登録.
	 * ステータスが変更されている場合、TODOのステータスも変更します。
	 * @param form コメント登録Form
	 * @param email ログインユーザのメールアドレス
	 */
	void putComment(TodoCommentForm form, String email);

	/**
	 * TODOに紐付くコメント削除.
	 * @param keyString　TODOのKey文字列
	 * @param commentKeyString　コメントのKey文字列
	 * @param commentVersionNo コメントのバージョン
	 * @param email ログインユーザのメールアドレス
	 */
	void deleteComment(String keyString, String commentKeyString,
			Long commentVersionNo, String email);
}
