package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.dao.StatusDao;
import jp.co.nemuzuka.form.StatusForm;
import jp.co.nemuzuka.model.StatusModel;
import jp.co.nemuzuka.service.StatusService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.LabelValueBeanUtils;

/**
 * StatusServiceの実装クラス.
 * @author k-katagiri
 */
public class StatusServiceImpl implements StatusService {

	StatusDao statusDao = StatusDao.getInstance();
	TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();

	private static StatusServiceImpl impl = new StatusServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static StatusServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private StatusServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.StatusService#get(java.lang.String)
	 */
	@Override
	public StatusForm get(String projectKeyString) {
		StatusForm form = new StatusForm();
		if(StringUtils.isNotEmpty(projectKeyString)) {
			StatusModel model = statusDao.get4ProjectKey(projectKeyString);
			setForm(form, model);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.StatusService#put(jp.co.nemuzuka.form.StatusForm, java.lang.String)
	 */
	@Override
	public void put(StatusForm form, String projectKeyString) {
		StatusModel model = null;
		Long version = ConvertUtils.toLong(form.versionNo);

		if(version != null) {
			//更新の場合
			//versionとKeyで情報を取得
			Key key = Datastore.stringToKey(form.keyToString);
			model = statusDao.get(key, version);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new StatusModel();
			//プロジェクトKeyを元にKeyを生成
			Key key = Datastore.createKey(StatusModel.class, projectKeyString);
			model.setKey(key);
		}
		setModel(model, form);
		statusDao.put(model);
		ticketMstService.initRefreshStartTime(projectKeyString);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.StatusService#getList(java.lang.String)
	 */
	@Override
	public List<LabelValueBean> getList(String projectKeyString) {
		StatusForm form = get(projectKeyString);
		return LabelValueBeanUtils.createList(form.statusName, true);
	}

	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定元Model
	 */
	private void setForm(StatusForm form, StatusModel model) {
		if(model == null) {
			return;
		}
		form.keyToString = model.getKeyToString();
		form.statusName = model.getStatusName().getValue();
		form.closeStatusName = model.getCloseStatusName().getValue();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定元Form
	 */
	private void setModel(StatusModel model, StatusForm form) {
		model.setStatusName(new Text(form.statusName));
		model.setCloseStatusName(new Text(StringUtils.defaultString(form.closeStatusName, "")));
	}

}
