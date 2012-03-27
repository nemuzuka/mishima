package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.model.MemberModel;

/**
 * Memberに関するService.
 * @author kazumune
 */
public interface MemberService {
	/**
	 * Member存在チェック.
	 * Memberに登録されていれば管理者としてputし、
	 * 登録されていなければ何も処理を行いません。
	 * @param mail メールアドレス
	 * @param nickName ニックネーム
	 */
	void checkAndCreateAdminMember(String mail, String nickName);
	
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
	
}
