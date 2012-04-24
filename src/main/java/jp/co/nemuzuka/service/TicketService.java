package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.exception.NotExistTicketException;
import jp.co.nemuzuka.form.TicketCommentForm;
import jp.co.nemuzuka.form.TicketDetailForm;
import jp.co.nemuzuka.form.TicketForm;

/**
 * Ticketに関するService.
 * @author kazumune
 */
public interface TicketService {

	/**
	 * 一覧取得.
	 * @param param 検索条件
	 * @param mail ログインユーザのメールアドレス(ダッシュボード表示の場合、必須)
	 * @param isDashboard ダッシュボード表示の場合、true
	 * @return 該当レコード
	 */
	List<TicketModelEx> getList(TicketDao.Param param, String mail, boolean isDashboard);

	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @param projectKeyString プロジェクトKey文字列
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	TicketForm get(String keyString, String projectKeyString);
	
	/**
	 * 詳細情報取得.
	 * コメント情報も取得します。
	 * @param keyString キー文字列
	 * @param projectKeyString プロジェクトKey文字列
	 * @return 詳細用Form。該当レコードがなければnull
	 */
	TicketDetailForm getDetail(String keyString, String projectKeyString);
	
	/**
	 * put処理.
	 * @param form put対象Form
	 * @param projectKeyString プロジェクトKey文字列
	 * @exception NotExistTicketException 親チケット指定時、存在しないidを指定された
	 */
	void put(TicketForm form, String projectKeyString) throws NotExistTicketException;
	
	/**
	 * delete処理.
	 * keyとバージョンNoが合致するデータを削除します。
	 * @param form delete対象Form
	 * @param projectKeyString プロジェクトKey文字列
	 */
	void delete(TicketForm form, String projectKeyString);

	/**
	 * Ticketステータス更新.
	 * Ticketのステータスのみ更新します。
	 * @param form 更新対象form
	 * @param projectKeyString プロジェクトKey文字列
	 */
	void updateTicketStatus(TicketForm form, String projectKeyString);
	
	/**
	 * Ticketに紐付くコメント登録.
	 * ステータスが変更されている場合、Ticketのステータスも変更します。
	 * @param form コメント登録Form
	 * @param projectKeyString プロジェクトKey文字列
	 * @param email ログインユーザのメールアドレス
	 */
	void putComment(TicketCommentForm form, String projectKeyString, String email);

	/**
	 * Ticketに紐付くコメント削除.
	 * @param keyString　TicketのKey文字列
	 * @param commentKeyString　コメントのKey文字列
	 * @param commentVersionNo コメントのバージョン
	 * @param projectKeyString プロジェクトKey文字列
	 */
	void deleteComment(String keyString, String commentKeyString,
			Long commentVersionNo, String projectKeyString);
}
