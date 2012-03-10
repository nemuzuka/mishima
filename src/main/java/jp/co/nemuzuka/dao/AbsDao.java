package jp.co.nemuzuka.dao;

import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.model.AbsModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * Daoの基底クラス.
 * @author kazumune
 */
public abstract class AbsDao {

	/**
	 * ModelMeta情報取得.
	 * @return ModelMeta情報
	 */
	@SuppressWarnings("rawtypes")
	abstract ModelMeta getModelMeta();
	
	/**
	 * ModelClass情報取得.
	 * @return ModelClass情報
	 */
	@SuppressWarnings("rawtypes")
	abstract Class getModelClass();
	
	/**
	 * put処理.
	 * @param model 対象Model
	 */
	public void put(AbsModel model) {
		Key key = Datastore.put(
				GlobalTransaction.transaction.get().getTransaction(), model);
		model.setKey(key);
	}
	
	/**
	 * get処理.
	 * @param modelClass 対象クラス.
	 * @param key Key情報
	 * @return 該当データ。存在しなければnull
	 */
	@SuppressWarnings("unchecked")
	public <M> M get(Key key) {
		try {
			return (M) Datastore.get(
					GlobalTransaction.transaction.get().getTransaction(),
					getModelClass(), key);
		} catch(EntityNotFoundRuntimeException e) {
			return null;
		}
	}
	
	/**
	 * 全件取得.
	 * 登録されているデータを全件取得します。
	 * @param <M>
	 * @return 登録データList
	 */
	@SuppressWarnings("unchecked")
	public <M> List<M> getAllList() {
		return (List<M>) Datastore.query(getModelMeta()).asList();
	}
	
}
