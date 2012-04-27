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
package jp.co.nemuzuka.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.time.DateUtils;

/**
 * 日付・時刻関連のUtils.
 * @author k-katagiri
 */
public class DateTimeUtils {

	/** 日曜日. */
	public static final String SUNDAY = "sunday";
	/** 月曜日. */
	public static final String MONDAY = "monday";
	/** 火曜日. */
	public static final String TUESDAY = "tuesday";
	/** 水曜日. */
	public static final String WEDNESDAY = "wednesday";
	/** 木曜日. */
	public static final String THURSDAY = "thursday";
	/** 金曜日. */
	public static final String FRIDAY = "friday";
	/** 土曜日. */
	public static final String SATURDAY = "saturday";



	/**
	 * 月初・月末取得.
	 * 引数の作成対象年月より、月初と月末のDateオブジェクトを作成します。
	 * @param targetYyyyMM 作成対象月
	 * @return index 0:月初のDate index 1:月末のDate
	 */
	public static List<Date> getStartEndDate(String targetYyyyMM) {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		sdf.setLenient(false);
		List<Date> retList = new ArrayList<Date>();
		try {
			Date startDate = sdf.parse(targetYyyyMM + "01");
			retList.add(startDate);

			//1月追加して、1日戻したものが月末
			Date endDate = DateUtils.addMonths(startDate, 1);
			endDate = DateUtils.addDays(endDate, -1);
			retList.add(endDate);

		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return retList;
	}


	/**
	 * 月初～月末List取得.
	 * 引数の作成対象年月より、月初～月末のDateオブジェクトListを作成します。
	 * @param targetYyyyMM 作成対象年月
	 * @return 月初～月末List
	 */
	public static List<Date> getStartEndDateList(String targetYyyyMM) {

		List<Date> startEndList = getStartEndDate(targetYyyyMM);
		return createDateList(startEndList.get(0), startEndList.get(1));
	}

	/**
	 * 月初・月末取得.
	 * 引数の作成対象年月より、月初と月末のDateオブジェクトを作成します。
	 * ただし、月初が日曜日でない場合、直前の日曜日まで日付を移動し、
	 * 月末が土曜日で無い場合、直後の土曜日まで日付を移動します。
	 * @param targetYyyyMM 作成対象年月日
	 * @return index 0:月初のDate index 1:月末のDate
	 */
	public static List<Date> getStartEndDate4SunDay(String targetYyyyMM) {
		List<Date> list = getStartEndDate(targetYyyyMM);
		Date startDate = list.get(0);
		Calendar calendar = Calendar.getInstance();

		//月初の設定
		while(true) {
			calendar.setTime(startDate);
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				//対象日が日曜日の場合、ループを抜ける
				break;
			}
			//1日前に戻る
			startDate = DateUtils.addDays(startDate, -1);
		}

		Date endDate = list.get(1);
		//月末の設定
		while(true) {
			calendar.setTime(endDate);
			if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				//対象日が土曜日の場合、ループを抜ける
				break;
			}
			//1日進める
			endDate = DateUtils.addDays(endDate, 1);
		}

