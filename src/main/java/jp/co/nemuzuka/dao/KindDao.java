package jp.co.nemuzuka.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.meta.KindModelMeta;
import jp.co.nemuzuka.model.KindModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
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
	 * @param keyToString 文字列化Key
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public KindModel get(String keyToString) {
		Key key = Datastore.stringToKey(keyToString);
		KindModel model = get(key);
		return model;
	}
	
	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param key key
	 * @param version バージョン
	 * @param projectKeyToString プロジェクトKey文字列
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public KindModel get(Key key, Long version, String projectKeyToString) {
		KindModel model = get(key, version);
		if(model != null) {
			Key projectKey = Datastore.stringToKey(projectKeyToString);
			if(model.getProjectKey().equals(projectKey) != false) {
				//プロジェクトが合致しない場合、戻り値はnull
				model = null;
			}
		}
		return null;
	}
	
	/**
	 * List取得.
	 * プロジェクトKeyに合致するデータを全件取得します。
	 * @param projectKeyString プロジェクトKey文字列
	 * @return 該当レコード
	 */
	public List<KindModel> getAllList(String projectKeyString) {
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		
		KindModelMeta e = (KindModelMeta) getModelMeta();
		return Datastore.query(e).filter(e.projectKey.equal(projectKey))
				.sortInMemory(e.sortNum.asc, e.key.asc).asList();
	}
	
	/**
	 * List取得.
	 * 指定したKey配列に合致するデータを取得します。
	 * @param projectKeyString プロジェクトKey文字列
	 * @param keys key配列
	 * @return 該当レコード
	 */
	public Map<Key, KindModel> getList(String projectKeyString, Key...keys) {
		KindModelMeta e = (KindModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		Map<Key, KindModel> map = new HashMap<Key, KindModel>();
		if(keys != null && keys.length != 0) {
			filterSet.add(e.key.in(keys));
		} else {
			return map;
		}
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		filterSet.add(e.projectKey.equal(projectKey));
		
		List<KindModel> list = Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0])).asList();
		for(KindModel target : list) {
			map.put(target.getKey(), target);
		}
		return map;
	}
}
