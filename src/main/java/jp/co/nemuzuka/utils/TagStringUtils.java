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
 * Tagに関する文字列Utilsです。
 * @author k-katagiri
 */
public class TagStringUtils {

	/**
	 * タグマッチ判断.
	 * 引数のタグ文字列のなかに、検索条件タグ文字列が完全一致するか判断します。
	 * @param tag TODOタグ(カンマ区切りで項目を区切る)
	 * @param searchTagName 検索条件タグ名
	 * @return 完全一致するタグ名が含まれる場合、true
	 */
	public static boolean matchTag(String tag, String searchTagName) {
		
		if(StringUtils.isEmpty(tag)) {
			return false;
		}
		
		String[] tags = tag.split(",");
		for(String target : tags) {
			String tagName = StringUtils.trimToEmpty(target);
			if(StringUtils.isEmpty(tagName)) {
				continue;
			}
			if(tagName.equals(searchTagName)) {
				return true;
			}
		}
		return false;
	}
}
