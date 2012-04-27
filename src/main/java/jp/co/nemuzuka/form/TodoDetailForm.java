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
import java.util.List;

import jp.co.nemuzuka.entity.CommentModelEx;
import jp.co.nemuzuka.utils.HtmlStringUtils;

/**
 * TODO詳細Form
 * @author k-katagiri
 */
public class TodoDetailForm implements Serializable {

	/**
	 * serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;

	/** TodoForm. */
	public TodoForm form;

	/** コメント一覧. */
	public List<CommentModelEx> commentList;
	
	/**
	 * 表示用内容取得.
	 * @return 表示用内容
	 */
	public String getContentView() {
		return HtmlStringUtils.escapeTextAreaString(form.content);
	}
	
	/**
	 * @return the form
	 */
	public TodoForm getForm() {
		return form;
	}

	/**
	 * @param form the form to set
	 */
	public void setForm(TodoForm form) {
		this.form = form;
	}

	/**
	 * @return the commentList
	 */
	public List<CommentModelEx> getCommentList() {
		return commentList;
	}

	/**
	 * @param commentList the commentList to set
	 */
	public void setCommentList(List<CommentModelEx> commentList) {
		this.commentList = commentList;
	}
}
