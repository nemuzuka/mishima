package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.meta.CategoryModelMeta;
import jp.co.nemuzuka.model.CategoryModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * CategoryModelに対するDao.
 * @author kazumune
 */
public class CategoryDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return CategoryModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return CategoryModel.class;
	}

	private static CategoryDao categoryDao = new CategoryDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static CategoryDao getInstance() {
		return categoryDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private CategoryDao() {}
	
	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param projectKeyToString 文字列化プロジェクトKey
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public CategoryModel get4ProjectKey(String projectKeyToString) {
		
		Key key = Datastore.createKey(getModelClass(), projectKeyToString);
		return get(key);
	}
}
