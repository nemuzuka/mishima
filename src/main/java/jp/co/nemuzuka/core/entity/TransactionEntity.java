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
