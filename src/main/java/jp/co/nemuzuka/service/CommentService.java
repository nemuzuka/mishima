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
