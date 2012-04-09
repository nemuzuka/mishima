package jp.co.nemuzuka.dao;

import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.model.AbsModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.EntityNotFoundRuntimeException;
import org.slim3.datastore.InMemorySortCriterion;
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
		Key key = GlobalTransaction.transaction.get().getTransaction().put(model);
		model.setKey(key);
	}
	
	/**
	 * delete処理.
	 * @param keys 削除対象Key
	 */
	public void delete(Key... keys) {
		GlobalTransaction.transaction.get().getTransaction().delete(keys);
	}
	
	/**
	 * get処理.
	 * @param key Key情報
	 * @return 該当データ。存在しなければnull
	 */
	@SuppressWarnings("unchecked")
	public <M> M get(Key key) {
		try {
			return (M) GlobalTransaction.transaction.get().getTransaction().get(
					getModelClass(), key);
		} catch(EntityNotFoundRuntimeException e) {
			return null;
		} catch(IllegalArgumentException e) {
			return null;
		}
	}

	/**
	 * get処理(version情報あり).
	 * @param key Key情報
	 * @param version version
	 * @return 該当データ。存在しない or versionが違う場合null
	 */
	@SuppressWarnings("unchecked")
	public <M> M get(Key key, Long version) {
		try {
			return (M) GlobalTransaction.transaction.get().getTransaction().get(
					getModelClass(), key, version);
		} catch(EntityNotFoundRuntimeException e) {
			return null;
		} catch(ConcurrentModificationException e) {
			return null;
		}
	}
	
	/**
	 * 全件取得.
	 * 登録されているデータを全件取得します。
	 * @param <M>
	 * @return 登録データList
	 */
	public <M> List<M> getAllList() {
		return getAllList((InMemorySortCriterion[])null);
	}

	/**
	 * 全件取得.
	 * 登録されているデータを全件取得します。
	 * @param <M>
	 * @param sort 検索条件
	 * @return 登録データList
	 */
	@SuppressWarnings("unchecked")
	public <M> List<M> getAllList(InMemorySortCriterion... sort) {
		
		if(sort == null) {
			return (List<M>) Datastore.query(getModelMeta()).asList();
		}
		return (List<M>) Datastore.query(getModelMeta()).sortInMemory(sort).asList();
	}
}
