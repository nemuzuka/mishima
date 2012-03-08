package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.model.ProjectModel;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * ProjectModelに対するDao.
 * @author kazumune
 */
public class ProjectDao {

	/**
	 * コンストラクタ.
	 */
	private ProjectDao(){}
	
	/**
	 * put処理.
	 * @param entity 対象Entity
	 */
	public static void put(ProjectModel entity) {
		Key key = Datastore.put(
				GlobalTransaction.transaction.get().getTransaction(), entity);
		entity.setKey(key);
	}
}
