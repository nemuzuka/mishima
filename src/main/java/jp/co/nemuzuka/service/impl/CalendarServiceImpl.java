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
package jp.co.nemuzuka.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.co.nemuzuka.service.CalendarService;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;

/**
 * CalendarServiceの実装クラス.
 * @author kazumune
 */
public class CalendarServiceImpl implements CalendarService {

	private static CalendarServiceImpl impl = new CalendarServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static CalendarServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private CalendarServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.CalendarService#getViewDates(java.lang.String, java.lang.String)
	 */
	@Override
	public Result getViewDates(String targetYyyyMM, String type) {
		
		String target = getYyyyMM(targetYyyyMM);
		
		if("target".equals(type)) {
			//対象日はそのまま
		} else if("next".equals(type)) {
			//対象年月は翌月
			target = DateTimeUtils.addMonth(targetYyyyMM, 1);
			
		} else if("prev".equals(type)) {
			//対象年月は前月
			target = DateTimeUtils.addMonth(targetYyyyMM, -1);
			
		} else {
			throw new IllegalArgumentException("type error.");
		}
		
		//対象年月に対して、表示日付を設定する
		return createResult(target);
	}

	/**
	 * 表示データ取得.
	 * @param target 対象年月(yyyyMM形式)
	 * @return 表示データ
	 */
	private Result createResult(String target) {
		
		Result result = new Result();
		result.targetYyyyMM = target;
		SimpleDateFormat sdf = DateTimeUtils.createSdf("d");
		List<Date> targetDates = DateTimeUtils.getStartEndDate4SunDayList(target);
		result.viewDates = new ArrayList<String>();
		for(Date targetDate : targetDates) {
			result.viewDates.add(sdf.format(targetDate));
		}
		
		//システム日付文字列生成
		sdf = DateTimeUtils.createSdf("yyyyMMdd");
		result.currentDate = sdf.format(CurrentDateUtils.getInstance().getCurrentDate());
		return result;
	}

	/**
	 * 取得対象年月取得.
	 * 正しい取得対象年月が設定されている場合、引数の値を返します。
	 * 不正な対象年月が渡された場合、または、未設定の場合、システム年月の値を返します。
	 * @param targetYyyyMM 取得対象年月
	 * @return 取得対象年月(yyyyMM形式)
	 */
	private String getYyyyMM(String targetYyyyMM) {
		
		if(StringUtils.isNotEmpty(targetYyyyMM)) {
			//設定されている場合
			SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMM");
			sdf.setLenient(false);
			try {
				sdf.parse(targetYyyyMM);
				//フォーマットに問題なければそのデータ
				return targetYyyyMM;
			} catch(ParseException e) {}
		}
		
		//システム年月のデータを返す
		return DateTimeUtils.getMonth();
	}
}
