package jp.co.nemuzuka.model;

import java.util.Date;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.CreationUser;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModificationDate;
import org.slim3.datastore.ModificationUser;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

/**
 * Modelの基底クラス.
 * @author kazumune
 */
public abstract class AbsModel {

	//共通的に使用する
	/** 登録日時. */
	@Attribute(listener = CreationDate.class, unindexed=true)
	Date createdAt;
	
	/** 登録ユーザ. */
	@Attribute(listener = CreationUser.class, unindexed=true)
	User createUser;
	
	/** 更新日時. */
	@Attribute(listener = ModificationDate.class, unindexed=true)
	Date updatedAt;
	
	/** 更新ユーザ. */
	@Attribute(listener = ModificationUser.class, unindexed=true)
	User updateUser;

	/** バージョンNo. */
	@Attribute(version = true)
	Long version;

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
	 * 不要データ削除.
	 * 登録/更新、日時、対象者をnullに置き換えます。
	 * 一覧を表示する際にトラフィックを減らすために使用します。
	 */
	public void clearData() {
		createdAt = null;
		createUser = null;
		updatedAt = null;
		updateUser = null;
	}
	
	/**
	 * Keyの文字列情報取得.
	 * @return 文字列化したKey
	 */
	public String getKeyToString() {
		return Datastore.keyToString(getKey());
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
