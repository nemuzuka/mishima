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

import java.util.Map;

import jp.co.nemuzuka.utils.ConvertUtils;

import org.slim3.controller.validator.AbstractValidator;
import org.slim3.util.ApplicationMessage;

/**
 * 改行コードで複数項目を入力するTextAreaのvalidator.
 * @author k-katagiri
 */
public class MultiDataValidator extends AbstractValidator {

    /**
     * The maximum length.
     */
    protected int maxlength;

	/**
	 * コンストラクタ.
	 * @param maxlength 1要素の文字数
	 */
	public MultiDataValidator(int maxlength) {
        this(maxlength, null);
	}
	
	/**
     * コンストラクタ.
     * 
     * @param message the error messsage
     */
    public MultiDataValidator(int maxlength, String message) {
        super(message);
        this.maxlength = maxlength;
    }
	
	/* (non-Javadoc)
	 * @see org.slim3.controller.validator.Validator#validate(java.util.Map, java.lang.String)
	 */
	@Override
	public String validate(Map<String, Object> parameters, String name) {
        Object value = parameters.get(name);
        if (value == null || "".equals(value)) {
            return null;
        }
        try {
            String s = (String) value;
            String[] array = ConvertUtils.toStringArray(s);
            boolean isError = false;
            for(String target : array) {
            	if(target.length() > maxlength) {
            		isError = true;
            		break;
            	}
            }
            if(isError == false) {
            	return null;
            }
        } catch (Throwable ignore) {}
        if (message != null) {
            return message;
        }
        return ApplicationMessage.get(
            getMessageKey(),
            getLabel(name),
            maxlength);
	}

	/* (non-Javadoc)
	 * @see org.slim3.controller.validator.AbstractValidator#getMessageKey()
	 */
	@Override
	protected String getMessageKey() {
		return "validator.multidata";
	}

}
