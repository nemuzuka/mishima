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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.meta.UploadFileModelMeta;
import jp.co.nemuzuka.model.UploadFileModel;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * UploadFileModelに対するDao.
 * @author kazumune
 */
public class UploadFileDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return UploadFileModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return UploadFileModel.class;
	}

	private static UploadFileDao uploadFileDao = new UploadFileDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static UploadFileDao getInstance() {
		return uploadFileDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private UploadFileDao() {}
	
	/**
	 * 詳細情報取得.
	 * 引数の情報が合致するデータを取得します。
	 * @param uploadFileKeyString UploadFile Key
	 * @param ticketKeyToString ticket Key
	 * @param projectKeyString project Key
	 * @param version バージョン
	 * @return 該当インスタンス(該当レコードが存在しない場合、null)
	 */
	public UploadFileModel get(String uploadFileKeyString, String ticketKeyToString, 
			String projectKeyString, Long version) {
		
		Key key = Datastore.stringToKey(uploadFileKeyString);
		Key parentKey = Datastore.stringToKey(ticketKeyToString);
		Key projectKey = Datastore.stringToKey(projectKeyString);
		
		UploadFileModel model = get(key, version);
		if(model == null || 
				model.getParentKey().equals(parentKey) == false || 
				model.getProjectKey().equals(projectKey) == false) {
			return null;
		}
		return model;
	}
	
	/**
	 * ファイル一覧取得.
	 * 引数に合致するファイル一覧情報を取得します。
	 * @param ticketKeyToString チケットKey文字列
	 * @param projectKeyString プロジェクトKey文字列
	 * @return ファイル一覧情報
	 */
	public List<UploadFileModel> getList(String ticketKeyToString,
			String projectKeyString) {
		UploadFileModelMeta e = (UploadFileModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		if(StringUtils.isNotEmpty(ticketKeyToString)) {
			filterSet.add(e.parentKey.equal(Datastore.stringToKey(ticketKeyToString)));
		}
		if(StringUtils.isNotEmpty(projectKeyString)) {
			filterSet.add(e.projectKey.equal(Datastore.stringToKey(projectKeyString)));
		}
		return Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0]))
				.sortInMemory(e.key.asc).asList();
	}
}