		List<Date> retList = new ArrayList<Date>();
		retList.add(startDate);
		retList.add(endDate);
		return retList;
	}

	/**
	 * 月初～月末List取得.
	 * 引数の作成対象年月より、月初～月末のDateオブジェクトListを作成します。
	 * ただし、月初が日曜日でない場合、直前の日曜日まで日付を移動し、
	 * 月末が土曜日で無い場合、直後の土曜日まで日付を移動します。
	 * @param targetYyyyMM 作成対象年月
	 * @return 月初～月末List
	 */
	public static List<Date> getStartEndDate4SunDayList(String targetYyyyMM) {
		List<Date> startEndList = getStartEndDate4SunDay(targetYyyyMM);
		return createDateList(startEndList.get(0), startEndList.get(1));

	}

	/**
	 * 対象日の月末日を取得します。
	 * @param targetDate 取得対象日
	 * @param cal Calendarインスタンス
	 * @return 月末日
	 */
	public static int getLastDay(Date targetDate, Calendar cal) {
		cal.setTime(targetDate);
		return cal.getActualMaximum(Calendar.DATE);
	}

	/**
	 * 時間加算.
	 * 引数の時分文字列に指定した分を加算して、その結果の文字列を返却します。
	 * @param targethhmm 対象文字列(HHmmフォーマット)
	 * @param time 加算時刻(分単位)
	 * @return 加算後文字列
	 */
	public static String addTime(String targethhmm, int time) {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("HHmm");
		Date date = null;
		try {
			date = sdf.parse(targethhmm);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		date = DateUtils.addMinutes(date, time);
		return sdf.format(date);
	}


	/**
	 * システム日付の翌月の文字列を返却します。
	 * @return システム日付の翌月(yyyyMM形式)
	 */
	public static String getNextMonth() {

		//システム日付から1ヶ月加算
		Date date = CurrentDateUtils.getInstance().getCurrentDate();
		date = DateUtils.addMonths(date, 1);

		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMM");
		return sdf.format(date);
	}

	/**
	 * 週加算.
	 * 引数の対象日より、指定した週を加算します。
	 * @param targetDate 対象日
	 * @param amount 移動週(負数の場合、過去に戻る)
	 * @return 加算後の日付
	 */
	public static Date addWeek(Date targetDate, int amount) {
		//指定分の週を加算
		return DateUtils.addWeeks(targetDate, amount);
	}

	/**
	 * 日加算.
	 * 引数より指定日数を加算します。
	 * @param targetDate 対象日
	 * @param amount 移動日数(負数の場合、過去に戻る)
	 * @return 加算後の日付
	 */
	public static Date addDay(Date targetDate, int amount) {
		return DateUtils.addDays(targetDate, amount);
	}

	/**
	 * 月加算.
	 * 引数より指定月数を加算します。
	 * @param targetYyyyMm 対象年月(yyyyMM形式)
	 * @param amount 移動月数(負数の場合、過去に戻る)
	 * @return 加算後の年月(yyyyMM形式)
	 */
	public static String addMonth(String targetYyyyMm, int amount) {

		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		Date date;
		try {
			date = sdf.parse(targetYyyyMm + "01");
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		date = DateUtils.addMonths(date, amount);
		SimpleDateFormat sdf2 = DateTimeUtils.createSdf("yyyyMM");
		return sdf2.format(date);
	}

	/**
	 * 月加算.
	 * 引数より指定月数を加算します。
	 * @param date 元日付
	 * @param amount 移動月数(負数の場合、過去に戻る)
	 * @return 加算後のDateオブジェクト
	 */
	public static Date addMonth(Date date, int amount) {
		return DateUtils.addMonths(date, amount);
	}

	/**
	 * システム日付の文字列を返却します。
	 * @return システム日付(yyyyMM形式)
	 */
	public static String getMonth() {

		Date date = CurrentDateUtils.getInstance().getCurrentDate();

		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMM");
		return sdf.format(date);
	}

	/**
	 * 日数計算.
	 * 開始日から終了日まで何日あるかを返却します。
	 * 開始日 = 終了日の場合、1日を返却します。
	 * @param startDate 開始日
	 * @param endDate 終了日
	 * @return 開始日から終了日までの日数
	 */
	public static int getDays(Date startDate, Date endDate) {

		if(startDate.getTime() > endDate.getTime()) {
			//開始日 > 終了日の場合、不正データとしてエラーにする
			throw new RuntimeException();
		}

		Date targetDate = startDate;
		int days = 1;
		while(true) {
			if(targetDate.getTime() >= endDate.getTime()) {
				break;
			}
			targetDate = DateUtils.addDays(targetDate, 1);
			days++;
		}
		return days;
	}


	/**
	 * 範囲チェック.
	 * 対象となる時刻が、基準となる時刻に被るかチェックします。
	 * From <= Toの関係であることが前提条件です。
	 * @param baseFrom 基準From(HHmm形式)
	 * @param baseTo 基準To(HHmm形式)
	 * @param targetFrom 対象From(HHmm形式)
	 * @param targetTo 対象To(HHmm形式)
	 * @return 被る場合、true
	 */
	public static boolean rangeCheck(String baseFrom, String baseTo, String targetFrom, String targetTo) {
		if( DateTimeChecker.isHourMinute(baseFrom) == false || DateTimeChecker.isHourMinute(baseTo) == false ||
				DateTimeChecker.isHourMinute(baseFrom) == false || DateTimeChecker.isHourMinute(baseTo) == false ){
			return false;
		}

		int baseFromNum = Integer.valueOf(baseFrom);
		int baseToNum = Integer.valueOf(baseTo);
		int targetFromNum = Integer.valueOf(targetFrom);
		int targetToNum = Integer.valueOf(targetTo);
		if(baseFromNum > baseToNum || targetFromNum > targetToNum) {
			return false;
		}

		//基準の開始が対象From～対象Toの間に含まれる場合
		if(targetFromNum <= baseFromNum && baseFromNum < targetToNum) {
			return true;
		}

		//基準の終了が対象From～対象Toの間に含まれる場合
		if(targetFromNum < baseToNum && baseToNum <= targetToNum) {
			return true;
		}

		//基準の範囲内に収まる場合
		if(baseFromNum <= targetFromNum && targetFromNum <= baseToNum &&
				baseFromNum <= targetToNum && targetToNum <= baseToNum) {
			return true;
		}
		return false;
	}

	/**
	 * 日付List生成.
	 * 開始日から終了日までの日付Listを作成します。
	 * @param startDate 開始日
	 * @param endDate 終了日
	 * @return 生成日付List
	 */
	public static List<Date> createDateList(Date startDate, Date endDate) {
		List<Date> retList = new ArrayList<Date>();
		Date addTargetDate = startDate;
		Date endTargetDate = endDate;
		while(true) {
			retList.add(addTargetDate);
			addTargetDate = DateUtils.addDays(addTargetDate, 1);
			if(addTargetDate.getTime() > endTargetDate.getTime()) {
				//月末の日付を超えた場合、ループを抜ける
				break;
			}
		}
		return retList;
	}

	/**
	 * 分換算.
	 * 時間を分に換算します。
	 * @param targetHhMm 換算元時刻(HHmmフォーマット)
	 * @return 換算分
	 */
	public static int convertMinute(String targetHhMm) {
		String hour = targetHhMm.substring(0, 2);
		String min = targetHhMm.substring(2);
		int hourInt = Integer.parseInt(hour);
		int minInt = Integer.parseInt(min);
		return hourInt * 60 + minInt;
	}

	/**
	 * 差分算出.
	 * 引数の2つの時刻の経過分を算出します
	 * @param fromHhMm from時刻
	 * @param toHhMm to時刻
	 * @return 経過分
	 */
	public static int calcMin(String fromHhMm, String toHhMm) {
		int fromInt = convertMinute(fromHhMm);
		int toInt = convertMinute(toHhMm);
		return toInt - fromInt;
	}

	/**
	 * 引数の日付が何番目のListに格納するかを取得します。
	 * @param targetDate 取得対象日付
	 * @param dateList 検索日付List
	 * @return index(該当無しの場合、-1)
	 */
	public static int getListIndex(Date targetDate, List<Date> dateList) {
		int index = 0;
		boolean isBreak = false;
		for(Date target : dateList) {
			if(target.getTime() == targetDate.getTime()) {
				isBreak = true;
				break;
			}
			index++;
		}
		if(isBreak) {
			return index;
		}
		return -1;
	}

	/**
	 * 時刻フォーマット.
	 * @param time 設定元時刻(HHmmフォーマット)
	 * @return フォーマット済み時刻
	 */
	public static String formatTime(String time) {
		String hh = time.substring(0, 2);
		String mm = time.substring(2);
		return hh + ":" + mm;
	}

	/**
	 * 年齢算出.
	 * 生年月日と基準日を元に年齢を算出します。
	 * @param birthDay 生年月日
	 * @param baseDate 基準日
	 * @return 年齢
	 */
	public static int calcAge(Date birthDay, Date baseDate) {
		if(birthDay == null || baseDate == null) {
			throw new IllegalArgumentException("入力が不正です。");
		}

		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		int val1 = Integer.valueOf(sdf.format(baseDate));
		int val2 = Integer.valueOf(sdf.format(birthDay));

		return (val1 - val2) / 10000;
	}

	/**
	 * SimpleDateForm取得.
	 * タイムゾーンをJSTにしたSimpleDateFormatを取得します。
	 * @param pattern フォーマットパターン
	 * @return SimpleDateFormatインスタンス
	 */
	public static SimpleDateFormat createSdf(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getTimeZone("JST"));
		return sdf;
	}
}
