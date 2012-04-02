package jp.co.nemuzuka.core.entity;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.datastore.TransactionOptions;

/**
 * グローバルトランザクションとロールバック有無を保持するClass.
 * @author kazumune
 */
public class TransactionEntity {
	/** グローバルトランザクション. */
	private Transaction transaction;
	
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
		transaction.commit();
	}
	
	/**
	 * ロールバック.
	 */
	public void rollback() {
		transaction.rollback();
	}
	
	/**
	 * トランザクションbegin.
	 * HRD対応のトランザクションをbigin状態にします。
	 */
	public void begin() {
		transaction = DatastoreServiceFactory.getDatastoreService().beginTransaction(TransactionOptions.Builder.withXG(true));;
	}
	
	/**
	 * @return transaction
	 */
	public Transaction getTransaction() {
		return transaction;
	}
	/**
	 * @param transaction セットする transaction
	 */
	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}
}
