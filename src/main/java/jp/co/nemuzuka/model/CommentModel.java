package jp.co.nemuzuka.model;

import java.util.Date;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * Commentを管理するModel.
 * @author k-katagiri
 */
@Model(schemaVersion = 1)
public class CommentModel extends AbsModel {

	/** CommentKey. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** 
	 * 参照Key.
	 * TODOKey or Ticket Keyが設定されます。
	 */
	private Key refsKey;
	
	/** 作成メンバーKey. */
	@Attribute(unindexed=true)
	private Key createMemberKey;

	/** コメント */
	@Attribute(unindexed=true)
	private Text comment;
	
	/** 登録日時. */
	@Attribute(listener = CreationDate.class)
	private Date createdAt;

	/**
	 * @return the key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}

	/**
	 * @return the createMemberKey
	 */
	@JSONHint(ignore=true)
	public Key getCreateMemberKey() {
		return createMemberKey;
	}
	/**
	 * @return the createdAt
	 */
	@JSONHint(ignore=true)
	public Date getCreatedAt() {
		return createdAt;
	}
	/**
	 * @return the refsKey
	 */
	@JSONHint(ignore=true)
	public Key getRefsKey() {
		return refsKey;
	}
	/**
	 * @return the comment
	 */
	@JSONHint(ignore=true)
	public Text getComment() {
		return comment;
	}

	/**
	 * @param refsKey the refsKey to set
	 */
	public void setRefsKey(Key refsKey) {
		this.refsKey = refsKey;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(Text comment) {
		this.comment = comment;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @param createMemberKey the createMemberKey to set
	 */
	public void setCreateMemberKey(Key createMemberKey) {
		this.createMemberKey = createMemberKey;
	}

}
