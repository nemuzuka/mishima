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
package jp.co.nemuzuka.dao;

import jp.co.nemuzuka.common.UniqueKey;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.meta.TicketSeqModelMeta;
import jp.co.nemuzuka.model.TicketSeqModel;
import jp.co.nemuzuka.utils.ConvertUtils;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * TicketSeqModelに対するDao.
 * @author kazumune
 */
public class TicketSeqDao {

	private static TicketSeqDao ticketSeqDao = new TicketSeqDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TicketSeqDao getInstance() {
		return ticketSeqDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private TicketSeqDao() {}

	
	/**
	 * 最大値を示すTicketSeqModelを作成します。
	 * @return 最大値を示すTicketSeqModel
	 */
	public TicketSeqModel createMaxTicketSeqModel() {
		
		Long no = getMaxValue();
		if(no == null) {
			no = 1L;
		} else {
			no = no + 1L;
		}
		
		//存在しないnoが取得されるまで繰り返す
		while(true) {
			if (Datastore.putUniqueValue(UniqueKey.ticketSeq.name(), ConvertUtils.toString(no))) {
				break;
			}
			no++;
		}
		
		TicketSeqModel model = new TicketSeqModel();
		model.setNo(no);
		
		Key key = GlobalTransaction.transaction.get().getTransaction().put(model);
		model.setKey(key);
		return model;
	}
	
	/**
	 * 最大値取得.
	 * 最大値を取得します。
	 * @return 最大値。存在しなければnull
	 */
	private Long getMaxValue() {
		TicketSeqModelMeta e = TicketSeqModelMeta.get();;
		return Datastore.query(e).max(e.no);
	}
}
