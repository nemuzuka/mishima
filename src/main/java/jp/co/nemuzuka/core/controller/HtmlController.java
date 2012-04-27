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

import java.lang.reflect.Method;
import java.util.ConcurrentModificationException;
import java.util.logging.Level;

import jp.co.nemuzuka.core.annotation.NoRegistCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.entity.UserInfo;
import jp.co.nemuzuka.exception.AlreadyExistKeyException;
import jp.co.nemuzuka.utils.DateTimeChecker;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;

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
			//UserInfoの確認
			checkAndSetUserInfo(getClass());
			
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
		} catch(Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw e;
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

		setUserService();
		
		boolean sessionCheck = executeSessionCheck(clazz);
		if(sessionCheck == false) {
			//SessionTimeoutの場合、エラー画面に遷移
			return forward(ERR_SESSION_TIMEOUT);
		}

		//ログインユーザの情報を元に、データストアに設定されているかチェック
		Navigation navigation = checkSettingUser(clazz);
		if(navigation != null) {
			return navigation;
		}
		
		//ProjectAdmin、ProjectMember、SystemManagerの設定確認
		boolean projectAdmin = executeProjectAdminCheck(clazz);
		boolean projectMember = executeProjectMemberCheck(clazz);
		boolean systemManager = executeSystemManagerCheck(clazz);
		if(projectAdmin == false || projectMember == false || systemManager == false) {
			//不正なエラーの場合、TOP画面に遷移させる
			getUserInfo().initProjectInfo();
			return forward("/");
		}
		
		//ActionFormの設定
		setActionForm(clazz);

		//validationの実行
		return executeValidation(clazz);
	}
	
	/**
	 * ログインユーザ設定チェック.
	 * ログインユーザが登録済みである or Google App Engine管理者であるかチェックを行います。
	 * 「@NoRegistCheck」が付与されている場合、強制的にnullを返します。
	 * @param clazz 対象クラス
	 * @return 登録済みである or GAE管理者である場合、null/それ以外、強制遷移先Navigation
	 */
	@SuppressWarnings({ "rawtypes" })
	private Navigation checkSettingUser(Class clazz) {
		
		//executeメソッドにValidatetionアノテーションが付与されている場合
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		NoRegistCheck validation = target.getAnnotation(NoRegistCheck.class);
		if(validation != null) {
			return null;
		}

		UserService service = UserServiceFactory.getUserService();
		if(service.isUserAdmin()) {
			//管理者がログインした場合、処理終了
			return null;
		}
		//登録済みユーザであることを確認する
		if(isExistsUser(service.getCurrentUser().getEmail()) == false) {
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
	 * 「@NoRegistCheck」が付与されている場合、チェックを行いません。
	 * @param clazz 対象クラス
	 */
	@SuppressWarnings({ "rawtypes" })
	private void checkAndSetUserInfo(Class clazz) {
		
		//executeメソッドにValidatetionアノテーションが付与されている場合、処理終了
		Method target = null;
		try {
			target = getDeclaredMethod(clazz, "execute", (Class[])null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		NoRegistCheck validation = target.getAnnotation(NoRegistCheck.class);
		if(validation != null) {
			return;
		}
		
		UserInfo userInfo = sessionScope(USER_INFO_KEY);
		if(userInfo == null || DateTimeChecker.isOverRefreshStartTime(userInfo.refreshStartTime)) {
			//更新する
			if(userInfo == null) {
				userInfo = new UserInfo();
			}
			refreshUserInfo(userInfo);
			sessionScope(USER_INFO_KEY, userInfo);
		}
	}
}
