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
package jp.co.nemuzuka.core.entity;

import org.slim3.datastore.Datastore;

/**
 * グローバルトランザクションとロールバック有無を保持するClass.
 * @author kazumune
 */
public class TransactionEntity {
	/** グローバルトランザクション. */
	private org.slim3.datastore.GlobalTransaction transaction;
	
	/**
	 * コンストラクタ.
	 */
	public TransactionEntity() {
		begin();
	}
	
	/**
	 * コミット.
	 */
	public void commit() {
		if(transaction.isActive()) {
			transaction.commit();
		}
	}
	
	/**
	 * ロールバック.
	 */
	public void rollback() {
		if(transaction.isActive()) {
			transaction.rollback();
		}
	}
	
	/**
	 * トランザクションbegin.
	 * HRD対応のトランザクションをbigin状態にします。
	 * slim3のグローバルトランザクションの仕組みは@Deprecatedですが、
	 * クロスグループトランザクションの仕組みでは5個以上の更新は不可です。
	 * 今回のシステムは5個以上のEntityを更新する可能性があるので、
	 * slim3のグローバルトランザクションの仕組みを使用します。
	 */
	@SuppressWarnings("deprecation")
	public void begin() {
//		transaction = DatastoreServiceFactory.getDatastoreService().beginTransaction(TransactionOptions.Builder.withXG(true));;
		transaction = Datastore.beginGlobalTransaction();
	}
	
	/**
	 * @return transaction
	 */
	public org.slim3.datastore.GlobalTransaction getTransaction() {
		return transaction;
	}
	/**
	 * @param transaction セットする transaction
	 */
	public void setTransaction(org.slim3.datastore.GlobalTransaction transaction) {
		this.transaction = transaction;
	}
}
