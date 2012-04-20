package jp.co.nemuzuka.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.entity.MilestoneModelEx;
import jp.co.nemuzuka.meta.MilestoneModelMeta;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * MilestoneModelに対するDao.
 * @author kazumune
 */
public class MilestoneDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return MilestoneModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return MilestoneModel.class;
	}

	private static MilestoneDao milestoneDao = new MilestoneDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static MilestoneDao getInstance() {
		return milestoneDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private MilestoneDao() {}

	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param keyToString 文字列化Key
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public MilestoneModel get(String keyToString) {
		Key key = Datastore.stringToKey(keyToString);
		MilestoneModel model = get(key);
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
	public MilestoneModel get(Key key, Long version, String projectKeyToString) {
		MilestoneModel model = get(key, version);
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
	public List<MilestoneModelEx> getAllList(String projectKeyString) {
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		
		MilestoneModelMeta e = (MilestoneModelMeta) getModelMeta();
		List<MilestoneModel> list = Datastore.query(e).filter(e.projectKey.equal(projectKey))
				.sortInMemory(e.sortNum.asc, e.key.asc).asList();
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		List<MilestoneModelEx> retList = new ArrayList<MilestoneModelEx>();
		for(MilestoneModel target : list) {
			
			MilestoneModelEx model = new MilestoneModelEx();
			model.model = target;
			model.startDate = ConvertUtils.toString(target.getStartDate(), sdf);
			model.endDate = ConvertUtils.toString(target.getEndDate(), sdf);
			retList.add(model);
		}
		return retList;
	}
	
	/**
	 * Map取得.
	 * 指定したKey配列に合致するデータを取得します。
	 * @param projectKeyString プロジェクトKey文字列
	 * @param keys key配列
	 * @return 該当Map
	 */
	public Map<Key, MilestoneModel> getMap(String projectKeyString, Key...keys) {
		MilestoneModelMeta e = (MilestoneModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		Map<Key, MilestoneModel> map = new HashMap<Key, MilestoneModel>();
		if(keys != null && keys.length != 0) {
			filterSet.add(e.key.in(keys));
		} else {
			return map;
		}
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		filterSet.add(e.projectKey.equal(projectKey));
		
		List<MilestoneModel> list = Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0])).asList();
		for(MilestoneModel target : list) {
			map.put(target.getKey(), target);
		}
		return map;
	}
}
