package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.meta.PriorityModelMeta;
import jp.co.nemuzuka.model.PriorityModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * PriorityModelに対するDao.
 * @author kazumune
 */
public class PriorityDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return PriorityModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return PriorityModel.class;
	}

	private static PriorityDao priorityDao = new PriorityDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static PriorityDao getInstance() {
		return priorityDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private PriorityDao() {}

	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param projectKeyToString 文字列化プロジェクトKey
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public PriorityModel get4ProjectKey(String projectKeyToString) {
		
		Key key = Datastore.createKey(getModelClass(), projectKeyToString);
		return get(key);
	}
}
