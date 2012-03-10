package jp.co.nemuzuka.core.entity;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Transaction;

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
	 */
	public void begin() {
		transaction = Datastore.beginTransaction();
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
