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
package jp.co.nemuzuka.form;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.dao.TodoDao;
import jp.co.nemuzuka.model.TodoTagModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

/**
 * TODO検索画面の入力Form.
 * @author kazumune
 */
public class TodoSearchForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** ステータス. */
	public String[] status = new String[0];
	
	/** 件名. */
	public String title;

	/** タグ. */
	public String tag;
	
	/** 期限From. */
	public String fromPeriod;
	
	/** 期限To. */
	public String toPeriod;

	//検索条件構成情報
	/** ステータス構成情報. */
	public List<LabelValueBean> getStatusList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		list.add(new LabelValueBean(TodoStatus.NO_FINISH_LABEL, TodoStatus.NO_FINISH));
		TodoStatus[] statusList = TodoStatus.values();
		for(TodoStatus target : statusList) {
			list.add(new LabelValueBean(target.getLabel(), target.getCode()));
		}
		return list;
	}
	
	/** TODOタグList. */
	private List<TodoTagModel> tagList;
	
	/**
	 * 登録済みTODOタグList取得
	 * ユーザが登録したTODOタグListを全て返却します。
	 * @return 登録済みTODOタグList
	 */
	public List<LabelValueBean> getTagList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		list.add(new LabelValueBean("", ""));
		for(TodoTagModel target : tagList) {
			list.add(new LabelValueBean(target.getTagName(), target.getTagName()));
		}
		return list;
	}
	
	/**
	 * @return the status
	 */
	public String[] getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String[] status) {
		this.status = status;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the fromPeriod
	 */
	public String getFromPeriod() {
		return fromPeriod;
	}

	/**
	 * @param fromPeriod the fromPeriod to set
	 */
	public void setFromPeriod(String fromPeriod) {
		this.fromPeriod = fromPeriod;
	}

	/**
	 * @return the toPeriod
	 */
	public String getToPeriod() {
		return toPeriod;
	}

	/**
	 * @param toPeriod the toPeriod to set
	 */
	public void setToPeriod(String toPeriod) {
		this.toPeriod = toPeriod;
	}
	
	/**
	 * @param tagList the tagList to set
	 */
	public void setTagList(List<TodoTagModel> tagList) {
		this.tagList = tagList;
	}

	/**
	 * @return the tag
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * 検索条件作成.
	 * @param email ログインユーザのemailアドレス
	 * @param memberService MemberServiceインスタンス
	 * @return 生成検索条件インスタンス
	 */
	public TodoDao.Param createParam(String email, MemberService memberService) {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		TodoDao.Param param = new TodoDao.Param();
		param.status = status;
		param.title = title;
		param.tag = tag;
		param.fromPeriod = ConvertUtils.toDate(fromPeriod, sdf);
		param.toPeriod = ConvertUtils.toDate(toPeriod, sdf);
		String memberKeyString = memberService.getKeyString(email);
		param.targetMemberKeyString = memberKeyString;
		return param;
	}
}
