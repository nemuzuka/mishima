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

import jp.co.nemuzuka.meta.VersionModelMeta;
import jp.co.nemuzuka.model.VersionModel;

import org.slim3.datastore.Datastore;
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

	private static VersionDao versionDao = new VersionDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static VersionDao getInstance() {
		return versionDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private VersionDao() {}

	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param projectKeyToString 文字列化プロジェクトKey
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public VersionModel get4ProjectKey(String projectKeyToString) {
		
		Key key = Datastore.createKey(getModelClass(), projectKeyToString);
		return get(key);
	}
}
