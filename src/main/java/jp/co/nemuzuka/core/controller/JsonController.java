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
package jp.co.nemuzuka.core.controller;


import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ConcurrentModificationException;
import java.util.Map;
import java.util.logging.Level;

import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.exception.AlreadyExistKeyException;
import net.arnx.jsonic.JSON;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * JSONでレスポンスを返す場合のController基底クラス.
 * @author kazumune
 */
public abstract class JsonController extends AbsController {

	/** tokenエラー存在有無格納キー. */
	private static final String TOKEN_ERR_KEY = "jp.co.nemuzuka.token.err";
	/** サーバエラー存在有無格納キー. */
	private static final String SEVERE_ERR_KEY = "jp.co.nemuzuka.severe.err";
	/** 前処理エラー存在有無格納キー. */
	private static final String SETUP_ERROR = "jp.co.nemuzuka.setup.error";
	/** 前処理エラー存在有無格納キー. */
	private static final String SESSION_TIMEOUT_ERROR = "jp.co.nemuzuka.session.timeout.error";
	
	/**
	 * メイン処理.
	 * 戻り値がJSONオブジェクトに変換されてレスポンスになります。
	 * @return JSONオブジェクト
	 * @throws Exception 例外
	 */
	abstract protected Object execute() throws Exception;
	
	/**
	 * メイン処理.
	 * 正常終了時、commitしてThreadLocalから削除します。
	 * @see org.slim3.controller.Controller#run()
	 */
	@Override
	protected Navigation run() throws Exception {
		
		//前処理でエラーが発生した場合、処理は行わない
		String setUpError = requestScope(SETUP_ERROR);
		if(StringUtils.isNotEmpty(setUpError)) {
			return null;
		}
		
		Object obj = null;
		try {
			obj = execute();
			if (obj == null) {
				throw new AssertionError("execute() must not be null.");
			}
			executeCommit();
		} catch (ConcurrentModificationException e) {
			//排他エラーが発生した場合、その情報をJsonオブジェクトに設定して返却
			super.tearDown();
			JsonResult result = new JsonResult();
			result.setStatus(JsonResult.VERSION_ERR);
			result.getErrorMsg().add(ApplicationMessage.get("errors.version"));
			obj = result;
		} catch(AlreadyExistKeyException e) {
			//一意制約エラーが発生した場合、その情報をJsonオブジェクトに設定して返却
			super.tearDown();
			JsonResult result = new JsonResult();
			result.setStatus(JsonResult.DUPLICATE_ERR);
			result.getErrorMsg().add(ApplicationMessage.get("errors.duplicate"));
			obj = result;
		} catch(Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
		}
		return writeJsonObj(obj);
	}

	/**
	 * 前処理.
	 * ・ActionFormの設定
	 * ・TokenCheck
	 * ・validation
	 * ・グローバルトランザクションをThreadLocalに設定
	 * を行います。
	 * @see org.slim3.controller.Controller#setUp()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Navigation setUp() {
		super.setUp();
		Class clazz = getClass();

		setUserService();

		//ActionFormの設定
		setActionForm(clazz);

		//Sesisonが存在しない時にエラーレスポンスを返す
		boolean sessionCheck = executeSessionCheck(clazz);
		if(sessionCheck == false) {
			errors.put("message", ApplicationMessage.get("errors.session.timeout"));
			requestScope(SESSION_TIMEOUT_ERROR, "1");
			jsonError();
			return null;
		}
	
		//TokenCheck、ProjectAdmin、ProjectMemberが指定されていれば実行
		boolean status = executeTokenCheck(clazz);
		boolean projectAdmin = executeProjectAdminCheck(clazz);
		boolean projectMember = executeProjectMemberCheck(clazz);
		boolean systemManager = executeSystemManagerCheck(clazz);

		if(status && projectAdmin && projectMember && systemManager) {
			//validationが指定されていれば実行
			status = executeValidation(clazz);
			if(status == false) {
				return null;
			}
			//グローバルトランザクションの設定を行う
			setTransaction();
		} else {
			requestScope(SETUP_ERROR, "1");
		}
		return null;
	}

	/**
	 * エラー時処理.
	 * @see org.slim3.controller.Controller#handleError(java.lang.Throwable)
	 */
	@Override
	protected Navigation handleError(Throwable error) throws Throwable {
		logger.log(Level.SEVERE, error.getMessage(), error);
		errors.put("message", ApplicationMessage.get("errors.severe"));
		requestScope(SEVERE_ERR_KEY, "1");
		return jsonError();
	}
	
