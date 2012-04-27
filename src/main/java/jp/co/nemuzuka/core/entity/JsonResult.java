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
package jp.co.nemuzuka.core.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class JsonResult implements Serializable {

	/** 正常終了. */
	public static Integer STATUS_OK = 0;
	/** 異常終了. */
	public static Integer STATUS_NG = -1;
	/** Tokenエラー. */
	public static Integer TOKEN_ERROR = -2;
	/** サーバーエラー. */
	public static Integer SEVERE_ERROR = -3;
	/** 更新時バージョンエラー. */
	public static Integer VERSION_ERR = -4;
	/** 一意制約エラー. */
	public static Integer DUPLICATE_ERR = -5;
	/** 該当データ無し. */
	public static Integer NO_DATA = -6;
	/** Sessionタイムアウト. */
	public static Integer SESSION_TIMEOUT = -99;
	
	
	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/** エラーメッセージ文字列List. */
	private List<String> errorMsg = new ArrayList<String>();
	/** メッセージ文字列List. */
	private List<String> infoMsg = new ArrayList<String>();
	
	/** ステータスコード. */
	private Integer status = STATUS_OK;
	/** 結果Object. */
	private Object result;
	/** token文字列. */
	private String token;
	
	/**
	 * @return token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token セットする token
	 */
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * @return errorMsg
	 */
	public List<String> getErrorMsg() {
		return errorMsg;
	}
	/**
	 * @param errorMsg セットする errorMsg
	 */
	public void setErrorMsg(List<String> errorMsg) {
		this.errorMsg = errorMsg;
	}
	/**
	 * @return infoMsg
	 */
	public List<String> getInfoMsg() {
		return infoMsg;
	}
	/**
	 * @param infoMsg セットする infoMsg
	 */
	public void setInfoMsg(List<String> infoMsg) {
		this.infoMsg = infoMsg;
	}
	/**
	 * @return status
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * @param status セットする status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * @return result
	 */
	public Object getResult() {
		return result;
	}
	/**
	 * @param result セットする result
	 */
	public void setResult(Object result) {
		this.result = result;
	}
}
