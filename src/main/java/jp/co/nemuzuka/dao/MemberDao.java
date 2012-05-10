/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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

	private static MemberDao memberDao = new MemberDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static MemberDao getInstance() {
		return memberDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private MemberDao() {}

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

		return Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0]))
				.sortInMemory(e.authority.asc, e.key.asc).asList();
	}
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getAllList()
	 */
	@SuppressWarnings("unchecked")
	public List<MemberModel> getAllList() {
		MemberModelMeta e = (MemberModelMeta) getModelMeta();
		List<MemberModel> list = super.getAllList(e.authority.asc, e.key.asc);
		return list;
	}
}