	/**
	 * JSONオブジェクト書き込み.
	 * レスポンスにJSONオブジェクトを書き込みます。
	 * @param obj JSONオブジェクト
	 * @return null
	 * @throws IOException IO例外
	 */
	protected Navigation writeJsonObj(Object obj) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		response.getWriter().write(JSON.encode(obj));
		response.flushBuffer();
		return null;
	}
	
	/**
	 * JSON形式でエラーを返却します。
	 * リクエストパラメータに設定されているエラーメッセージをJSONオブジェクトに設定します。
	 * @return null
	 */
	public Navigation jsonError() {
		JsonResult result = new JsonResult();
		for(Map.Entry<String, String> target : errors.entrySet()) {
			result.getErrorMsg().add(target.getValue());
		}
		String jsonError = requestScope(TOKEN_ERR_KEY);
		String severeError = requestScope(SEVERE_ERR_KEY);
		String sessionTimeOut = requestScope(SESSION_TIMEOUT_ERROR);
		
		if(StringUtils.isNotEmpty(jsonError)) {
			//Tokenエラー
			result.setStatus(JsonResult.TOKEN_ERROR);
		} else if(StringUtils.isNotEmpty(severeError)) {
			//サーバーエラー
			result.setStatus(JsonResult.SEVERE_ERROR);
		} else if(StringUtils.isNotEmpty(sessionTimeOut)) {
			//Sessionタイムアウトエラー
			result.setStatus(JsonResult.SESSION_TIMEOUT);
		} else {
			//通常のエラー
			result.setStatus(JsonResult.STATUS_NG);
		}
		try {
			//エラーが存在する旨、設定
			requestScope(SETUP_ERROR, "1");
			return writeJsonObj(result);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * エラーJsonResult作成.
	 * 引数の情報を元にエラーメッセージを設定したJsonResultを作成します。
	 * @param key エラーメッセージKey
	 * @param obj エラーメッセージパラメータ
	 * @return エラーJsonResultインスタンス
	 */
	protected JsonResult createErrorMsg(String key, Object...obj) {
		JsonResult result = new JsonResult();
		result.setStatus(JsonResult.STATUS_NG);
		result.getErrorMsg().add(ApplicationMessage.get(key, obj));
		return result;
	}

	/**
	 * validation実行.
	 * メイン処理に「@Validation」が付与されれている場合、メソッドを呼び出し、validateを実行します。
	 * @param clazz 対象クラス
	 * @return エラーが無い or validatationが無い場合はtrue/エラーが存在する場合、false
	 */
	@SuppressWarnings({ "rawtypes" })
	private boolean executeValidation(Class clazz) {
		//executeメソッドにValidatetionアノテーションが付与されている場合
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Validation validation = target.getAnnotation(Validation.class);
		if(validation != null) {

			Validators validators = (Validators)invoke(clazz, validation.method());

			//validate実行
			boolean bret = validators.validate();

			//エラーが存在する場合
			if(bret == false) {
				//inputのメソッドで呼び出された定義を呼び出すようにする
				invoke(getClass(), validation.input());
				return false;
			}
		}
		return true;
	}
	
	/**
	 * tokenチェック実行.
	 * メイン処理に「@TokenCheck」が付与されれている場合、tokenチェックを行います。
	 * 合致しない場合、戻り値をfalseに設定します。
	 * @param clazz 対象クラス
	 * @return エラーが無い or 付与されていない場合、true/エラーが存在する場合、false
	 */
	@SuppressWarnings({ "rawtypes" })
	private boolean executeTokenCheck(Class clazz) {
		//executeメソッドにTokenCheckアノテーションが付与されている場合
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		TokenCheck tokenCheck = target.getAnnotation(TokenCheck.class);
		if(tokenCheck != null) {

			//SessionのTokenとリクエストパラメータのTokenが合致しているかチェック
			String reqToken = asString(TOKEN_KEY);
			String sessionToken = sessionScope(TOKEN_KEY);
			removeSessionScope(TOKEN_KEY);
			if(ObjectUtils.equals(reqToken, sessionToken) == false) {
				//requestスコープにエラーメッセージを設定し、JSONエラー時のメソッドを呼び出す
				errors.put("message", ApplicationMessage.get("errors.token"));
				requestScope(TOKEN_ERR_KEY, "1");
				jsonError();
				return false;
			}
		}
		return true;
	}
}
