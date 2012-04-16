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
		
		String name = targetStr;
		name = name.replaceAll("\r\n", "\n");
		name = name.replaceAll("\r", "\n");
		String[] names = name.split("\n");
		for(String value : names) {
			if(StringUtils.isEmpty(value)) {
				continue;
			}
			list.add(new LabelValueBean(value, value));
		}
		return list;
	}
}
