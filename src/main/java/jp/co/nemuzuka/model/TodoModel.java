package jp.co.nemuzuka.model;

import java.util.Date;

import jp.co.nemuzuka.common.TodoStatus;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * TODOを管理するModel.
 * @author k-katagiri
 */
@Model(schemaVersion = 1)
public class TodoModel extends AbsModel {

	/** TODOKey. */
	//自動採番
	@Attribute(primaryKey=true)
	private Key key;

	/** ステータス. */
	private TodoStatus status;

	/** 作成メンバーKey. */
	private Key createMemberKey;
	
	/** 件名. */
	private String title;
	
	/** 内容. */
	@Attribute(unindexed=true)
	private Text content;
	
	/** 期限. */
	private Date period;
	
	/** 登録日時. */
	@Attribute(listener = CreationDate.class, unindexed=true)
	private Date createdAt;

	/**
	 * @return the key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}

	/**
	 * @return the status
	 */
	public TodoStatus getStatus() {
		return status;
	}
	/**
	 * @return the createMemberKey
	 */
	@JSONHint(ignore=true)
	public Key getCreateMemberKey() {
		return createMemberKey;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the content
	 */
	@JSONHint(ignore=true)
	public Text getContent() {
		return content;
	}
	/**
	 * @return the period
	 */
	@JSONHint(ignore=true)
	public Date getPeriod() {
		return period;
	}
	/**
	 * @return the createdAt
	 */
	@JSONHint(ignore=true)
	public Date getCreatedAt() {
		return createdAt;
	}
	
	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TodoStatus status) {
		this.status = status;
	}


	/**
	 * @param createMemberKey the createMemberKey to set
	 */
	public void setCreateMemberKey(Key createMemberKey) {
		this.createMemberKey = createMemberKey;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(Text content) {
		this.content = content;
	}

	/**
	 * @param period the period to set
	 */
	public void setPeriod(Date period) {
		this.period = period;
	}

	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
