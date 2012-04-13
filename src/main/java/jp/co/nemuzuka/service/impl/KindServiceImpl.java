package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.dao.KindDao;
import jp.co.nemuzuka.form.KindForm;
import jp.co.nemuzuka.model.KindModel;
import jp.co.nemuzuka.service.KindService;
import jp.co.nemuzuka.utils.ConvertUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * KindServiceの実装クラス.
 * @author kazumune
 */
public class KindServiceImpl implements KindService {

	KindDao kindDao = new KindDao();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#get(java.lang.String)
	 */
	@Override
	public KindForm get(String keyString) {

		KindForm form = new KindForm();
		if(StringUtils.isNotEmpty(keyString)) {
			KindModel model = kindDao.get(keyString);
			setForm(form, model);
		}
		return form;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#put(jp.co.nemuzuka.form.KindForm, java.lang.String)
	 */
	@Override
	public void put(KindForm form, String projectKeyToString) {

		KindModel model = null;
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//versionとKeyとprojectKeyで情報を取得
			model = kindDao.get(key, version, projectKeyToString);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new KindModel();
		}
		setModel(model, form, projectKeyToString);
		kindDao.put(model);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#delete(jp.co.nemuzuka.form.KindForm, java.lang.String)
	 */
	@Override
	public void delete(KindForm form, String projectKeyToString) {
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//versionとKeyとprojectKeyで情報を取得
		KindModel model = kindDao.get(key, version, projectKeyToString);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		kindDao.delete(model.getKey());
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#getAllList(java.lang.String)
	 */
	@Override
	public List<KindModel> getAllList(String projectKeyToString) {
		return kindDao.getAllList(projectKeyToString);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#updateSortNum(java.lang.String[], java.lang.String)
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
		Map<Key, KindModel> map = kindDao.getMap(projectKeyToString, keys.toArray(new Key[0]));
		
		//ソート順を1から採番して更新する
		long sortNum = 1;
		for(Key target : keys) {
			KindModel model = map.get(target);
			if(model != null) {
				model.setSortNum(sortNum);
				kindDao.put(model);
				sortNum++;
			}
		}
	}

	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定元Model
	 */
	private void setForm(KindForm form, KindModel model) {
		if(model == null) {
			return;
		}
		form.keyToString = model.getKeyToString();
		form.kindName = model.getKindName();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	private void setModel(KindModel model, KindForm form, String projectKeyToString) {
		
		if(model.getKey() == null) {
			//新規の場合
			Key projectKey = Datastore.stringToKey(projectKeyToString);
			model.setProjectKey(projectKey);
			model.setSortNum(Long.MAX_VALUE);
		}
		
		model.setKindName(form.kindName);
	}
}
