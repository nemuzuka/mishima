package jp.co.nemuzuka.model;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * 種別を管理するModel.
 * プロジェクトにつき1レコード作成されます
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class KindModel extends AbsModel {

	/** 種別Key. */
	//projectKeyを元に作成
	@Attribute(primaryKey=true)
	private Key key;

	/** 種別名. */
	@Attribute(unindexed=true)
	private Text kindName;

	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}
	/**
	 * @return the kindName
	 */
	@JSONHint(ignore=true)
	public Text getKindName() {
		return kindName;
	}
	/**
	 * @param kindName the kindName to set
	 */
	public void setKindName(Text kindName) {
		this.kindName = kindName;
	}
	/**
	 * @param key セットする key
	 */
	public void setKey(Key key) {
		this.key = key;
	}
}
