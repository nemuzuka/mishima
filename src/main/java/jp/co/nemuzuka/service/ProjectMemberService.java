package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.entity.ProjectMemberModelEx;

/**
 * ProjectMemberに関するService.
 * @author kazumune
 */
public interface ProjectMemberService {
	/**
	 * プロジェクトメンバーデータ取得.
	 * 指定したプロジェクトに紐付く全メンバーの設定情報を取得します。
	 * @param selectedProject 選択プロジェクトコード
	 * @return プロジェクトメンバーデータList
	 */
	List<ProjectMemberModelEx> getProjectMemberModelList(String selectedProject);
}
