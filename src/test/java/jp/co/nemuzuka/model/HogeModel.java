package jp.co.nemuzuka.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * データストアテスト用のModel.
 * @author kazumune
 */
@Model
public class HogeModel {
	@Attribute(primaryKey=true)
	private Key key;
	
	private String prop1;

	/**
	 * Key文字列取得.
	 * @return key文字列化
	 */
	public String getKeyToString() {
		return Datastore.keyToString(key);
	}
	
	/**
	 * @return key
	 */
	public Key getKey() {
		return key;
	}

	/**
	 * @param key セットする key
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return prop1
	 */
	public String getProp1() {
		return prop1;
	}

	/**
	 * @param prop1 セットする prop1
	 */
	public void setProp1(String prop1) {
		this.prop1 = prop1;
	}

}
