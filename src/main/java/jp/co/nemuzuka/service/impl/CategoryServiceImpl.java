package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.dao.CategoryDao;
import jp.co.nemuzuka.form.CategoryForm;
import jp.co.nemuzuka.model.CategoryModel;
import jp.co.nemuzuka.service.CategoryService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.LabelValueBeanUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * CategoryServiceの実装クラス.
 * @author kazumune
 */
public class CategoryServiceImpl implements CategoryService {

	CategoryDao categoryDao = CategoryDao.getInstance();
	TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();

	private static CategoryServiceImpl impl = new CategoryServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static CategoryServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private CategoryServiceImpl(){}
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#get(java.lang.String)
	 */
	@Override
	public CategoryForm get(String projectKeyString) {
		CategoryForm form = new CategoryForm();
		if(StringUtils.isNotEmpty(projectKeyString)) {
			CategoryModel model = categoryDao.get4ProjectKey(projectKeyString);
			setForm(form, model);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#put(jp.co.nemuzuka.form.CategoryForm, java.lang.String)
	 */
	@Override
	public void put(CategoryForm form, String projectKeyString) {
		CategoryModel model = null;
		Long version = ConvertUtils.toLong(form.versionNo);

		if(version != null) {
			//更新の場合
			//versionとKeyで情報を取得
			Key key = Datastore.stringToKey(form.keyToString);
			model = categoryDao.get(key, version);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new CategoryModel();
			//プロジェクトKeyを元にKeyを生成
			Key key = Datastore.createKey(CategoryModel.class, projectKeyString);
			model.setKey(key);
		}
		setModel(model, form);
		categoryDao.put(model);
		ticketMstService.initRefreshStartTime(projectKeyString);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.CategoryService#getList(java.lang.String)
	 */
	@Override
	public List<LabelValueBean> getList(String projectKeyString) {
		CategoryForm form = get(projectKeyString);
		return LabelValueBeanUtils.createList(form.categoryName, true);
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
		form.categoryName = model.getCategoryName().getValue();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定元Form
	 */
	private void setModel(CategoryModel model, CategoryForm form) {
		model.setCategoryName(new Text(form.categoryName));
	}
}
