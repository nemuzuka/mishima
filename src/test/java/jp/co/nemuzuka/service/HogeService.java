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
package jp.co.nemuzuka.service;

import java.util.List;

import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.meta.HogeModelMeta;
import jp.co.nemuzuka.model.HogeModel;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * データストアテスト用のService.
 * @author kazumune
 */
public class HogeService {

	private HogeService() {
	}

	public static HogeModel newAndPut(String keyStr, String prop1) {
		HogeModel model = new HogeModel();
		model.setProp1(prop1);
		Key key = Datastore.createKey(HogeModelMeta.get(), keyStr);
		model.setKey(key);
		GlobalTransaction.transaction.get().getTransaction().put(model);
		return model;
	}

	public static List<HogeModel> queryAll() {
		return Datastore.query(HogeModelMeta.get()).asList();
	}
}
