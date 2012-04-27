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

import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.entity.TicketModelEx;

/**
 * ガントチャート出力に関するService.
 * @author kazumune
 */
public interface GanttService {
	/**
	 * ガントチャート出力情報取得.
	 * @param param 検索条件
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @return ガントチャート出力情報
	 */
	Result getList(TicketDao.Param param, String projectKeyString);
	
	/**
	 * ガントチャート出力情報
	 * @author k-katagiri
	 */
	class Result{
		/** マイルストーン名. */
		public String milestoneName;
		
		/** 表示対象Ticket情報 */
		public List<TicketModelEx> ticketList;
	}
}
