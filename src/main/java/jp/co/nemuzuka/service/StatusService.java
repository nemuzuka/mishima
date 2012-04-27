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

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.form.StatusForm;

/**
 * Statusに関するService.
 * @author kazumune
 */
public interface StatusService {
	/**
	 * 詳細情報取得.
	 * @param  projectKeyString キー文字列(プロジェクトKey)
	 * @return 該当レコードがあれば更新用Form。該当レコードがなければ新規用Form
	 */
	StatusForm get(String projectKeyString);
	
	/**
	 * put処理.
	 * 更新後、チケットマスタのキャッシュ更新を行って下さい
	 * @param form put対象Form
	 * @param  projectKeyString キー文字列(プロジェクトKey)
	 */
	void put(StatusForm form, String projectKeyString);
	
	/**
	 * 画面表示情報取得.
	 * キー文字列に紐付く情報を取得し、画面表示情報に生成します。
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @return 画面表示情報
	 */
	List<LabelValueBean> getList(String projectKeyString);
}
