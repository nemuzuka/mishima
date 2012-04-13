package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.model.VersionModel;

/**
 * Versionに関するService.
 * @author kazumune
 */
public interface VersionService {
	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	VersionForm get(String keyString);
	
	/**
	 * put処理.
	 * @param form put対象Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	void put(VersionForm form, String projectKeyToString);
	
	/**
	 * delete処理.
	 * keyとバージョンNoが合致するデータを削除します。
	 * @param form delete対象Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	void delete(VersionForm form, String projectKeyToString);
	
	/**
	 * 全件取得.
	 * @param projectKeyToString プロジェクトKey文字列
	 * @return 該当レコード
	 */
	List<VersionModel> getAllList(String projectKeyToString);
	
	/**
	 * ソート順更新.
	 * 更新Key文字列配列の順番でソート順を更新します。
	 * @param sortedKeyToString 更新Key文字列配列
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	void updateSortNum(String[] sortedKeyToString, String projectKeyToString);
}
