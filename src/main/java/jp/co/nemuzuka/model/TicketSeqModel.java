package jp.co.nemuzuka.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * Ticketの採番Model.
 * @author k-katagiri
 */
@Model(schemaVersion = 1)
public class TicketSeqModel {

	/** Key. */
	@Attribute(primaryKey=true)
	private Key key;

	/** シーケンス値. */
	private Long no;
	
	/**
	 * @return the key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return the no
	 */
	public Long getNo() {
		return no;
	}

	/**
	 * @param no the no to set
	 */
	public void setNo(Long no) {
		this.no = no;
	}
}
