package jp.co.nemuzuka.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;

/**
 * 日付チェック.
 * @author k-katagiri
 */
public class DateTimeChecker {

	/**
	 * 日付文字列として正しいか.
	 * yyyymmdd形式の文字列かチェックします。
	 * @param target 対象文字列
	 * @return true:日付文字列として正しい false:正しくない
	 */
	public static boolean isDay(String target) {
		return executeCheck(target, "yyyyMMdd");
	}


	/**
	 * 年月文字列として正しいか.
	 * yyyymm形式の文字列かチェックします。
	 * @param target 対象文字列
	 * @return true:年月文字列として正しい false:正しくない
	 */
	public static boolean isMonth(String target) {
		return executeCheck(target, "yyyyMM");
	}


	/**
	 * 時分文字列として正しいか.
	 * hhmmの文字列かチェックします。
	 * @param target 対象文字列
	 * @return 時分文字列として正しい場合、true
	 */
	public static boolean isHourMinute(String target) {
		return executeCheck(target, "HHmm");
	}

	/**
	 * チェックメイン.
	 * @param target 対象文字列
	 * @param patterns チェック文字列パターン
	 * @return true:パターン合致する文字列 false:パターンに合致しない
	 */
	private static boolean executeCheck(String target, String pattern) {
		if(StringUtils.isEmpty(target)) {
			return false;
		}

		if(target.length() != pattern.length()) {
			return false;
		}

		try {
			Integer.valueOf(target);
		} catch(NumberFormatException e) {
			return false;
		}

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setLenient(false);
		try {
			format.parse(target);
		} catch (ParseException e) {
			return false;
		}

		return true;
	}

}