package jp.co.nemuzuka.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import jp.co.nemuzuka.common.TodoStatus;
import jp.co.nemuzuka.meta.TodoModelMeta;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TodoModel;
import jp.co.nemuzuka.utils.CurrentDateUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.InMemoryFilterCriterion;
import org.slim3.datastore.InMemorySortCriterion;
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

	private static TodoDao todoDao = new TodoDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TodoDao getInstance() {
		return todoDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private TodoDao() {}

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

		//期限未設定のみを抽出
		if(param.periodNull) {
			//期限がnullであることを指定する
			filterSet.add(e.period.equal(null));
		}
		
		Set<InMemorySortCriterion> sortSet = new LinkedHashSet<InMemorySortCriterion>();
		if(param.orderByPeriod) {
			//期限の昇順でソートする
			sortSet.add(e.period.asc);
		}
		sortSet.add(e.key.asc);
		
		//参照可能ユーザの検索条件
		Key createMemberKey = Datastore.createKey(MemberModel.class, param.email);
		filterSet.add(e.createMemberKey.equal(createMemberKey));
		
		ModelQuery<TodoModel> query = Datastore.query(e).filterInMemory(filterSet.toArray(new InMemoryFilterCriterion[0]))
				.sortInMemory(sortSet.toArray(new InMemorySortCriterion[0]));
		
		List<TodoModel> retList =  query.asList();
		if(param.limit != null) {
			
			int toIndex = param.limit;
			if(toIndex > retList.size()) {
				//結果が一覧取得数より少ない場合
				toIndex = retList.size();
			}
			//一覧取得数の指定がされている場合、設定
			return retList.subList(0, toIndex);
		}
		return retList;
		
	}
	
	/**
	 * ダッシュボード用一覧取得.
	 * ステータスが未完了のTODOを指定したlimit件数分取得します。
	 * 一覧は、
	 * ・期限が設定されているもの(期限の昇順)
	 * ・期限が未設定のもの(登録順)
	 * の順番でソートされます。
	 * @param limit 取得件数
	 * @param email メールアドレス
	 * @return 該当レコード
	 */
	public List<TodoModel> getDashbordList(int limit, String email) {
		
		//ステータスが未完了で、指定したLimit分取得する
		
		//期限が設定されていて、未完了のものを取得
		Param param = new Param();
		param.limit = limit;
		param.email = email;
		param.toPeriod = CurrentDateUtils.getInstance().getMaxDate();
		param.orderByPeriod = true;
		
		List<TodoModel> list = getList(param);
		if(list.size() >= limit) {
			//指定件数分取得できた場合、終了
			return list;
		}
		
		//期限が設定されておらず、未完了のものを取得
		param.toPeriod = null;
		param.periodNull = true;
		param.orderByPeriod = false;
		List<TodoModel> periodNullList = getList(param);
		for(TodoModel target : periodNullList) {
			//期限がnullのものだけ戻りListに取り込む
			if(target.getPeriod() != null) {
				break;
			}
			list.add(target);
			if(list.size() >= limit) {
				//追加後、上限件数を超えた場合、終了
				break;
			}
		}
		return list;
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
		/** 期限がnullのもののみ取得する場合、true */
		boolean periodNull;

		/** 取得上限件数. */
		public Integer limit;
		/** メールアドレス. */
		public String email;
		
		/** 期限の昇順でソートする場合、true */
		boolean orderByPeriod;
	}
}
