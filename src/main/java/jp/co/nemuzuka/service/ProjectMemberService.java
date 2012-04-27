/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
	 * 指定したプロジェクトに紐付く、システムに登録されている全メンバーの設定情報を取得します。
	 * セキュリティ確保の為、メールアドレスは空文字に設定します。
	 * @param selectedProject 選択プロジェクトKey文字列
	 * @return プロジェクトメンバーデータList(プロジェクトに参加していないユーザも含む)
	 */
	List<ProjectMemberModelEx> getProjectMemberModelList(String selectedProject);
	
	/**
	 * プロジェクトメンバーデータ取得.
	 * 指定したプロジェクトに参加しているメンバーの情報を取得します。
	 * セキュリティ確保の為、メールアドレスは空文字に設定します。
	 * @param selectedProject 選択プロジェクトKey文字列
	 * @return プロジェクトメンバーList(プロジェクトに参加しているユーザのみ)
	 */
	List<ProjectMemberModelEx> getProjectMemberOnlyModelList(String selectedProject);
	
	/**
	 * プロジェクトメンバーデータ更新.
	 * 指定したプロジェクトに紐付くメンバーの設定情報を更新します。
	 * 登録データを一度全て削除した後、appendします。
	 * @param selectedProject 選択プロジェクトKey文字列
	 * @param form 更新Form情報
	 */
	void updateProjectMember(String selectedProject, ProjectMemberForm form);
	
}
