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
import java.util.List;

import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.dao.PriorityDao;
import jp.co.nemuzuka.form.PriorityForm;
import jp.co.nemuzuka.model.PriorityModel;
import jp.co.nemuzuka.service.PriorityService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.LabelValueBeanUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * PriorityServiceの実装クラス.
 * @author kazumune
 */
public class PriorityServiceImpl implements PriorityService {

	PriorityDao priorityDao = PriorityDao.getInstance();

	private static PriorityServiceImpl impl = new PriorityServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static PriorityServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private PriorityServiceImpl(){}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.PriorityService#get(java.lang.String)
	 */
	@Override
	public PriorityForm get(String projectKeyString) {
		PriorityForm form = new PriorityForm();
		if(StringUtils.isNotEmpty(projectKeyString)) {
			PriorityModel model = priorityDao.get4ProjectKey(projectKeyString);
			setForm(form, model);
		}
		return form;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.PriorityService#put(jp.co.nemuzuka.form.PriorityForm, java.lang.String)
	 */
	@Override
	public void put(PriorityForm form, String projectKeyString) {
		PriorityModel model = null;
		Long version = ConvertUtils.toLong(form.versionNo);

		if(version != null) {
			//更新の場合
			//versionとKeyで情報を取得
			Key key = Datastore.stringToKey(form.keyToString);
			model = priorityDao.get(key, version);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合
			model = new PriorityModel();
			//プロジェクトKeyを元にKeyを生成
			Key key = Datastore.createKey(PriorityModel.class, projectKeyString);
			model.setKey(key);
		}
		setModel(model, form);
		priorityDao.put(model);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.PriorityService#getList(java.lang.String)
	 */
	@Override
	public List<LabelValueBean> getList(String projectKeyString) {
		PriorityForm form = get(projectKeyString);
		return LabelValueBeanUtils.createList(form.priorityName, true);
	}
	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定元Model
	 */
	private void setForm(PriorityForm form, PriorityModel model) {
		if(model == null) {
			return;
		}
		form.keyToString = model.getKeyToString();
		form.priorityName = model.getPriorityName().getValue();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定元Form
	 */
	private void setModel(PriorityModel model, PriorityForm form) {
		model.setPriorityName(new Text(form.priorityName));
	}

}
