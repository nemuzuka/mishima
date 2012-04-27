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
import java.util.ArrayList;
import java.util.List;

import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.core.entity.LabelValueBean;

/**
 * TODO登録・更新Form
 * @author k-katagiri
 */
public class TodoForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** 文字列化Key. */
	public String keyToString;
	
	/** ステータス. */
	public String todoStatus;

	/** 件名. */
	public String title;
	
	/** 内容. */
	public String content;
	
	/** 期限. */
	public String period;
	
	/** バージョンNo. */
	public String versionNo;

	//検索条件構成情報
	/** ステータス構成情報. */
	public List<LabelValueBean> getStatusList() {
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		TodoStatus[] statusList = TodoStatus.values();
		for(TodoStatus target : statusList) {
			list.add(new LabelValueBean(target.getLabel(), target.getCode()));
		}
		return list;
	}

	/**
	 * @return the keyToString
	 */
	public String getKeyToString() {
		return keyToString;
	}

	/**
	 * @param keyToString the keyToString to set
	 */
	public void setKeyToString(String keyToString) {
		this.keyToString = keyToString;
	}


	/**
	 * @return the todoStatus
	 */
	public String getTodoStatus() {
		return todoStatus;
	}

	/**
	 * @param todoStatus the todoStatus to set
	 */
	public void setTodoStatus(String todoStatus) {
		this.todoStatus = todoStatus;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the period
	 */
	public String getPeriod() {
		return period;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(String period) {
		this.period = period;
	}

	/**
	 * @return the versionNo
	 */
	public String getVersionNo() {
		return versionNo;
	}

	/**
	 * @param versionNo the versionNo to set
	 */
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
}
