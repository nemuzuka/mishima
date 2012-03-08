package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.model.MemberModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;

import com.google.appengine.api.datastore.Key;

/**
 * MemberModelに対するDao.
 * @author kazumune
 */
public class MemberDao {

	/**
	 * コンストラクタ.
	 */
	private MemberDao(){}
	
	/**
	 * put処理.
	 * @param entity 対象Entity
	 */
	public static void put(MemberModel entity) {
		Key key = Datastore.put(
				GlobalTransaction.transaction.get().getTransaction(), entity);
		entity.setKey(key);
	}
	
	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param email メールアドレス
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public static MemberModel get(String email) {
		Key key = Datastore.createKey(MemberModel.class, email);
		MemberModel model = null;
		try {
			model = Datastore.get(
					GlobalTransaction.transaction.get().getTransaction(),
					MemberModel.class, key);
		} catch(EntityNotFoundRuntimeException e) {}
		return model;
	}
	
}
