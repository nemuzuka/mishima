package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.entity.MilestoneModelEx;
import jp.co.nemuzuka.form.MilestoneForm;

/**
 * Milestoneに関するService.
 * @author kazumune
 */
public interface MilestoneService {
	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	MilestoneForm get(String keyString);
	
	/**
	 * put処理.
	 * 更新後、チケットマスタのキャッシュ更新を行わせます
	 * @param form put対象Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	void put(MilestoneForm form, String projectKeyToString);
	
	/**
	 * delete処理.
	 * 更新後、チケットマスタのキャッシュ更新を行わせます
	 * keyとバージョンNoが合致するデータを削除します。
	 * @param form delete対象Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	void delete(MilestoneForm form, String projectKeyToString);
	
	/**
	 * 全件取得.
	 * @param projectKeyToString プロジェクトKey文字列
	 * @return 該当レコード
	 */
	List<MilestoneModelEx> getAllList(String projectKeyToString);
	
	/**
	 * ソート順更新.
	 * 更新Key文字列配列の順番でソート順を更新します。
	 * 更新後、チケットマスタのキャッシュ更新を行わせます
	 * @param sortedKeyToString 更新Key文字列配列
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	void updateSortNum(String[] sortedKeyToString, String projectKeyToString);
}
