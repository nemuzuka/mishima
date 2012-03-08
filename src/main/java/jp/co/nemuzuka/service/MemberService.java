package jp.co.nemuzuka.service;

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
}
