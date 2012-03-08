package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.model.MemberModel;

import org.slim3.datastore.Datastore;

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
}
