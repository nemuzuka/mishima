package jp.co.nemuzuka.model;

import jp.co.nemuzuka.common.ProjectAuthority;
import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.Key;

/**
 * プロジェクトメンバーを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class ProjectMemberModel extends AbsModel {

	/** プロジェクトメンバーKey. */
	//プロジェクトKeyとプロジェクトメンバーKeyの複合Key
	@Attribute(primaryKey=true)
	private Key key;

	/** プロジェクトKey. */
	private Key projectKey;

	/** メンバーKey. */
	private Key memberKey;
	
	/** プロジェクトメンバー権限. */
	private ProjectAuthority projectAuthority;
	
	/**
	 * Key生成.
	 * 引数の情報を元にKey情報を作成します。
	 * @param projectKey プロジェクトKey
	 * @param memberKey メンバーKey
	 * @return 生成Key
	 */
	public Key createKey(Key projectKey, Key memberKey) {
		this.projectKey = projectKey;
		this.memberKey = memberKey;
		String projectKeyString = Datastore.keyToString(projectKey);
		String memberKeyString = Datastore.keyToString(memberKey);
		
		String keyString = projectKeyString + ":" + memberKeyString;
		key = Datastore.createKey(ProjectMemberModel.class, keyString);
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
	 * @return projectKey
	 */
	public Key getProjectKey() {
		return projectKey;
	}

	/**
	 * @param projectKey セットする projectKey
	 */
	public void setProjectKey(Key projectKey) {
		this.projectKey = projectKey;
	}

	/**
	 * @return memberKey
	 */
	public Key getMemberKey() {
		return memberKey;
	}

	/**
	 * @param memberKey セットする memberKey
	 */
	public void setMemberKey(Key memberKey) {
		this.memberKey = memberKey;
	}

	/**
	 * @return projectAuthority
	 */
	public ProjectAuthority getProjectAuthority() {
		return projectAuthority;
	}

	/**
	 * @param projectAuthority セットする projectAuthority
	 */
	public void setProjectAuthority(ProjectAuthority projectAuthority) {
		this.projectAuthority = projectAuthority;
	}

	/**
	 * @param key セットする key
	 */
	public void setKey(Key key) {
		this.key = key;
	}
}
