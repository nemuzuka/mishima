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

import java.util.List;

import jp.co.nemuzuka.meta.TodoTagModelMeta;
import jp.co.nemuzuka.model.TodoTagModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * TodoTagModelに対するDao.
 * @author kazumune
 */
public class TodoTagDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return TodoTagModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return TodoTagModel.class;
	}

	private static TodoTagDao todoTagDao = new TodoTagDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TodoTagDao getInstance() {
		return todoTagDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private TodoTagDao() {}

	
	/**
	 * TODOTagList取得.
	 * メンバーKeyに紐付くTODOTagListを取得します。
	 * @param memberKey メンバーKey
	 * @return TODOTagList
	 */
	public List<TodoTagModel> getList(Key memberKey) {
		TodoTagModelMeta e = (TodoTagModelMeta) getModelMeta();
		return Datastore.query(e).filterInMemory(e.memberKey.equal(memberKey))
				.sortInMemory(e.key.asc).asList();
	}
	
	/**
	 * Model取得.
	 * @param key TodoTagModelのKey
	 * @param version TodoTagModelのバージョン
	 * @param memberKey メンバーKey
	 * @return 存在すればModelインスタンス
	 */
	public TodoTagModel get(Key key, Long version, Key memberKey) {
		
		TodoTagModel model = get(key, version);
		if(model != null) {
			if(model.getMemberKey().equals(memberKey) == false) {
				model = null;
			}
		}
		return model;
	}
}
