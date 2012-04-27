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
package jp.co.nemuzuka.entity;

import jp.co.nemuzuka.model.CommentModel;
import jp.co.nemuzuka.utils.HtmlStringUtils;

/**
 * Comment表示用Entity.
 * @author k-katagiri
 */
public class CommentModelEx {

	/** Comment情報. */
	private CommentModel model;
	
	/** コメント登録者名. */
	private String createMemberName;
	
	/** 登録日時. */
	//MM/dd HH:mmフォーマット
	private String createdAt;

	/**
	 * @return 画面表示用コメント文字列取得
	 */
	public String getComment() {
		if(model.getComment() != null) {
			return HtmlStringUtils.escapeTextAreaString(model.getComment().getValue());
		}
		return "";
	}

	/**
	 * @return the model
	 */
	public CommentModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(CommentModel model) {
		this.model = model;
	}

	/**
	 * @return the createMemberName
	 */
	public String getCreateMemberName() {
		return createMemberName;
	}

	/**
	 * @param createMemberName the createMemberName to set
	 */
	public void setCreateMemberName(String createMemberName) {
		this.createMemberName = createMemberName;
	}

	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
}
