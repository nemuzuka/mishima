package jp.co.nemuzuka.core.controller;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Logger;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.core.entity.TransactionEntity;

import net.sf.json.JSONSerializer;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slim3.controller.Controller;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.BeanUtil;

/**
 * JSONでレスポンスを返す場合のController基底クラス.
 * @author kazumune
 */
public abstract class JsonController extends Controller {

	/** token格納キー. */
	//Sessionも、リクエストパラメータもこの項目であることが前提です。
	private String TOKEN_KEY = "jp.co.nemuzuka.token";
	/** tokenエラー存在有無格納キー. */
	private String TOKEN_ERR_KEY = "jp.co.nemuzuka.token.err";
	
	/** logger. */
	protected final Logger logger = Logger.getLogger(getClass().getName());
	
	/**
	 * メイン処理.
	 * 戻り値がJSONオブジェクトに変換されてレスポンスになります。
	 * @return JSONオブジェクト
	 * @throws Exception 例外
	 */
	abstract protected Object execute() throws Exception;
	
	/**
	 * メイン処理.
	 * @see org.slim3.controller.Controller#run()
	 */
	@Override
	protected Navigation run() throws Exception {
		Object obj = execute();
		if (obj == null) {
			throw new AssertionError("execute() must not be null.");
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

		//ActionFormの設定
		setActionForm(clazz);

		//TokenCheckが指定されていれば実行
		Navigation navigation;
		try {
			navigation = executeTokenCheck(clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if(navigation == null) {
			//validationが指定されていれば実行
			navigation = executeValidation(clazz);
			if(navigation != null) {
				//正常終了していない場合、処理終了
				return navigation;
			}
			//グローバルトランザクションの設定を行う
			setTransaction();
		}
		return null;
	}

	/**
	 * エラー時処理.
	 * グローバルトランザクションをrollback.
	 * Commit不要状態にします。
	 * @see org.slim3.controller.Controller#handleError(java.lang.Throwable)
	 */
	@Override
	protected Navigation handleError(Throwable error) throws Throwable {
		
		TransactionEntity entity = GlobalTransaction.transaction.get();
		entity.rollback();
		return super.handleError(error);
	}
	
	/**
	 * 終了時処理.
	 * ・Commitを発行します（ロールバック済の場合、何もしません）。
	 * ・ThreadLocalの中身を空に設定します。
	 * @see org.slim3.controller.Controller#tearDown()
	 */
	@Override
	protected void tearDown() {
		TransactionEntity entity = GlobalTransaction.transaction.get();
		if(entity.isRollback() == false) {
			entity.commit();
		}
		GlobalTransaction.transaction.remove();
	};
	
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
		JSONSerializer.toJSON(obj).write(response.getWriter());
		response.flushBuffer();
		return null;
	}
	
	/**
	 * JSON形式でエラーを返却します。
	 * リクエストパラメータに設定されているエラーメッセージをJSONオブジェクトに設定します。
	 * @return null
	 * @throws Exception 例外
	 */
	protected Navigation jsonError() throws Exception {
		JsonResult result = new JsonResult();
		for(Map.Entry<String, String> target : errors.entrySet()) {
			result.getErrorMsg().add(target.getValue());
		}
		String jsonError = requestScope(TOKEN_ERR_KEY);
		if(StringUtils.isEmpty(jsonError)) {
			//通常のエラー
			result.setResult(JsonResult.STATUS_NG);
		} else {
			//Tokenエラー
			result.setResult(JsonResult.TOKEN_ERROR);
		}
		return writeJsonObj(result);
	}
	
	
	/**
	 * メソッド呼び出し.
	 * 引数なしでメソッドを呼び出します。
	 * @param clazz クラス
	 * @param methodName メソッド名
	 * @return 呼び出したメソッドの戻り値
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object invoke(Class clazz, String methodName) {

		//validateメソッドの呼び出し
		Method method = null;
		try {
			method = clazz.getDeclaredMethod(methodName, (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		//validateメソッド呼び出し
		Object obj = null;
		try {
			obj = method.invoke(this, (Object[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return obj;
	}

	/**
	 * ActionForm設定.
	 * 「@ActionForm」と定義されているものに対して、
	 * インスタンス生成し、値をリクエストパラメータよりコピーします。
	 * @param clazz 対象クラス
	 */
	@SuppressWarnings("rawtypes")
	private void setActionForm(Class clazz) {

		//@ActionFormと定義されているものに対して、インスタンス生成し、コピーする
		Field[] fields = clazz.getDeclaredFields();
		for(Field target : fields) {
			Annotation[] annos = target.getAnnotations();
			for(Annotation targetAnno :annos) {
				if(targetAnno instanceof ActionForm) {

					//インスタンス生成
					Object obj = null;
					try {
						target.setAccessible(true);
						obj = target.getType().newInstance();
						BeanUtil.copy(request, obj);
						target.set(this, obj);
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
	}


	/**
	 * validation実行.
	 * メイン処理に「@Validation」が付与されれている場合、メソッドを呼び出し、validateを実行します。
	 * @param clazz 対象クラス
	 * @return Navigation(エラーが無い or validatationが無い場合はnull)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Navigation executeValidation(Class clazz) {
		//executeメソッドにValidatetionアノテーションが付与されている場合
		Method target = null;
		try {
			target = clazz.getDeclaredMethod("execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if(target != null) {
			Validation validation = target.getAnnotation(Validation.class);
			if(validation != null) {

				Validators validators = (Validators)invoke(clazz, validation.method());

				//validate実行
				boolean bret = validators.validate();

				//エラーが存在する場合
				if(bret == false) {
					//inputのメソッドで呼び出された定義を呼び出すようにする
					Navigation navigation = (Navigation)invoke(getClass(), validation.input());
					return navigation;
				}
			}
		}
		return null;
	}

	/**
	 * Token設定.
	 * SessionにTokenを設定します。
	 * @return 設定Token文字列
	 */
	protected String setToken() {
		String token = RandomStringUtils.randomAlphanumeric(32);
		sessionScope(TOKEN_KEY, token);
		return token;
	}
	
	/**
	 * tokenチェック実行.
	 * メイン処理に「@TokenCheck」が付与されれている場合、tokenチェックを行います。
	 * 合致しない場合、戻り値を
	 * @param clazz 対象クラス
	 * @return エラーの場合、遷移先。エラーが無い or 付与されていない場合、null
	 * @throws Exception 例外
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Navigation executeTokenCheck(Class clazz) throws Exception {
		//executeメソッドにTokenCheckアノテーションが付与されている場合
		Method target = null;
		try {
			target = clazz.getDeclaredMethod("execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		if(target != null) {
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
					return jsonError();
				}
			}
		}
		return null;
	}


	/**
	 * グローバルトランザクション設定.
	 * ThreadLocalに開始状態のトランザクションを設定します。
	 */
	private void setTransaction() {
		TransactionEntity transactionEntity = new TransactionEntity();
		GlobalTransaction.transaction.set(transactionEntity);
	}
}
