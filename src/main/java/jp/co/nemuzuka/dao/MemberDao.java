package jp.co.nemuzuka.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.meta.MemberModelMeta;
import jp.co.nemuzuka.model.MemberModel;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * MemberModelに対するDao.
 * @author kazumune
 */
public class MemberDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModel()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return MemberModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	Class getModelClass() {
		return MemberModel.class;
	}

	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param email メールアドレス
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public MemberModel get(String email) {
		Key key = Datastore.createKey(MemberModel.class, email);
		return get(key);
	}
	
	/**
	 * List取得.
	 * @param name 氏名(前方一致)
	 * @param mail メール(前方一致)
	 * @return 該当レコード
	 */
	public List<MemberModel> getList(String name, String mail) {
		MemberModelMeta e = (MemberModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		if(StringUtils.isNotEmpty(name)) {
			filterSet.add(e.name.startsWith(name));
		}
		if(StringUtils.isNotEmpty(mail)) {
			filterSet.add(e.mail.startsWith(mail));
		}
		return Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0]))
				.sortInMemory(e.authority.asc, e.key.asc).asList();
	}
	
	/**
	 * Map取得.
	 * 指定したKey配列に合致するデータを取得します。
	 * @param keys key配列
	 * @return 該当Map
	 */
	public Map<Key, MemberModel> getMap(Key...keys) {
		Map<Key, MemberModel> map = new HashMap<Key, MemberModel>();
		List<MemberModel> list = getList(keys);
		for(MemberModel target : list) {
			map.put(target.getKey(), target);
		}
		return map;
	}
	
	/**
	 * List取得.
	 * 指定したKey配列に合致するデータを取得します。
	 * @param keys Key配列
	 * @return 該当Map
	 */
	public List<MemberModel> getList(Key...keys) {
		MemberModelMeta e = (MemberModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		
		if(keys != null && keys.length != 0) {
			filterSet.add(e.key.in(keys));
		} else {
			return new ArrayList<MemberModel>();
		}

		return Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0])).asList();
	}
	
}
