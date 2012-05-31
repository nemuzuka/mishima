/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.nemuzuka.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import jp.co.nemuzuka.common.PeriodStatus;
import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.dao.TodoDao.Param;
import jp.co.nemuzuka.entity.TodoModelEx;
import jp.co.nemuzuka.form.TodoCommentForm;
import jp.co.nemuzuka.form.TodoDetailForm;
import jp.co.nemuzuka.form.TodoForm;
import jp.co.nemuzuka.model.TodoModel;
import jp.co.nemuzuka.service.CommentService;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.TodoService;
import jp.co.nemuzuka.service.TodoTagService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

/**
 * TodoServiceの実装クラス.
 * @author k-katagiri
 */
public class TodoServiceImpl implements TodoService {

	TodoDao todoDao = TodoDao.getInstance();
	CommentService commentService = CommentServiceImpl.getInstance();
	MemberService memberService = MemberServiceImpl.getInstance();
	TodoTagService todoTagService = TodoTagServiceImpl.getInstance();
	
	private static TodoServiceImpl impl = new TodoServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TodoServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private TodoServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#getList(jp.co.nemuzuka.dao.TodoDao.Param)
	 */
	@Override
	public List<TodoModelEx> getList(Param param, boolean isDashboard) {
		
		List<TodoModel> modelList = null;
		if(isDashboard) {
			modelList = todoDao.getDashbordList(param.limit, param.targetMemberKeyString);
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
			entity.setTodoStatus(target.getStatus().getLabel());
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
			Key memberKey = memberService.getKey(mail);
			TodoModel model = todoDao.getWithMemberKey(key, memberKey);
			setForm(form, model);
		}
		//TODOタグ一覧を設定
		form.setTagList(todoTagService.getList(mail));
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#getDetail(java.lang.String, java.lang.String)
	 */
	@Override
	public TodoDetailForm getDetail(String keyString, String mail) {
		
		TodoForm form = get(keyString, mail);
		if(StringUtils.isEmpty(form.keyToString)) {
			return null;
		}
		
		TodoDetailForm detailForm = new TodoDetailForm();
		detailForm.setForm(form);
		detailForm.commentList = commentService.getList(Datastore.stringToKey(form.keyToString));
		
		return detailForm;
	}


	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#updateTodoStatus(jp.co.nemuzuka.form.TodoForm, java.lang.String)
	 */
	@Override
	public void updateTodoStatus(TodoForm form, String email) {
		TodoModel model = null;
		Key memberKey = memberService.getKey(email);
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//keyとバージョンとメンバーKeyでデータを取得
		model = todoDao.get(key, version, memberKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		
		TodoStatus status = TodoStatus.fromCode(form.todoStatus);
		if(status == null) {
			status = TodoStatus.nothing;
		}
		model.setStatus(status);
		todoDao.put(model);
	}
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#put(jp.co.nemuzuka.form.TodoForm, java.lang.String)
	 */
	@Override
	public void put(TodoForm form, String mail) {
		
		TodoModel model = null;
		Key memberKey = memberService.getKey(mail);
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
		
		//登録されたTODOタグをマスタ登録
		putTodoTag(form.tag, mail);
	}

	/**
	 * TODOタグ登録.
	 * 引数の情報を元に、TODOタグを登録します。
	 * @param tag 入力タグ値(カンマ区切りで複数登録)
	 * @param mail メールアドレス
	 */
	private void putTodoTag(String tag, String mail) {
		
		if(StringUtils.isEmpty(tag)) {
			return;
		}
		
		String[] tags = tag.split(",");
		Set<String> tagSet = new LinkedHashSet<String>();
		for(String target : tags) {
			String tagName = StringUtils.trimToEmpty(target);
			if(StringUtils.isEmpty(tagName)) {
				continue;
			}
			tagSet.add(tagName);
		}
		
		if(tagSet.size() == 0) {
			return;
		}
		todoTagService.put(tagSet.toArray(new String[0]), mail);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#delete(jp.co.nemuzuka.form.TodoForm, java.lang.String)
	 */
	@Override
	public void delete(TodoForm form, String mail) {
		Key key = Datastore.stringToKey(form.keyToString);
		Key memberKey = memberService.getKey(mail);
		Long version = ConvertUtils.toLong(form.versionNo);
		//keyとバージョンとメンバーKeyでデータを取得
		TodoModel model = todoDao.get(key, version, memberKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		todoDao.delete(key);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#putComment(jp.co.nemuzuka.form.TodoCommentForm, java.lang.String)
	 */
	@Override
	public void putComment(TodoCommentForm form, String email) {
		//ステータスが変更されていないかチェックする
		Key todoModelKey = Datastore.stringToKey(form.keyToString);
		Key memberKey = memberService.getKey(email);
		
		//KeyとメンバーKeyでデータを取得
		TodoModel model = todoDao.getWithMemberKey(todoModelKey, memberKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		
		//ステータスが変更されているかチェックする
		TodoStatus status = TodoStatus.fromCode(form.status);
		if(status == null) {
			status = TodoStatus.nothing;
		}
		if(model.getStatus().equals(status) == false) {
			Long versonNo = ConvertUtils.toLong(form.versionNo);
			//変更されているので、更新
			if(model.getVersion().equals(versonNo) == false) {
				//他のユーザに更新されている可能性があるので、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
			model.setStatus(status);
			todoDao.put(model);
		}

		//コメント登録
		commentService.put(todoModelKey, form.comment, email);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoService#deleteComment(java.lang.String, java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	public void deleteComment(String keyString, String commentKeyString,
			Long commentVersionNo, String email) {

		//TODOの存在チェック
		Key todoModelKey = Datastore.stringToKey(keyString);
		Key memberKey = memberService.getKey(email);
		
		//KeyとメンバーKeyでデータを取得
		TodoModel model = todoDao.getWithMemberKey(todoModelKey, memberKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		
		//コメント削除
		commentService.delete(todoModelKey, commentKeyString, commentVersionNo);
	}

	/**
	 * 期限ステータス判断.
	 * @param today システム日付
	 * @param targetDate 期限
	 * @param todoStatus TODOステータス
	 * @return 期限ステータス
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
		form.todoStatus = model.getStatus().getCode();
		form.title = model.getTitle();
		form.tag = StringUtils.defaultString(model.getTag());
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
		
		TodoStatus status = TodoStatus.fromCode(form.todoStatus);
		if(status == null) {
			status = TodoStatus.nothing;
		}
		model.setStatus(status);
		model.setTitle(form.title);
		model.setTag(form.tag);
		model.setContent(new Text(StringUtils.defaultString(form.content)));
		model.setPeriod(ConvertUtils.toDate(form.period, sdf));
		model.setVersion(ConvertUtils.toLong(form.versionNo));
	}
}
