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

import java.util.ArrayList;
import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.core.entity.UserInfo;
import jp.co.nemuzuka.entity.ProjectModelEx;
import jp.co.nemuzuka.form.ProjectForm;
import jp.co.nemuzuka.model.ProjectModel;

/**
 * Projectに関するService.
 * @author kazumune
 */
public interface ProjectService {
	/**
	 * 詳細情報取得.
	 * @param  keyString キー文字列
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	ProjectForm get(String keyString);
	
	/**
	 * put処理.
	 * @param form put対象Form
	 */
	void put(ProjectForm form);
	
	/**
	 * delete処理.
	 * keyとバージョンNoが合致するデータを削除します。
	 * 本プロジェクトに紐付くメンバー情報も削除します。
	 * @param form delete対象Form
	 */
	void delete(ProjectForm form);
	
	/**
	 * 該当レコード取得.
	 * 一覧を取得します。
	 * @param projectName 検索条件：プロジェクト名
	 * @return 該当レコード
	 */
	List<ProjectModelEx> getList(String projectName);
	
	/**
	 * 該当レコード取得.
	 * ユーザ情報が参照可能なプロジェクト一覧を取得します。
	 * GAE管理者 or アプリケーション管理者であるかも設定します。
	 * @param email ユーザのメールアドレス
	 * @param gaeAdmin GAE管理者の場合、true
	 * @return 該当レコード
	 */
	TargetProjectResult getUserProjectList(String email, boolean gaeAdmin);
	
	/**
	 * 所属プロジェクト取得.
	 * ユーザが参照可能なプロジェクト一覧を取得します。
	 * @param email ユーザのメールアドレス
	 * @return 該当レコード
	 */
	List<ProjectModelEx> getUserProjectList(String email);
	
	/**
	 * 全件取得.
	 * @return 該当レコード
	 */
	List<ProjectModel> getAllList();
	
	/**
	 * UserInfo情報更新.
	 * プロジェクトKey、ログインユーザのメールアドレスを元に、UserInfoに権限を設定します。
	 * @param projectKeyString プロジェクトKey文字列
	 * @param mail ログインユーザのメールアドレス
	 * @param userInfo 設定先UserInfoインスタンス
	 */
	void setUserInfo(String projectKeyString, String mail, UserInfo userInfo);
	
	/**
	 * 戻り値.
	 * @author kazumune
	 */
	class TargetProjectResult {
		/** 参照可能プロジェクトList. */
		public List<LabelValueBean> projectList = new ArrayList<LabelValueBean>();
		/** GAE管理者 or アプリケーション管理者であるか. */
		public boolean admin = false;
	}
}
