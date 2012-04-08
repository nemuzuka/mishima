package jp.co.nemuzuka.core.controller;

import java.lang.reflect.Method;
import java.util.ConcurrentModificationException;
import java.util.Date;

import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.entity.UserInfo;
import jp.co.nemuzuka.exception.AlreadyExistKeyException;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.impl.ProjectServiceImpl;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;

import org.apache.commons.lang.time.DateUtils;
import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/**
 * Htmlを返却するControllerの基底クラス.
 * 基本的に、これを継承したControllerからデータストアのアクセスは考えていません。
 * @author kazumune
 */
public abstract class HtmlController extends AbsController {
	
	/**
	 * メイン処理.
	 * @return 遷移先Navigation
	 * @throws Exception 例外
	 */
	abstract protected Navigation execute() throws Exception;

	/**
	 * メイン処理.
	 * 正常終了時、commitしてThreadLocalから削除します。
	 * ConcurrentModificationExceptionやAlreadyExistKeyExceptionは
	 * 本クラスを継承したクラスでは発生しない設計思想なので、
	 * エラー画面に遷移させます。
	 * ※更新は、Ajax側で行う
	 * @see org.slim3.controller.Controller#run()
	 */
	@Override
	protected Navigation run() throws Exception {
		
		//グローバルトランザクションの設定を行う
		setTransaction();
		
		Navigation navigation = null;
		try {
			setUserService();
			
			//UserInfoの確認
			checkAndSetUserInfo();
			
			navigation = execute();
			//commit
			executeCommit();
		} catch (ConcurrentModificationException e) {
			//今回の思想では、こちらのケースで排他エラーになるような処理は無いので、
			//強制的にエラー画面を表示させることとする
			//ここに来たら設計バグ
			super.tearDown();
			navigation = forward(ERR_URL_SYSERROR);
		} catch(AlreadyExistKeyException e) {
			//一意制約エラーの場合
			//ここに来たら設計バグ
			super.tearDown();
			navigation = forward(ERR_URL_SYSERROR);
		}
		return navigation;
	}
	
	/**
	 * 前処理.
	 * ・ActionFormの設定
	 * ・validation
	 * を行います。
	 * @see org.slim3.controller.Controller#setUp()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected Navigation setUp() {
		super.setUp();
		Class clazz = getClass();

		//ログインユーザの情報を元に、データストアに設定されているかチェック
		Navigation navigation = checkSettingUser();
		if(navigation != null) {
			return navigation;
		}
		
		//ActionFormの設定
		setActionForm(clazz);

		//validationの実行
		return executeValidation(clazz);
	}
	
	/**
	 * ログインユーザ設定チェック.
	 * ログインユーザが登録済みである or Google App Engine管理者であるかチェックを行います。
	 * @return 登録済みである or GAE管理者である場合、null/それ以外、強制遷移先Navigation
	 */
	private Navigation checkSettingUser() {
		
		UserService service = UserServiceFactory.getUserService();
		if(service.isUserAdmin()) {
			//管理者がログインした場合、処理終了
			return null;
		}
		//登録済みユーザであることを確認する
		Key key = Datastore.createKey(MemberModel.class, service.getCurrentUser().getEmail());
		try {
			Datastore.get(MemberModel.class, key);
		} catch(EntityNotFoundRuntimeException e) {
			//存在しないので遷移先のNavigation
			return forward(ERR_URL_NO_REGIST);
		}
		return null;
	}

	/**
	 * validation実行.
	 * メイン処理に「@Validation」が付与されれている場合、メソッドを呼び出し、validateを実行します。
	 * @param clazz 対象クラス
	 * @return エラーが無い or validatationが無い場合はnull/エラーが存在する場合、遷移先Navigation
	 */
	@SuppressWarnings({ "rawtypes" })
	private Navigation executeValidation(Class clazz) {
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
				return (Navigation) invoke(getClass(), validation.input());
			}
		}
		return null;
	}
	
	/**
	 * UserInfo確認.
	 * Sessionが存在しない or UserInfoの更新開始時刻を現在の時刻が超えた(もしくはnull)
	 * の場合、参照可能プロジェクトを更新と共に更新開始時期を
	 * 現在時刻 + システムプロパティ(jp.co.nemuzuka.session.refresh.min)分加算して設定する
	 */
	private void checkAndSetUserInfo() {
		UserInfo userInfo = sessionScope(USER_INFO_KEY);
		if(userInfo == null || isOverRefreshStartTime(userInfo.refreshStartTime)) {
			//更新する
			if(userInfo == null) {
				userInfo = new UserInfo();
			}
			refreshUserInfo(userInfo);
			sessionScope(USER_INFO_KEY, userInfo);
		}
	}

	/**
	 * 更新開始時刻超えチェック.
	 * 現在時刻が更新開始時刻を超えているがチェックします。
	 * @param refreshStartTime 更新開始時刻
	 * @return 更新開始時刻超えの場合、true
	 */
	private boolean isOverRefreshStartTime(Date refreshStartTime) {
		
		if(refreshStartTime == null) {
			return true;
		}
		
		long cuurentTime = CurrentDateUtils.getInstance().getCurrentDateTime().getTime();
		long targetTime = refreshStartTime.getTime();
		if(cuurentTime > targetTime) {
			return true;
		}
		return false;
	}

	/**
	 * UserInfo更新.
	 * 参照可能プロジェクトと更新開始時刻を更新する
	 * @param userInfo 設定UserInfo
	 */
	private void refreshUserInfo(UserInfo userInfo) {
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
