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

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.dao.TodoTagDao;
import jp.co.nemuzuka.model.TodoTagModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.TodoTagService;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * TodoTagServiceの実装クラス.
 * @author k-katagiri
 */
public class TodoTagServiceImpl implements TodoTagService {

	TodoTagDao todoTagDao = TodoTagDao.getInstance();
	MemberService memberService = MemberServiceImpl.getInstance();
	
	private static TodoTagServiceImpl impl = new TodoTagServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TodoTagServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private TodoTagServiceImpl(){}

	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoTagService#getList(java.lang.String)
	 */
	@Override
	public List<TodoTagModel> getList(String mail) {
		return getList(memberService.getKey(mail));
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoTagService#put(java.lang.String[], java.lang.String)
	 */
	@Override
	public void put(String[] tagNames, String mail) {
		
		if(tagNames == null || tagNames.length == 0) {
			return;
		}

		Key memberKey = memberService.getKey(mail);

		//登録されているタグ情報を取得
		List<TodoTagModel> tagList = getList(memberKey);
		Set<String> tagNameSet = new HashSet<String>();
		for(TodoTagModel target : tagList) {
			tagNameSet.add(target.getTagName());
		}
		
		//未登録のタグが存在する場合、登録
		for(String target : tagNames) {
			if(tagNameSet.contains(target)) {
				continue;
			}
			TodoTagModel model = new TodoTagModel();
			model.setMemberKey(memberKey);
			model.setTagName(target);
			todoTagDao.put(model);
		}
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TodoTagService#delete(java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	public void delete(String keyString, Long version, String mail) {
		Key key = Datastore.stringToKey(keyString);
		Key memberKey = memberService.getKey(mail);
		
		//詳細情報取得
		TodoTagModel model = todoTagDao.get(key, version, memberKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		todoTagDao.delete(key);
	}
	
	/**
	 * 一覧取得.
	 * @param memberKey MemberKey
	 * @return 該当一覧
	 */
	private List<TodoTagModel> getList(Key memberKey) {
		return todoTagDao.getList(memberKey);
	}
	
}
