package jp.co.nemuzuka.dao;

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
				.sortInMemory(e.projectId.asc, e.key.asc).asList();
	}
}
