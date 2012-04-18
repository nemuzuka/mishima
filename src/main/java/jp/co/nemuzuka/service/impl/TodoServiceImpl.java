package jp.co.nemuzuka.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import jp.co.nemuzuka.common.PeriodStatus;
import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.dao.TodoDao.Param;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoModel;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

/**
 * TodoServiceの実装クラス.
 * @author k-katagiri
 */
public class TodoServiceImpl implements TodoService {

	TodoDao todoDao = new TodoDao();
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#getList(jp.co.nemuzuka.dao.TodoDao.Param)
	 */
	@Override
	public List<TodoModelEx> getList(Param param, boolean isDashboard) {
		
		List<TodoModel> modelList = todoDao.getList(param);
		if(isDashboard) {
			modelList = todoDao.getDashbordList(param.limit, param.email);
		} else {
			modelList = todoDao.getList(param);
		}
		
		List<TodoModelEx> list = new ArrayList<TodoModelEx>();
		Date today = CurrentDateUtils.getInstance().getCurrentDate();
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		SimpleDateFormat sdf2 = DateTimeUtils.createSdf("yyyy/MM/dd HH:mm");
		for(TodoModel target : modelList) {
			TodoModelEx entity = new TodoModelEx();
			entity.setModel(target);
			entity.setPeriod(ConvertUtils.toString(target.getPeriod(), sdf));
			entity.setCreatedAt(ConvertUtils.toString(target.getCreatedAt(), sdf2));
			
			//期限が設定されている場合
			if(target.getPeriod() != null) {
				entity.setPeriodStatus(
						createPeriodStatus(today, target.getPeriod(), target.getStatus()));
			}
			list.add(entity);
		}
		return list;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#get(java.lang.String, java.lang.String)
	 */
	@Override
	public TodoForm get(String keyString, String mail) {
		
		TodoForm form = new TodoForm();
		if(StringUtils.isNotEmpty(keyString)) {
			//Key情報が設定されていた場合
			Key key = Datastore.stringToKey(keyString);
			Key memberKey = Datastore.createKey(MemberModel.class, mail);
			TodoModel model = todoDao.getWithMemberKey(key, memberKey);
			setForm(form, model);
		}
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#put(jp.co.nemuzuka.form.TodoForm, java.lang.String)
	 */
	@Override
	public void put(TodoForm form, String mail) {
		
		TodoModel model = null;
		Key memberKey = Datastore.createKey(MemberModel.class, mail);
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//keyとバージョンとメンバーKeyでデータを取得
			model = todoDao.get(key, version, memberKey);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//登録者の情報として設定
			model = new TodoModel();
			model.setCreateMemberKey(memberKey);
		}
		//取得した情報に対してプロパティを更新
		setModel(model, form);
		todoDao.put(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#delete(jp.co.nemuzuka.form.TodoForm, java.lang.String)
	 */
	@Override
	public void delete(TodoForm form, String mail) {
		Key key = Datastore.stringToKey(form.keyToString);
		Key memberKey = Datastore.createKey(MemberModel.class, mail);
		Long version = ConvertUtils.toLong(form.versionNo);
		//keyとバージョンとメンバーKeyでデータを取得
		TodoModel model = todoDao.get(key, version, memberKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		todoDao.delete(key);
	}

	/**
	 * 期限ステータス判断.
	 * @param today システム日付
	 * @param targetDate 期限
	 * @param todoStatus TODOステータス
	 * @return 期限ステータス値
	 */
	PeriodStatus createPeriodStatus(Date today, Date targetDate, TodoStatus todoStatus) {
		long toDayTime = today.getTime();
		long periodTime = targetDate.getTime();
		
		//ステータスが未完了の場合
		PeriodStatus periodStatus = null;
		switch(todoStatus) {
			case doing:case nothing:
				if(toDayTime == periodTime) {
					periodStatus = PeriodStatus.today;
				} else if(toDayTime > periodTime) {
					periodStatus = PeriodStatus.periodDate;
				}
				break;
		}
		return periodStatus;
	}
	
	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定Model
	 */
	private void setForm(TodoForm form, TodoModel model) {
		if(model == null) {
			return;
		}
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		form.keyToString = model.getKeyToString();
		form.status = model.getStatus().getCode();
		form.title = model.getTitle();
		form.content = model.getContent().getValue();
		form.period = ConvertUtils.toString(model.getPeriod(), sdf);
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定Form
	 */
	private void setModel(TodoModel model, TodoForm form) {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		
		TodoStatus status = TodoStatus.fromCode(form.status);
		if(status == null) {
			status = TodoStatus.nothing;
		}
		model.setStatus(status);
		model.setTitle(form.title);
		model.setContent(new Text(StringUtils.defaultString(form.content)));
		model.setPeriod(ConvertUtils.toDate(form.period, sdf));
		model.setVersion(ConvertUtils.toLong(form.versionNo));
	}


}
