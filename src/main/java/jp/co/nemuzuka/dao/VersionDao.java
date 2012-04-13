package jp.co.nemuzuka.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.meta.VersionModelMeta;
import jp.co.nemuzuka.model.VersionModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * VersionModelに対するDao.
 * @author kazumune
 */
public class VersionDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return VersionModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return VersionModel.class;
	}

	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param keyToString 文字列化Key
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public VersionModel get(String keyToString) {
		Key key = Datastore.stringToKey(keyToString);
		VersionModel model = get(key);
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
	public VersionModel get(Key key, Long version, String projectKeyToString) {
		VersionModel model = get(key, version);
		if(model != null) {
			Key projectKey = Datastore.stringToKey(projectKeyToString);
			if(model.getProjectKey().equals(projectKey) == false) {
				//プロジェクトが合致しない場合、戻り値はnull
				model = null;
			}
		}
		return model;
	}
	
	/**
	 * List取得.
	 * プロジェクトKeyに合致するデータを全件取得します。
	 * @param projectKeyString プロジェクトKey文字列
	 * @return 該当レコード
	 */
	public List<VersionModel> getAllList(String projectKeyString) {
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		
		VersionModelMeta e = (VersionModelMeta) getModelMeta();
		return Datastore.query(e).filter(e.projectKey.equal(projectKey))
				.sortInMemory(e.sortNum.asc, e.key.asc).asList();
	}
	
	/**
	 * Map取得.
	 * 指定したKey配列に合致するデータを取得します。
	 * @param projectKeyString プロジェクトKey文字列
	 * @param keys key配列
	 * @return 該当Map
	 */
	public Map<Key, VersionModel> getMap(String projectKeyString, Key...keys) {
		VersionModelMeta e = (VersionModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		Map<Key, VersionModel> map = new HashMap<Key, VersionModel>();
		if(keys != null && keys.length != 0) {
			filterSet.add(e.key.in(keys));
		} else {
			return map;
		}
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		filterSet.add(e.projectKey.equal(projectKey));
		
		List<VersionModel> list = Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0])).asList();
		for(VersionModel target : list) {
			map.put(target.getKey(), target);
		}
		return map;
	}
}
