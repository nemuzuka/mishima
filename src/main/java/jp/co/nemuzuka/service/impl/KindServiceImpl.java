package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.dao.KindDao;
import jp.co.nemuzuka.form.KindForm;
import jp.co.nemuzuka.model.KindModel;
import jp.co.nemuzuka.service.KindService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.LabelValueBeanUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * KindServiceの実装クラス.
 * @author kazumune
 */
public class KindServiceImpl implements KindService {

	KindDao kindDao = KindDao.getInstance();
	TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	
	private static KindServiceImpl impl = new KindServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static KindServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private KindServiceImpl(){}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#get(java.lang.String)
	 */
	@Override
	public KindForm get(String projectKeyString) {

		KindForm form = new KindForm();
		if(StringUtils.isNotEmpty(projectKeyString)) {
			KindModel model = kindDao.get4ProjectKey(projectKeyString);
			setForm(form, model);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#put(jp.co.nemuzuka.form.KindForm)
	 */
	@Override
	public void put(KindForm form, String projectKeyString) {

		KindModel model = null;
		Long version = ConvertUtils.toLong(form.versionNo);

		if(version != null) {
			//更新の場合
			//versionとKeyで情報を取得
			Key key = Datastore.stringToKey(form.keyToString);
			model = kindDao.get(key, version);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new KindModel();
			//プロジェクトKeyを元にKeyを生成
			Key key = Datastore.createKey(KindModel.class, projectKeyString);
			model.setKey(key);
		}
		setModel(model, form);
		kindDao.put(model);
		ticketMstService.initRefreshStartTime(projectKeyString);
	}


	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.KindService#getList(java.lang.String)
	 */
	@Override
	public List<LabelValueBean> getList(String projectKeyToString) {
		KindForm form = get(projectKeyToString);
		return LabelValueBeanUtils.createList(form.getKindName(), true);
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
		form.kindName = model.getKindName().getValue();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}
	
	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定元Form
	 */
	private void setModel(KindModel model, KindForm form) {
		model.setKindName(new Text(form.kindName));
	}
}
