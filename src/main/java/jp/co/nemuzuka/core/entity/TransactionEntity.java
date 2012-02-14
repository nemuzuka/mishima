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
	/** ロールバック有無. */
	private boolean rollback;
	
	/**
	 * コンストラクタ.
	 */
	public TransactionEntity() {
		transaction = Datastore.beginTransaction();
		rollback = false;
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
		rollback = true;
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
	/**
	 * @return rollback
	 */
	public boolean isRollback() {
		return rollback;
	}
	/**
	 * @param rollback セットする rollback
	 */
	public void setRollback(boolean rollback) {
		this.rollback = rollback;
	}
}
