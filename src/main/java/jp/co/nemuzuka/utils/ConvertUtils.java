package jp.co.nemuzuka.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

}
