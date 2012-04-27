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

import jp.co.nemuzuka.entity.TicketMstEntity;

/**
 * チケットマスタに関するService.
 * Memcacheを使用します。
 * @author k-katagiri
 */
public interface TicketMstService {

	/**
	 * チケットマスタ取得.
	 * 指定したプロジェクトに紐付く、チケットマスタ情報を取得します。
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @return 該当チケットマスタ情報
	 */
	TicketMstEntity.TicketMst getTicketMst(String projectKeyString);
	
	/**
	 * チケットマスタ初期化.
	 * 指定したプロジェクトに紐付く、チケットマスタ情報を初期化します。
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 */
	void initRefreshStartTime(String projectKeyString);
}
