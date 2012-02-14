package jp.co.nemuzuka.core.entity;

/**
 * トランザクションのThread毎の保持Class
 * @author kazumune
 */
public class GlobalTransaction {

	/** ThreadLocal. */
	public static ThreadLocal<TransactionEntity> transaction = new ThreadLocal<TransactionEntity>() {
		protected TransactionEntity initialValue() {
			return null;
		};
	};
	
}
