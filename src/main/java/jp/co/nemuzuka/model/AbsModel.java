package jp.co.nemuzuka.model;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * Modelの基底クラス.
 * @author kazumune
 */
public abstract class AbsModel {

	/**
	 * Key情報の取得.
	 * @return Key
	 */
	public abstract Key getKey();
	
	/**
	 * Keyの文字列情報取得.
	 * @return 文字列化したKey
	 */
	public String getKeyToString() {
		return Datastore.keyToString(getKey());
	}
}
