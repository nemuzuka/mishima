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

import jp.co.nemuzuka.meta.ProjectMemberModelMeta;
import jp.co.nemuzuka.model.ProjectMemberModel;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.ModelMeta;

import com.google.appengine.api.datastore.Key;

/**
 * ProjectMemberModelに対するDao.
 * @author kazumune
 */
public class ProjectMemberDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return ProjectMemberModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return ProjectMemberModel.class;
	}
	
	private static ProjectMemberDao projectMemberDao = new ProjectMemberDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static ProjectMemberDao getInstance() {
		return projectMemberDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private ProjectMemberDao() {}

	/**
	 * get処理.
	 * @param projectKey プロジェクトKey
	 * @param memberKey メンバーKey
	 * @return 該当データが存在する場合、Modelインスタンス。該当データが存在しない場合、null
	 */
	public ProjectMemberModel get(Key projectKey, Key memberKey) {
		String projectKeyString = Datastore.keyToString(projectKey);
		String memberKeyString = Datastore.keyToString(memberKey);
		
		String keyString = projectKeyString + ":" + memberKeyString;
		Key key = Datastore.createKey(ProjectMemberModel.class, keyString);
		return get(key);
	}
	
	/**
	 * List取得.
	 * @param projectKeyString プロジェクトKey文字列
	 * @param memberKeyString メンバーKey文字列
	 * @return 該当レコード
	 */
	public List<ProjectMemberModel> getList(String projectKeyString, String memberKeyString) {
		ProjectMemberModelMeta e = (ProjectMemberModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		if(StringUtils.isNotEmpty(projectKeyString)) {
			Key key = Datastore.stringToKey(projectKeyString);
			filterSet.add(e.projectKey.equal(key));
		}
		if(StringUtils.isNotEmpty(memberKeyString)) {
			Key key = Datastore.stringToKey(memberKeyString);
			filterSet.add(e.memberKey.equal(key));
		}
		return Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0]))
				.sortInMemory(e.projectKey.asc, e.projectAuthority.asc, e.memberKey.asc).asList();
	}
}
