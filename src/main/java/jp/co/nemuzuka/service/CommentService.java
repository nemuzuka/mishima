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

import com.google.appengine.api.datastore.Key;

import jp.co.nemuzuka.entity.CommentModelEx;

/**
 * Commentに関するService.
 * @author k-katagiri
 */
public interface CommentService {

	/**
	 * 一覧取得.
	 * @param refsKey 登録元Key(Todo Model or Ticket ModelのKey)
	 * @return 取得情報
	 */
	public List<CommentModelEx> getList(Key refsKey);
	
	/**
	 * コメント登録.
	 * @param refsKey 登録元Key(Todo Model or Ticket ModelのKey)
	 * @param comment コメント内容
	 * @param email 登録者のメールアドレス
	 */
	public void put(Key refsKey, String comment, String email);
	
	/**
	 * コメント削除.
	 * @param refsKey 登録元Key(Todo Model or Ticket ModelのKey)
	 * @param commentKeyString コメントKey文字列
	 * @param commentVersionNo バージョンNo
	 */
	public void delete(Key refsKey, String commentKeyString, Long commentVersionNo);
}
