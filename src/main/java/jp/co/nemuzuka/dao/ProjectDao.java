package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.meta.ProjectModelMeta;
import jp.co.nemuzuka.model.ProjectModel;

import org.slim3.datastore.ModelMeta;

/**
 * ProjectModelに対するDao.
 * @author kazumune
 */
public class ProjectDao extends AbsDao {

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

}
