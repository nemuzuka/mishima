package jp.co.nemuzuka.model;

import java.util.Date;

import jp.co.nemuzuka.common.Authority;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.CreationUser;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModificationDate;
import org.slim3.datastore.ModificationUser;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

/**
 * アプリケーションに登録されているユーザを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class MemberModel {

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
	
	//共通的に使用する
	/** 登録日時. */
	@Attribute(listener = CreationDate.class)
	Date createdAt;
	
	/** 登録ユーザ. */
	@Attribute(listener = CreationUser.class)
	User createUser;
	
	/** 更新日時. */
	@Attribute(listener = ModificationDate.class)
	Date updatedAt;
	
	/** 更新ユーザ. */
	@Attribute(listener = ModificationUser.class)
	User updateUser;

	/** バージョンNo. */
	@Attribute(version = true)
	Long version;

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

	/**
	 * @return createdAt
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt セットする createdAt
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return createUser
	 */
	public User getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser セットする createUser
	 */
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return updatedAt
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt セットする updatedAt
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return updateUser
	 */
	public User getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser セットする updateUser
	 */
	public void setUpdateUser(User updateUser) {
		this.updateUser = updateUser;
	}
}
