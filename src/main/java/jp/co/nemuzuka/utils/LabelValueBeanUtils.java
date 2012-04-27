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

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.nemuzuka.core.entity.LabelValueBean;

/**
 * LabelValueBeanのListを作成するUtils.
 * @author k-katagiri
 */
public class LabelValueBeanUtils {

	/**
	 * LabelValueBean配列生成.
	 * 引数の文字列を改行コードで分割して、分割データを1レコードとしてListを作成します。
	 * @param targetStr 対象文字列
	 * @param isEmptyData 先頭に空のデータを追加する場合、true
	 * @return 生成List
	 */
	public static List<LabelValueBean> createList(String targetStr, boolean isEmptyData) {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		
		if(isEmptyData) {
			list.add(new LabelValueBean("", ""));
		}
		
		if(StringUtils.isEmpty(targetStr)) {
			return list;
		}
		
		String[] array = ConvertUtils.toStringArray(targetStr);
		for(String value : array) {
			list.add(new LabelValueBean(value, value));
		}
		return list;
	}
}
