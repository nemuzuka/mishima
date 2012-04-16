package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.meta.KindModelMeta;
import jp.co.nemuzuka.model.KindModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * KindModelに対するDao.
 * @author kazumune
 */
public class KindDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return KindModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return KindModel.class;
	}

	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param projectKeyToString 文字列化プロジェクトKey
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public KindModel get4ProjectKey(String projectKeyToString) {
		
		Key key = Datastore.createKey(getModelClass(), projectKeyToString);
		return get(key);
	}
}
