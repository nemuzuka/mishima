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

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

/**
 * 現在日付、現在日時を取得するUtils.
 * ※本プロジェクトでは、本Utilsを使用して日付を取得することとします。
 * プログラム内で new Date()等をすることを禁止します。
 * @author k-katagiri
 *
 */
public class CurrentDateUtils {

	/** インスタンス. */
	private static CurrentDateUtils currentDateUtils = new CurrentDateUtils();

	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static CurrentDateUtils getInstance() {
		return currentDateUtils;
	}

	/**
	 * 現在日付取得.
	 * 時間以降の情報は切り捨てます。
	 * @return 現在日付
	 */
	public Date getCurrentDate() {
		//US時間を取得
		Date date = new Date();
		
		//日本時間に設定
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		String jpDateStr = sdf.format(date);
		Date jpDate;
		try {
			jpDate = sdf.parse(jpDateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		jpDate = DateUtils.truncate(jpDate, Calendar.DAY_OF_MONTH);
		
		//日本時間をUS時間に変更
		sdf = new SimpleDateFormat("yyyyMMdd");
		String usDateStr = sdf.format(jpDate);
		Date usDate;
		try {
			usDate = sdf.parse(usDateStr);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		return usDate;
	}

	/**
	 * 現在日付取得.
	 * 時間以降の情報を含めます。
	 * @return 現在日付
	 */
	public Date getCurrentDateTime() {
		return new Date();
	}

	/**
	 * 最大日付取得.
	 * システム的に使用されないであろう最大日付を設定します。
	 * @return 最大日付
	 */
	public Date getMaxDate() {
		Date date = null;
		try {
			date = DateTimeUtils.createSdf("yyyy/MM/dd").parse("2999/12/31");
		} catch (ParseException e) {}

		return date;
	}

	/**
	 * 現在日時取得.
	 * @return 現在日時
	 */
	public Timestamp getCurrentTimestamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return timestamp;
	}

	/**
	 * 最大日時取得.
	 * システム的に使用されないであろう最大日時を設定します。
	 * @return 最大日時
	 */
	public Timestamp getMaxCurrentTimestamp() {
		Date date = getMaxDate();
		return new Timestamp(date.getTime());

	}
}
