package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.dao.VersionDao;
import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.model.VersionModel;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.utils.ConvertUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * VersionServiceの実装クラス.
 * @author kazumune
 */
public class VersionServiceImpl implements VersionService {

	VersionDao versionDao = new VersionDao();
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#get(java.lang.String)
	 */
	@Override
	public VersionForm get(String keyString) {

		VersionForm form = new VersionForm();
		if(StringUtils.isNotEmpty(keyString)) {
			VersionModel model = versionDao.get(keyString);
			setForm(form, model);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#put(jp.co.nemuzuka.form.VersionForm, java.lang.String)
	 */
	@Override
	public void put(VersionForm form, String projectKeyToString) {

		VersionModel model = null;
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//versionとKeyとprojectKeyで情報を取得
			model = versionDao.get(key, version, projectKeyToString);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new VersionModel();
		}
		setModel(model, form, projectKeyToString);
		versionDao.put(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#delete(jp.co.nemuzuka.form.VersionForm, java.lang.String)
	 */
	@Override
	public void delete(VersionForm form, String projectKeyToString) {
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//versionとKeyとprojectKeyで情報を取得
		VersionModel model = versionDao.get(key, version, projectKeyToString);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		versionDao.delete(model.getKey());
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#getAllList(java.lang.String)
	 */
	@Override
	public List<VersionModel> getAllList(String projectKeyToString) {
		return versionDao.getAllList(projectKeyToString);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#updateSortNum(java.lang.String[], java.lang.String)
	 */
	@Override
	public void updateSortNum(String[] sortedKeyToString,
			String projectKeyToString) {
		
		//更新対象のKeyを取得
		Set<Key> keys = new LinkedHashSet<Key>();
		for(String target : sortedKeyToString) {
			keys.add(Datastore.stringToKey(target));
		}
		if(keys.size() == 0) {
			return;
		}
		Map<Key, VersionModel> map = versionDao.getMap(projectKeyToString, keys.toArray(new Key[0]));
		
		//ソート順を1から採番して更新する
		long sortNum = 1;
		for(Key target : keys) {
			VersionModel model = map.get(target);
			if(model != null) {
				model.setSortNum(sortNum);
				versionDao.put(model);
				sortNum++;
			}
		}
	}

	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定元Model
	 */
	private void setForm(VersionForm form, VersionModel model) {
		if(model == null) {
			return;
		}
		form.keyToString = model.getKeyToString();
		form.versionName = model.getVersionName();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	private void setModel(VersionModel model, VersionForm form, String projectKeyToString) {
		
		if(model.getKey() == null) {
			//新規の場合
			Key projectKey = Datastore.stringToKey(projectKeyToString);
			model.setProjectKey(projectKey);
			model.setSortNum(Long.MAX_VALUE);
		}
		
		model.setVersionName(form.versionName);
	}
}
