package jp.co.nemuzuka.utils;

import java.math.BigDecimal;

/**
 * 数値に関するチェック.
 * @author k-katagiri
 */
public class DecimalChecker {

	/**
	 * 小数点以下桁数チェック.
	 * 指定した桁数より大きいか判断します。
	 * @param target チェック対象文字列(数値文字列である必要があります)
	 * @param maxScale 指定桁数
	 * @return チェック対象文字列の小数点以下の桁数が指定桁数より大きい場合、false
	 */
	public static boolean checkScale(String target, int maxScale) {

		BigDecimal decimal = new BigDecimal(target);
		if(decimal.scale() > maxScale) {
			return false;
		}
		return true;
	}


	/**
	 * 整数部桁数チェック.
	 * 指定した桁数より大きいか判断します。
	 * マイナス符号も1桁と判断します。
	 * @param target チェック対象文字列(数値文字列である必要があります)
	 * @param length 指定桁数
	 * @return チェック対象文字列の整数部分の桁数が指定桁数より大きい場合、false
	 */
	public static boolean checkInt(String target, int length) {
		BigDecimal decimal = new BigDecimal(target);
		long longValue = decimal.longValue();
		if(Long.valueOf(longValue).toString().length() > length) {
			return false;
		}
		return true;
	}
}
