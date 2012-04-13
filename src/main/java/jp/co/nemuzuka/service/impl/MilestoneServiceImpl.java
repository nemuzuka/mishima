package jp.co.nemuzuka.service.impl;

import java.text.SimpleDateFormat;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.dao.MilestoneDao;
import jp.co.nemuzuka.entity.MilestoneModelEx;
import jp.co.nemuzuka.form.MilestoneForm;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * MilestoneServiceの実装クラス.
 * @author kazumune
 */
public class MilestoneServiceImpl implements MilestoneService {

	MilestoneDao milestoneDao = new MilestoneDao();
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MilestoneService#get(java.lang.String)
	 */
	@Override
	public MilestoneForm get(String keyString) {

		MilestoneForm form = new MilestoneForm();
		if(StringUtils.isNotEmpty(keyString)) {
			MilestoneModel model = milestoneDao.get(keyString);
			setForm(form, model);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MilestoneService#put(jp.co.nemuzuka.form.MilestoneForm, java.lang.String)
	 */
	@Override
	public void put(MilestoneForm form, String projectKeyToString) {

		MilestoneModel model = null;
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//versionとKeyとprojectKeyで情報を取得
			model = milestoneDao.get(key, version, projectKeyToString);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new MilestoneModel();
		}
		setModel(model, form, projectKeyToString);
		milestoneDao.put(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MilestoneService#delete(jp.co.nemuzuka.form.MilestoneForm, java.lang.String)
	 */
	@Override
	public void delete(MilestoneForm form, String projectKeyToString) {
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//versionとKeyとprojectKeyで情報を取得
		MilestoneModel model = milestoneDao.get(key, version, projectKeyToString);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		milestoneDao.delete(model.getKey());
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MilestoneService#getAllList(java.lang.String)
	 */
	@Override
	public List<MilestoneModelEx> getAllList(String projectKeyToString) {
		return milestoneDao.getAllList(projectKeyToString);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MilestoneService#updateSortNum(java.lang.String[], java.lang.String)
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
		Map<Key, MilestoneModel> map = milestoneDao.getMap(projectKeyToString, keys.toArray(new Key[0]));
		
		//ソート順を1から採番して更新する
		long sortNum = 1;
		for(Key target : keys) {
			MilestoneModel model = map.get(target);
			if(model != null) {
				model.setSortNum(sortNum);
				milestoneDao.put(model);
				sortNum++;
			}
		}
	}

	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定元Model
	 */
	private void setForm(MilestoneForm form, MilestoneModel model) {
		if(model == null) {
			return;
		}
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		form.keyToString = model.getKeyToString();
		form.milestoneName = model.getMilestoneName();
		form.startDate = ConvertUtils.toString(model.getStartDate(), sdf);
		form.endDate = ConvertUtils.toString(model.getEndDate(), sdf);
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	private void setModel(MilestoneModel model, MilestoneForm form, String projectKeyToString) {
		
		if(model.getKey() == null) {
			//新規の場合
			Key projectKey = Datastore.stringToKey(projectKeyToString);
			model.setProjectKey(projectKey);
			model.setSortNum(Long.MAX_VALUE);
		}
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		model.setMilestoneName(form.milestoneName);
		model.setStartDate(ConvertUtils.toDate(form.startDate, sdf));
		model.setEndDate(ConvertUtils.toDate(form.endDate, sdf));
	}
}
