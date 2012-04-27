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
