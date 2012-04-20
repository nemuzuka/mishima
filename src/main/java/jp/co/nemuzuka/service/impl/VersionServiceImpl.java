package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.dao.VersionDao;
import jp.co.nemuzuka.form.VersionForm;
import jp.co.nemuzuka.model.VersionModel;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.LabelValueBeanUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * VersionServiceの実装クラス.
 * @author kazumune
 */
public class VersionServiceImpl implements VersionService {

	VersionDao versionDao = VersionDao.getInstance();

	private static VersionServiceImpl impl = new VersionServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static VersionServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private VersionServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#get(java.lang.String)
	 */
	@Override
	public VersionForm get(String projectKeyString) {
		VersionForm form = new VersionForm();
		if(StringUtils.isNotEmpty(projectKeyString)) {
			VersionModel model = versionDao.get4ProjectKey(projectKeyString);
			setForm(form, model);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#put(jp.co.nemuzuka.form.VersionForm, java.lang.String)
	 */
	@Override
	public void put(VersionForm form, String projectKeyString) {
		VersionModel model = null;
		Long version = ConvertUtils.toLong(form.versionNo);

		if(version != null) {
			//更新の場合
			//versionとKeyで情報を取得
			Key key = Datastore.stringToKey(form.keyToString);
			model = versionDao.get(key, version);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new VersionModel();
			//プロジェクトKeyを元にKeyを生成
			Key key = Datastore.createKey(VersionModel.class, projectKeyString);
			model.setKey(key);
		}
		setModel(model, form);
		versionDao.put(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.VersionService#getList(java.lang.String)
	 */
	@Override
	public List<LabelValueBean> getList(String projectKeyString) {
		VersionForm form = get(projectKeyString);
		return LabelValueBeanUtils.createList(form.versionName, true);
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
		form.versionName = model.getVersionName().getValue();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定元Form
	 */
	private void setModel(VersionModel model, VersionForm form) {
		model.setVersionName(new Text(form.versionName));
	}

}
