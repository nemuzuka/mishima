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


import java.io.Serializable;
import java.util.List;

/**
 * カレンダーに関するService.
 * @author kazumune
 */
public interface CalendarService {
	
	/**
	 * 表示対象日付List取得.
	 * 取得区分が、"target"の場合、
	 * 	有効な対象年月が設定されている場合、対象年月に対する表示データを取得します。
	 * 	有効でない対象年月または対象年月が未設定の場合、システム年月に対する表示データを取得します。
	 * 取得区分が、"next"の場合、
	 * 	対象年月の翌月に対する表示データを取得します。
	 * 取得区分が、"prev"の場合、
	 * 	対象年月の前月に対する表示データを取得します。
	 * @param targetYyyyMM 対象年月
	 * @param type 取得区分
	 * @return 表示対象データ
	 */
	Result getViewDates(String targetYyyyMM, String type);
	
	/**
	 * 戻り値オブジェクト
	 * @author k-katagiri
	 */
	class Result implements Serializable {

		/**
		 * serialVersionUID.
		 */
		private static final long serialVersionUID = 1L;
		
		/** 表示年月(yyyyMM形式). */
		public String targetYyyyMM;
		
		/** 表示日(d形式). */
		public List<String> viewDates;
		
		/** システム日付(yyyyMMdd). */
		public String currentDate;
	}
}
