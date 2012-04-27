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

import org.apache.commons.lang.StringUtils;

/**
 * 画面に表示する際の文字列を作成します。
 * @author k-katagiri
 */
public class HtmlStringUtils {

	/**
	 * textarea文字列出力.
	 * textAreaに入力されている、
	 * ・タグはエスケープ
	 * ・http:もしくはhttps:で始まる文字列は、<a/>
	 * ・\r\n/\r/\nは<br />に変換します。
	 * @param target エスケープ文字列
	 * @return エスケープ後文字列
	 */
	public static final String escapeTextAreaString(String target) {

		if(StringUtils.isEmpty(target)) {
			return "";
		}

		//タグをエスケープする
		String resultStr = escape(target);
		resultStr = getATagString(resultStr);
		return replaseNl(resultStr);
	}

	/**
	 * 改行タグ化.
	 * 文字列中の\r\n/\r/\nは<br />に変換します
	 * @param target 対象文字列
	 * @return 変換後文字列
	 */
	private static String replaseNl(String target) {
		String result = target.replaceAll("\r\n", "\n");
		result = result.replaceAll("\r", "\n");
		return result.replaceAll("\n", "<br />");
	}

	/**
	 * http文字列変換.
	 * 引数の文字列の内、http/httpsで始まる文字列を<a/>で評価します。
	 * @param str 対象文字列
	 * @return aタグ変換後文字列
	 */
	private static String getATagString(String str) {
		return str.replaceAll("(http://|https://){1}[\\w\\.\\-/:%]+","<a href='$0' target='_blank'>$0</a>");
	}


    /**
     * タグエスケープ処理.
     * タグをエスケープします。
     * @param value 対象文字列
     * @return エスケープ後文字列
     */
    private static String escape(String value) {

        if (value == null || value.length() == 0) {
            return value;
        }

        StringBuffer result = null;
        String filtered = null;
        for (int i = 0; i < value.length(); i++) {
            filtered = null;
            switch (value.charAt(i)) {
                case '<':
                    filtered = "&lt;";
                    break;
                case '>':
                    filtered = "&gt;";
                    break;
                case '&':
                    filtered = "&amp;";
                    break;
                case '"':
                    filtered = "&quot;";
                    break;
                case '\'':
                    filtered = "&#39;";
                    break;
            }

            if (result == null) {
                if (filtered != null) {
                    result = new StringBuffer(value.length() + 50);
                    if (i > 0) {
                        result.append(value.substring(0, i));
                    }
                    result.append(filtered);
                }
            } else {
                if (filtered == null) {
                    result.append(value.charAt(i));
                } else {
                    result.append(filtered);
                }
            }
        }

        return result == null ? value : result.toString();
    }

	/**
	 * 住所文字列作成.
	 * 引数の情報を元に、文字列を生成します。
	 * 本項目を出力するときは、エスケープ無しで出力して下さい。
	 * zipCode<br>
	 * prefecturalCapital cities townName buildingName
	 * @param zipCode 郵便番号
	 * @param prefecturalCapital 都道府県
	 * @param cities 市区町村
	 * @param townName 町名・番地
	 * @param buildingName ビル・マンション名
	 * @return 住所文字列
	 */
	public static String createAddress(String zipCode, String prefecturalCapital, String cities, String townName,
			String buildingName) {

		StringBuilder sb = new StringBuilder();
		if(StringUtils.isNotEmpty(zipCode)) {
			sb = sb.append(zipCode).append("\n");
		}
		if(StringUtils.isNotEmpty(prefecturalCapital)) {
			sb = sb.append(prefecturalCapital).append(" ");
		}
		if(StringUtils.isNotEmpty(cities)) {
			sb = sb.append(cities).append(" ");
		}
		if(StringUtils.isNotEmpty(townName)) {
			sb = sb.append(townName).append(" ");
		}
		if(StringUtils.isNotEmpty(buildingName)) {
			sb = sb.append(buildingName).append(" ");
		}
		return escapeTextAreaString(sb.toString());
	}
}
