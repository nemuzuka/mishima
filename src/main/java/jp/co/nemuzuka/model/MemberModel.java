package jp.co.nemuzuka.model;

import jp.co.nemuzuka.common.Authority;
import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * アプリケーションに登録されているユーザを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class MemberModel extends AbsModel {

	/** メンバーKey. */
	//mailアドレスから生成
	@Attribute(primaryKey=true)
	private Key key;

	/** メールアドレス. */
	private String mail;
	
	/** 氏名. */
	private String name;
	
	/** 
	 * 権限. 
	 * 管理者：システムにユーザを追加できる
	 * 一般
	 */
	private Authority authority;
	
	/**
	 * Key生成.
	 * 引数のメールアドレスでKeyを生成します。
	 * メンバ変数のメールアドレスにも設定します。
	 * @param mail メールアドレス
	 * @return 生成Key
	 */
	public Key createKey(String mail) {
		this.mail = mail;
		key = Datastore.createKey(MemberModel.class, mail);
		return key;
	}

	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
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
	 * @return mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail セットする mail
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name セットする name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return authority
	 */
	public Authority getAuthority() {
		return authority;
	}

	/**
	 * @param authority セットする authority
	 */
	public void setAuthority(Authority authority) {
		this.authority = authority;
	}
}
