package jp.co.nemuzuka.service;


import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.form.KindForm;

/**
 * Kindに関するService.
 * @author kazumune
 */
public interface KindService {
	/**
	 * 詳細情報取得.
	 * @param  projectKeyString キー文字列(プロジェクトKey)
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	KindForm get(String projectKeyString);
	
	/**
	 * put処理.
	 * 更新後、チケットマスタのキャッシュ更新を行って下さい
	 * @param form put対象Form
	 * @param  projectKeyString キー文字列(プロジェクトKey)
	 */
	void put(KindForm form, String projectKeyString);
	
	/**
	 * 画面表示情報取得.
	 * キー文字列に紐付く情報を取得し、画面表示情報に生成します。
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @return 画面表示情報
	 */
	List<LabelValueBean> getList(String projectKeyString);
}
