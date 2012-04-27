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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.meta.ProjectModelMeta;
import jp.co.nemuzuka.model.ProjectModel;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * ProjectModelに対するDao.
 * @author kazumune
 */
public class ProjectDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return ProjectModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return ProjectModel.class;
	}

	private static ProjectDao projectDao = new ProjectDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static ProjectDao getInstance() {
		return projectDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private ProjectDao() {}

	/**
	 * get処理.
	 * 引数の情報に合致するModelを取得します。
	 * @param keyToString 文字列化Key
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public ProjectModel get(String keyToString) {
		Key key = Datastore.stringToKey(keyToString);
		ProjectModel model = get(key);
		return model;
	}
	
	/**
	 * List取得.
	 * @param projectName プロジェクト名(前方一致)
	 * @return 該当レコード
	 */
	public List<ProjectModel> getList(String projectName) {
		ProjectModelMeta e = (ProjectModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		if(StringUtils.isNotEmpty(projectName)) {
			filterSet.add(e.projectName.startsWith(projectName));
		}
		return Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0]))
				.sortInMemory(e.key.asc).asList();
	}
	
	/**
	 * List取得.
	 * @param keys key配列
	 * @return 該当レコード
	 */
	public List<ProjectModel> getList(Key...keys) {
		ProjectModelMeta e = (ProjectModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		if(keys != null && keys.length != 0) {
			filterSet.add(e.key.in(keys));
		} else {
			return new ArrayList<ProjectModel>();
		}
		return Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0]))
				.sortInMemory(e.key.asc).asList();
	}
}
