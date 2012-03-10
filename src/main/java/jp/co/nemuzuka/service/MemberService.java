package jp.co.nemuzuka.service;

import java.util.List;

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
	 */
	void checkAndCreateAdminMember(String mail);
	
	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @return 該当レコードがあればインスタンス。該当レコードがなければnull
	 */
	MemberModel get(String keyString);
	
	/**
	 * put処理.
	 * @param model put対象Model
	 */
	void put(MemberModel model);
	
	/**
	 * 該当レコード取得.
	 * 一覧を取得します。
	 * @param name 検索条件：氏名
	 * @return 該当レコード
	 */
	List<MemberModel> getList(String name);
	
	/**
	 * 全件取得.
	 * 登録されている全件取得します。
	 * @return 該当レコード
	 */
	List<MemberModel> getAllList();
	
}
