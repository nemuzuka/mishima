package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.dao.CategoryDao;
import jp.co.nemuzuka.form.CategoryForm;
import jp.co.nemuzuka.model.CategoryModel;
import jp.co.nemuzuka.service.CategoryService;
import jp.co.nemuzuka.utils.ConvertUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * CategoryServiceの実装クラス.
 * @author kazumune
 */
public class CategoryServiceImpl implements CategoryService {

	CategoryDao categoryDao = new CategoryDao();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#get(java.lang.String)
	 */
	@Override
	public CategoryForm get(String keyString) {

		CategoryForm form = new CategoryForm();
		if(StringUtils.isNotEmpty(keyString)) {
			CategoryModel model = categoryDao.get(keyString);
			setForm(form, model);
		}
		return form;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#put(jp.co.nemuzuka.form.CategoryForm, java.lang.String)
	 */
	@Override
	public void put(CategoryForm form, String projectKeyToString) {

		CategoryModel model = null;
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//versionとKeyとprojectKeyで情報を取得
			model = categoryDao.get(key, version, projectKeyToString);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new CategoryModel();
		}
		setModel(model, form, projectKeyToString);
		categoryDao.put(model);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#delete(jp.co.nemuzuka.form.CategoryForm, java.lang.String)
	 */
	@Override
	public void delete(CategoryForm form, String projectKeyToString) {
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//versionとKeyとprojectKeyで情報を取得
		CategoryModel model = categoryDao.get(key, version, projectKeyToString);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		categoryDao.delete(model.getKey());
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#getAllList(java.lang.String)
	 */
	@Override
	public List<CategoryModel> getAllList(String projectKeyToString) {
		return categoryDao.getAllList(projectKeyToString);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#updateSortNum(java.lang.String[], java.lang.String)
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
		Map<Key, CategoryModel> map = categoryDao.getMap(projectKeyToString, keys.toArray(new Key[0]));
		
		//ソート順を1から採番して更新する
		long sortNum = 1;
		for(Key target : keys) {
			CategoryModel model = map.get(target);
			if(model != null) {
				model.setSortNum(sortNum);
				categoryDao.put(model);
				sortNum++;
			}
		}
	}

	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定元Model
	 */
	private void setForm(CategoryForm form, CategoryModel model) {
		if(model == null) {
			return;
		}
		form.keyToString = model.getKeyToString();
		form.categoryName = model.getCategoryName();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定Form
	 * @param projectKeyToString プロジェクトKey文字列
	 */
	private void setModel(CategoryModel model, CategoryForm form, String projectKeyToString) {
		
		if(model.getKey() == null) {
			//新規の場合
			Key projectKey = Datastore.stringToKey(projectKeyToString);
			model.setProjectKey(projectKey);
			model.setSortNum(Long.MAX_VALUE);
		}
		
		model.setCategoryName(form.categoryName);
	}
}
