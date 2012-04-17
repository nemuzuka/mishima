package jp.co.nemuzuka.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.meta.TodoModelMeta;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoModel;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.InMemoryFilterCriterion;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;

/**
 * TodoModelに対するDao.
 * @author kazumune
 */
public class TodoDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return TodoModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return TodoModel.class;
	}

	/**
	 * List取得.
	 * @param param 検索条件
	 * @return 該当レコード
	 */
	public List<TodoModel> getList(Param param) {
		TodoModelMeta e = (TodoModelMeta) getModelMeta();
		
		Set<InMemoryFilterCriterion> filterSet = new HashSet<InMemoryFilterCriterion>();
		//ステータスの検索条件
		String[] status = param.status;
		if(status != null && status.length != 0) {
			Set<TodoStatus> statusSet = new HashSet<TodoStatus>();
			for(String statusCode : status) {
				if(TodoStatus.NO_FINISH.equals(statusCode)) {
					//未完了が選択された際には、未対応と対応中を検索条件に設定
					statusSet.add(TodoStatus.nothing);
					statusSet.add(TodoStatus.doing);
				} else {
					TodoStatus statusType = TodoStatus.fromCode(statusCode);
					if(statusType != null) {
						statusSet.add(statusType);
					}
				}
			}
			if(statusSet.size() != 0) {
				filterSet.add(e.status.in(statusSet));
			}
		}
		
		//件名の検索条件
		if(StringUtils.isNotEmpty(param.title)) {
			filterSet.add(e.title.startsWith(param.title));
		}
		
		//期限Fromの検索条件
		if(param.fromPeriod != null) {
			filterSet.add(e.period.isNotNull());
			filterSet.add(e.period.greaterThanOrEqual(param.fromPeriod));
		}
		
		//期限Toの検索条件
		if(param.toPeriod != null) {
			filterSet.add(e.period.isNotNull());
			filterSet.add(e.period.lessThanOrEqual(param.toPeriod));
		}
		
		//参照可能ユーザの検索条件
		Key createMemberKey = Datastore.createKey(MemberModel.class, param.email);
		filterSet.add(e.createMemberKey.equal(createMemberKey));
		
		ModelQuery<TodoModel> query = Datastore.query(e).filterInMemory(filterSet.toArray(new InMemoryFilterCriterion[0]))
				.sortInMemory(e.key.asc);
		if(param.limit != null) {
			//一覧取得数の指定がされている場合、設定
			query = query.limit(param.limit);
		}
		return query.asList();
	}
	
	/**
	 * Model取得.
	 * @param key TodoModelのKey
	 * @param memberKey MemberModelのKey
	 * @return 存在すればModelインスタンス
	 */
	public TodoModel getWithMemberKey(Key key, Key memberKey) {
		
		TodoModel model = get(key);
		if(model != null) {
			if(model.getCreateMemberKey().equals(memberKey) == false) {
				model = null;
			}
		}
		return model;
	}
	
	/**
	 * Model取得.
	 * @param key TodoModelのKey
	 * @param version TodoModelのバージョン
	 * @param memberKey MemberModelのKey
	 * @return 存在すればModelインスタンス
	 */
	public TodoModel get(Key key, Long version, Key memberKey) {
		
		TodoModel model = get(key, version);
		if(model != null) {
			if(model.getCreateMemberKey().equals(memberKey) == false) {
				model = null;
			}
		}
		return model;
	}

	/**
	 * 検索条件パラメータ.
	 * @author k-katagiri
	 */
	public static class Param {
		/** ステータス. */
		public String[] status;
		/** 件名. */
		public String title;
		/** 期限From. */
		public Date fromPeriod;
		/** 期限To. */
		public Date toPeriod;

		/** 取得上限件数. */
		public Integer limit;
		/** メールアドレス. */
		public String email;
	}

}
