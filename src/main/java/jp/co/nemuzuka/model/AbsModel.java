package jp.co.nemuzuka.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * Modelの基底クラス.
 * @author kazumune
 */
public abstract class AbsModel {

	//共通的に使用する
	/** バージョンNo. */
	@Attribute(version = true)
	private Long version;

	/**
	 * Key情報の取得.
	 * @return Key
	 */
	public abstract Key getKey();

	/**
	 * Key情報の設定.
	 * @param key 設定対象
	 */
	public abstract void setKey(Key key);
	
	/**
	 * Keyの文字列情報取得.
	 * @return 文字列化したKey
	 */
	public String getKeyToString() {
		return Datastore.keyToString(getKey());
	}

	/**
	 * @return version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version セットする version
	 */
	public void setVersion(Long version) {
		this.version = version;
	}
}
