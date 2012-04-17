package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoForm;

/**
 * Todoに関するService.
 * @author kazumune
 */
public interface TodoService {

	/**
	 * 一覧取得.
	 * @param projectKeyToString プロジェクトKey文字列
	 * @return 該当レコード
	 */
	List<TodoModelEx> getList(TodoDao.Param param);

	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @param mail ログインユーザのメールアドレス
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	TodoForm get(String keyString, String mail);
	
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
	
}
