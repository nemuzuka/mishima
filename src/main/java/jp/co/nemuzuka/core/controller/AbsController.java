package jp.co.nemuzuka.core.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.NoSessionCheck;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.core.entity.UserInfo;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.impl.ProjectServiceImpl;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slim3.controller.Controller;
import org.slim3.util.BeanUtil;

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Contorollerの基底クラス.
 * @author kazumune
 */
public abstract class AbsController extends Controller {

	/** UserInfo格納キー. */
	protected String USER_INFO_KEY = "userInfo";
	
	/** token格納キー. */
	//Sessionも、リクエストパラメータもこの項目であることが前提です。
	protected String TOKEN_KEY = "jp.co.nemuzuka.token";

	/** ログインユーザ情報. */
	protected UserService userService;
	
	//遷移先URL
	/** システムに登録されていないユーザからのアクセス. */
	protected String ERR_URL_NO_REGIST = "/error/noregist/";
	/** システムエラー. */
	protected String ERR_URL_SYSERROR = "/error/syserror/";
	/** Sessionタイムアウト. */
	protected String ERR_SESSION_TIMEOUT = "/error/timeout/";
	
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
	 * UserInfo取得.
	 * Sessionに格納されているUserInfoを取得します。
	 * @return UserInfoインスタンス
	 */
	protected UserInfo getUserInfo() {
		return sessionScope(USER_INFO_KEY);
	}

	/**
	 * ログインユーザ情報設定.
	 * 同時にlogout用のURLを設定します。
	 */
	protected void setUserService() {
		userService = UserServiceFactory.getUserService();
		requestScope("logoutURL", "/logout");
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
	
	/**
	 * ProjectAdminチェック実行.
	 * メイン処理に「@ProjectAdmin」が付与されれている場合、プロジェクト管理者であるかチェックを行います。
	 * 管理者でない場合、戻り値をfalseに設定します。
	 * @param clazz 対象クラス
	 * @return プロジェクト管理者である or 付与されていない場合、true/プロジェクト管理者でない場合、false
	 */
	@SuppressWarnings("rawtypes")
	protected boolean executeProjectAdminCheck(Class clazz) {
		//executeメソッドにProjectAdminアノテーションが付与されている場合
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ProjectAdmin projectAdmin = target.getAnnotation(ProjectAdmin.class);
		if(projectAdmin != null) {
			//UserInfoにおいて、管理者であるかの結果を返す
			return getUserInfo().projectManager;
		}
		return true;
	}

	/**
	 * ProjectMemberチェック実行.
	 * メイン処理に「@ProjectMember」が付与されれている場合、プロジェクト参加者であるかチェックを行います。
	 * プロジェクト参加者でない場合、戻り値をfalseに設定します。
	 * @param clazz 対象クラス
	 * @return プロジェクト参加者である or 付与されていない場合、true/プロジェクト参加者でない場合、false
	 */
	@SuppressWarnings("rawtypes")
	protected boolean executeProjectMemberCheck(Class clazz) {
		//executeメソッドにProjectMemberアノテーションが付与されている場合
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		ProjectMember projectMember = target.getAnnotation(ProjectMember.class);
		if(projectMember != null) {
			//UserInfoにおいて、プロジェクト参加者であるかの結果を返す
			return getUserInfo().projectMember;
		}
		return true;
	}

	/**
	 * SystemManagerチェック実行.
	 * メイン処理に「@SystemManager」が付与されれている場合、システム管理者であるかチェックを行います。
	 * システム管理者でない場合、戻り値をfalseに設定します。
	 * @param clazz 対象クラス
	 * @return システム管理者である or 付与されていない場合、true/システム管理者でない場合、false
	 */
	@SuppressWarnings("rawtypes")
	protected boolean executeSystemManagerCheck(Class clazz) {
		//executeメソッドにSystemManagerアノテーションが付与されている場合
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		SystemManager systemManager = target.getAnnotation(SystemManager.class);
		if(systemManager != null) {
			//UserInfoにおいて、システム管理者であるかの結果を返す
			return getUserInfo().systemManager;
		}
		return true;
	}

	/**
	 * Session存在チェック.
	 * Sessionが存在するか確認します。
	 * @param clazz 対象クラス
	 * @return Sessionが存在する or NoSessionCheckアノテーションが付与されている場合、true/Sessionが存在しない、false
	 */
	@SuppressWarnings("rawtypes")
	protected boolean executeSessionCheck(Class clazz) {
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		NoSessionCheck noSessionCheck = target.getAnnotation(NoSessionCheck.class);
		if(noSessionCheck != null) {
			//executeメソッドにNoSessionCheckアノテーションが付与されている場合、無条件でOKとする
			return true;
		}
		UserInfo userInfo = getUserInfo();
		if(userInfo == null) {
			return false;
		}
		return true;
	}


	/**
	 * UserInfo更新.
	 * 参照可能プロジェクトと更新開始時刻を更新する
	 * @param userInfo 設定UserInfo
	 */
	protected void refreshUserInfo(UserInfo userInfo) {
		ProjectService service = new ProjectServiceImpl();
		ProjectService.TargetProjectResult result = 
				service.getUserProjectList(userService.getCurrentUser().getEmail(), userService.isUserAdmin());
		
		userInfo.projectList = result.projectList;
		userInfo.systemManager = result.admin;

		//現在時刻に加算分の時刻(分)を加算し、設定する
		Date date = CurrentDateUtils.getInstance().getCurrentDateTime();
		int min = ConvertUtils.toInteger(System.getProperty("jp.co.nemuzuka.session.refresh.min", "15"));
		date = DateUtils.addMinutes(date, min);
		userInfo.refreshStartTime = date;
	}

}
