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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * 各種コンバートユーティリティ.
 * @author k-katagiri
 */
public class ConvertUtils {

	/**
	 * String配列→Long配列変換.
	 * @param targets 処理対象配列
	 * @return 生成後Long配列(処理対象配列がnullの場合、サイズ0の配列)
	 */
	public static Long[] convert(String[] targets) {
		if(targets == null) {
			return new Long[0];
		}

		List<Long> list = new ArrayList<Long>();
		for(String target : targets) {
			list.add(Long.valueOf(target));
		}
		return list.toArray(new Long[0]);
	}

	/**
	 * Long配列→String配列変換.
	 * @param targets 処理対象配列
	 * @return 生成後String配列(処理対象配列がnullの場合、サイズ0の配列)
	 */
	public static String[] convert(Long[] targets) {
		if(targets == null) {
			return new String[0];
		}

		List<String> list = new ArrayList<String>();
		for(Long target : targets) {
			list.add(target.toString());
		}
		return list.toArray(new String[0]);
	}

	/**
	 * Date変換.
	 * @param source 文字列
	 * @param sdf 日付変換フォーマットオブジェクト
	 * @return Dateオブジェクト
	 */
	public static Date toDate(String source, SimpleDateFormat sdf) {

		if(StringUtils.isEmpty(source)) {
			return null;
		}

		try {
			return sdf.parse(source);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Long変換.
	 * @param source 文字列
	 * @return Longオブジェクト
	 */
	public static Long toLong(String source) {
		if(StringUtils.isEmpty(source)) {
			return null;
		}
		return Long.valueOf(source);
	}

	/**
	 * Integer変換.
	 * @param source 文字列
	 * @return Integerオブジェクト
	 */
	public static Integer toInteger(String source) {
		if(StringUtils.isEmpty(source)) {
			return null;
		}
		return Integer.valueOf(source);
	}

	/**
	 * String→Bigdecimal変換.
	 * @param source 文字列
	 * @return BigDecimalオブジェクト
	 */
	public static BigDecimal toBigDecimal(String source) {
		if(StringUtils.isEmpty(source)) {
			return null;
		}
		return new BigDecimal(source);
	}

	/**
	 * Integer→String変換.
	 * 変換元値がnullの場合、空文字を返却します。
	 * @param val 変換元
	 * @return 変換後文字列
	 */
	public static String toString(Integer val) {
		if(val == null) {
			return "";
		}
		return String.valueOf(val);
	}

	/**
	 * Date→日付文字列変換.
	 * 変換元がnullの場合、空文字を返却します。
	 * @param date 変換元
	 * @param sdf フォーマット
	 * @return 日付文字列
	 */
	public static String toString(Date date, SimpleDateFormat sdf) {
		if(date == null) {
			return "";
		}
		return sdf.format(date);
	}

	/**
	 * Long→String変換.
	 * 変換元値がnullの場合、空文字を返却します。
	 * @param val 変換元
	 * @return 変換後文字列
	 */
	public static String toString(Long val) {
		if(val == null) {
			return "";
		}
		return String.valueOf(val);
	}

	/**
	 * Number→String変換.
	 * 変換元がnullの場合、空文字を返却します。
	 * @param val 変換元
	 * @param df フォーマット
	 * @return 数値文字列
	 */
	public static String toString(Number val, DecimalFormat df) {
		if(val == null) {
			return "";
		}
		return df.format(val);
	}

	/**
	 * String→String配列変換.
	 * 改行コードを元に、String配列に変換します。
	 * 空文字の場合、戻り値の文字列列配列には含みません。
	 * @param target 変換元
	 * @return 変換後文字列配列
	 */
	public static String[] toStringArray(String target) {
		String name = target;
		if(name == null) {
			name="";
		}
		name = name.replaceAll("\r\n", "\n");
		name = name.replaceAll("\r", "\n");
		String[] names = name.split("\n");
		Set<String> strSet = new LinkedHashSet<String>();
		
		for(String value : names) {
			if(StringUtils.isEmpty(value)) {
				continue;
			}
			strSet.add(value);
		}
		return strSet.toArray(new String[0]);
	}
}
