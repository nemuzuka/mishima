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
package jp.co.nemuzuka.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.arnx.jsonic.JSONHint;

import jp.co.nemuzuka.core.entity.LabelValueBean;

/**
 * チケット用選択肢マスタ.
 * @author k-katagiri
 */
public class TicketMstEntity implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * チケットマスタMap.
	 * KeyはプロジェクトKey文字列、valueはそれに紐付くマスタ情報になります。
	 */
	public Map<String, TicketMst> map = new HashMap<String, TicketMstEntity.TicketMst>();
	
	/**
	 * チケットマスタ.
	 * @author k-katagiri
	 */
	public static class TicketMst implements Serializable {
		
		/**
		 * serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;
		
		/** 未完了を意味するコード値. */
		public static final String NO_FINISH = "-1";
		/** 未完了を意味するラベル. */
		public static final String NO_FINISH_LABEL = "未完了";
		
		/** 更新開始時刻. */
		//この時間を超えた場合、チケットマスタの値を再設定する
		@JSONHint(ignore=true)
		public Date refreshStartTime;
		
		/** 優先度選択肢. */
		public List<LabelValueBean> priorityList;
		
		/** ステータス選択肢. */
		public List<LabelValueBean> statusList;

		/** 未完了を意味するステータス. */
		public String[] openStatus;
		
		/** ステータス検索条件. */
		public List<LabelValueBean> searchStatusList;

		/** 種別選択肢. */
		public List<LabelValueBean> kindList;
		
		/** カテゴリ選択肢. */
		public List<LabelValueBean> categoryList;
		
		/** マイルストーン選択肢. */
		public List<LabelValueBean> milestoneList;
		
		/** バージョン選択肢. */
		public List<LabelValueBean> versionList;
		
		/** メンバー選択肢. */
		public List<LabelValueBean> memberList;
		
	}
}
