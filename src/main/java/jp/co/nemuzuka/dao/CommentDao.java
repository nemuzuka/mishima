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

import jp.co.nemuzuka.meta.CommentModelMeta;
import jp.co.nemuzuka.model.CommentModel;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * CommentModelに対するDao.
 * @author kazumune
 */
public class CommentDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return CommentModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return CommentModel.class;
	}

	private static CommentDao commentDao = new CommentDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static CommentDao getInstance() {
		return commentDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private CommentDao() {}

	/**
	 * List取得.
	 * 取得元Keyに合致するComment一覧を返却します
	 * @param refsKey 登録元Key
	 * @return 該当レコード
	 */
	public List<CommentModel> getList(Key refsKey) {
		CommentModelMeta e = (CommentModelMeta) getModelMeta();
		
		return Datastore.query(e).filter(e.refsKey.equal(refsKey))
				.sortInMemory(e.createdAt.asc, e.key.asc).asList();
	}
	
	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param keyString 文字列化CommentKey
	 * @param versionNo バージョンNo
	 * @param refsKey 登録元Key
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public CommentModel get(String keyString, Long versionNo, Key refsKey) {
		Key key = Datastore.stringToKey(keyString);
		
		CommentModel model = get(key, versionNo);
		if(model != null) {
			if(model.getRefsKey().equals(refsKey) == false) {
				model = null;
			}
		}
		return model;
	}
}
