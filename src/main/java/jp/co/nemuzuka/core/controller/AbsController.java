package jp.co.nemuzuka.core.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;

import org.apache.commons.lang.RandomStringUtils;
import org.slim3.controller.Controller;
import org.slim3.util.BeanUtil;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Contorollerの基底クラス.
 * @author kazumune
 */
public abstract class AbsController extends Controller {

	/** token格納キー. */
	//Sessionも、リクエストパラメータもこの項目であることが前提です。
	protected String TOKEN_KEY = "jp.co.nemuzuka.token";

	/** ログインユーザ情報. */
	protected UserService userService;
	
	/**
	 * 終了時処理.
	 * ThreadLocalに存在する場合、ロールバックして空にします。
	 * @see org.slim3.controller.Controller#tearDown()
	 */
	@Override
	protected void tearDown() {
		TransactionEntity entity = GlobalTransaction.transaction.get();
		if(entity != null) {
			entity.rollback();
			GlobalTransaction.transaction.remove();
		}
	};

	/**
	 * ログインユーザ情報設定.
	 */
	protected void setUserService() {
		userService = UserServiceFactory.getUserService();
	}
	
	/**
	 * Method取得.
	 * メソッドを取得します。
	 * 存在しない場合、親クラスに対して検索します。
	 * @param clazz 対象Class
	 * @param methodName メソッド名
	 * @param paramClass パラメータクラス配列
	 * @return メソッド
	 * @throws NoSuchMethodException 親クラスまでさかのぼっても見つからなかった
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Method getDeclaredMethod(Class clazz, String methodName, 
			Class[] paramClass) throws NoSuchMethodException {
		Method target = null;
		try {
			target = clazz.getDeclaredMethod(methodName, paramClass);
		} catch(NoSuchMethodException e) {
			Class superClazz = clazz.getSuperclass();
			if(superClazz == null) {
				throw e;
			}
			return getDeclaredMethod(superClazz, methodName, paramClass);
		}
		return target;
	}
	/**
	 * メソッド呼び出し.
	 * 引数なしでメソッドを呼び出します。
	 * @param clazz クラス
	 * @param methodName メソッド名
	 * @return 呼び出したメソッドの戻り値
	 */
	@SuppressWarnings({ "rawtypes" })
	protected Object invoke(Class clazz, String methodName) {

		//validateメソッドの呼び出し
		Method method = null;
		try {
			method = getDeclaredMethod(clazz, methodName, (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		//validateメソッド呼び出し
		Object obj = null;
		try {
			method.setAccessible(true);
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
	protected void setActionForm(Class clazz) {

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
	 * グローバルトランザクション設定.
	 * ThreadLocalに開始状態のトランザクションを設定します。
	 */
	protected void setTransaction() {
		TransactionEntity transactionEntity = new TransactionEntity();
		GlobalTransaction.transaction.set(transactionEntity);
	}

	/**
	 * Commit実行.
	 * Commitを発行し、ThreadLocalから削除します。
	 */
	protected void executeCommit() {
		TransactionEntity entity = GlobalTransaction.transaction.get();
		entity.commit();
		GlobalTransaction.transaction.remove();
	}
}
