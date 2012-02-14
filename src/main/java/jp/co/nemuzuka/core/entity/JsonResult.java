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
