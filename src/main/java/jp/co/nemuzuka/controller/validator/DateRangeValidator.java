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
package jp.co.nemuzuka.controller.validator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.slim3.controller.validator.AbstractValidator;
import org.slim3.util.ApplicationMessage;

/**
 * 2つの日付の相関チェックのvalidator.
 * nameには、カンマ区切りで「From日付,To日付」の形で設定されていることを想定します。
 * さらにデータ型はyyyyMMddでくる想定です。
 * もし、変換できなかった場合、データにnullが渡されたものとして処理を行います。
 * @author k-katagiri
 */
public class DateRangeValidator extends AbstractValidator {

	/**
	 * コンストラクタ.
	 * @param maxlength 1要素の文字数
	 */
	public DateRangeValidator() {
		super();
	}
	
	/**
     * コンストラクタ.
     * @param message メッセージキー
     */
    public DateRangeValidator(String message) {
        super(message);
    }
	
	/* (non-Javadoc)
	 * @see org.slim3.controller.validator.Validator#validate(java.util.Map, java.lang.String)
	 */
	@Override
	public String validate(Map<String, Object> parameters, String name) {
		String[] names = name.split(",");
		if(names.length != 2) {
			//カンマ区切りで渡ってきていない場合、処理終了
			return null;
		}
		
		String fromDateStr = (String) parameters.get(names[0]);
		String toDateStr = (String) parameters.get(names[1]);
		Long fromTime = getTime(fromDateStr);
		Long toTime = getTime(toDateStr);
		
		if(fromTime == null || toTime == null) {
			//どちらかがnullの場合、処理終了
			return null;
		}
		
		if(fromTime.longValue() > toTime.longValue()) {
			//開始日>終了日の関係の場合、エラーメッセージを返却
	        if (message != null) {
	            return message;
	        }
	        return ApplicationMessage.get(
	            getMessageKey(),
	            getLabel(names[1]),
	            getLabel(names[0]),
	            getLabel("date"));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.slim3.controller.validator.AbstractValidator#getMessageKey()
	 */
	@Override
	protected String getMessageKey() {
		return "validator.check.between";
	}

	/**
	 * 日付ミリ秒取得.
	 * 引数の日付文字列が未設定、または不正の場合、nullを返却します。
	 * @param dateStr 日付文字列
	 * @return 日付ミリ秒
	 */
	private Long getTime(String dateStr) {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		Date date = null;
		try {
			date = ConvertUtils.toDate(dateStr, sdf);
			if(date != null) {
				return date.getTime(); 
			}
		} catch(RuntimeException e) {}
		return null;
	}
	
}
