package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.entity.ProjectMemberModelEx;
import jp.co.nemuzuka.form.ProjectMemberForm;

/**
 * ProjectMemberに関するService.
 * @author kazumune
 */
public interface ProjectMemberService {
	/**
	 * プロジェクトメンバーデータ取得.
	 * 指定したプロジェクトに紐付く全メンバーの設定情報を取得します。
	 * @param selectedProject 選択プロジェクトKey文字列
	 * @return プロジェクトメンバーデータList
	 */
	List<ProjectMemberModelEx> getProjectMemberModelList(String selectedProject);
	
	/**
	 * プロジェクトメンバーデータ更新.
	 * 指定したプロジェクトに紐付くメンバーの設定情報を更新します。
	 * 登録データを一度全て削除した後、appendします。
	 * @param selectedProject 選択プロジェクトKey文字列
	 * @param form 更新Form情報
	 */
	void updateProjectMember(String selectedProject, ProjectMemberForm form);
	
}
