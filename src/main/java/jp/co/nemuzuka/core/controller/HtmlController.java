package jp.co.nemuzuka.core.controller;

import java.lang.reflect.Method;
import java.util.ConcurrentModificationException;

import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.model.MemberModel;

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
	 * @see org.slim3.controller.Controller#run()
	 */
	@Override
	protected Navigation run() throws Exception {
		//グローバルトランザクションの設定を行う
		setTransaction();
		
		Navigation navigation = null;
		try {
			navigation = execute();
			//commit
			executeCommit();
		} catch (ConcurrentModificationException e) {
			//今回の思想では、こちらのケースで排他エラーになるような処理は無いので、
			//強制的にエラー画面を表示させることとする
			//ここに来たら設計バグ
			super.tearDown();
			navigation = forward("/error/noregist/");
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
			return forward("/error/noregist/");
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
}
