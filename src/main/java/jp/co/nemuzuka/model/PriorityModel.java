package jp.co.nemuzuka.model;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * 優先度を管理するModel.
 * プロジェクトにつき1レコード作成されます
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class PriorityModel extends AbsModel {

	/** プライオリティKey. */
	//projectKeyを元に作成
	@Attribute(primaryKey=true)
	private Key key;

	/** 優先度名. */
	@Attribute(unindexed=true)
	private Text priorityName;

	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}

	/**
	 * @return priorityName
	 */
	@JSONHint(ignore=true)
	public Text getPriorityName() {
		return priorityName;
	}

	/**
	 * @param priorityName セットする priorityName
	 */
	public void setPriorityName(Text priorityName) {
		this.priorityName = priorityName;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}
}
