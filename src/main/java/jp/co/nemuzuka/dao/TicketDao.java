package jp.co.nemuzuka.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.meta.TicketModelMeta;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TicketModel;
import jp.co.nemuzuka.utils.CurrentDateUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.datastore.FilterCriterion;
import org.slim3.datastore.InMemoryFilterCriterion;
import org.slim3.datastore.InMemorySortCriterion;
import org.slim3.datastore.ModelMeta;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;

/**
 * TicketModelに対するDao.
 * @author kazumune
 */
public class TicketDao extends AbsDao {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelMeta()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	ModelMeta getModelMeta() {
		return TicketModelMeta.get();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.dao.AbsDao#getModelClass()
	 */
	@SuppressWarnings("rawtypes")
	@Override
	Class getModelClass() {
		return TicketModel.class;
	}

	TicketSeqDao ticketSeqDao = TicketSeqDao.getInstance();

	private static TicketDao ticketDao = new TicketDao();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TicketDao getInstance() {
		return ticketDao;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private TicketDao() {}

	/**
	 * put処理.
	 * 新規登録の場合、Noを設定します。
	 * @param model 対象Model
	 */
	public void put(TicketModel model){
		if(model.getKey() == null) {
			//新規の場合
			model.setNo(ticketSeqDao.createMaxTicketSeqModel().getNo());
		}
		super.put(model);
	}
	
	/**
	 * List取得.
	 * @param param 検索条件
	 * @return 該当レコード
	 */
	public List<TicketModel> getList(Param param) {
		TicketModelMeta e = (TicketModelMeta) getModelMeta();
		
		Set<InMemoryFilterCriterion> filterSet = new HashSet<InMemoryFilterCriterion>();
		//ステータスの検索条件
		String[] status = param.status;
		if(status != null && status.length != 0) {
			Set<String> statusSet = new HashSet<String>();
			for(String statusValue : status) {
				if(TicketMst.NO_FINISH.equals(statusValue)) {
					//未完了が選択された際には、未完了を意味するステータスを設定
					for(String target : param.openStatus) {
						statusSet.add(target);
					}
				} else {
					statusSet.add(statusValue);
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
		
		//優先度の検索条件
		if(StringUtils.isNotEmpty(param.priority)) {
			filterSet.add(e.priority.equal(param.priority));
		}
		//種別の検索条件
		if(StringUtils.isNotEmpty(param.kind)) {
			filterSet.add(e.targetKind.equal(param.kind));
		}
		//カテゴリの検索条件
		if(StringUtils.isNotEmpty(param.category)) {
			filterSet.add(e.category.equal(param.category));
		}
		//バージョンの検索条件
		if(StringUtils.isNotEmpty(param.version)) {
			filterSet.add(e.targetVersion.equal(param.version));
		}
		//マイルストーンの検索条件
		if(StringUtils.isNotEmpty(param.milestone)) {
			Key milestoneKey = Datastore.stringToKey(param.milestone);
			filterSet.add(e.milestone.equal(milestoneKey));
		}
		//担当者の検索条件
		if(StringUtils.isNotEmpty(param.targetMember)) {
			Key targetMemberKey = Datastore.stringToKey(param.targetMember);
			filterSet.add(e.targetMemberKey.equal(targetMemberKey));
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
		sortSet.add(e.no.asc);
		
		//プロジェクトの検索条件
		Key projectKey = Datastore.stringToKey(param.projectKeyString);
		filterSet.add(e.projectKey.equal(projectKey));
		
		ModelQuery<TicketModel> query = Datastore.query(e).filterInMemory(filterSet.toArray(new InMemoryFilterCriterion[0]))
				.sortInMemory(sortSet.toArray(new InMemorySortCriterion[0]));
		
		List<TicketModel> retList =  query.asList();
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
	 * 指定したプロジェクトにおいて、
	 * ステータスが未完了でかつ、担当者が自分のチケットを指定したlimit件数分取得します。
	 * 一覧は、
	 * ・期限が設定されているもの(期限の昇順)
	 * ・期限が未設定のもの(登録順)
	 * の順番でソートされます。
	 * @param limit 取得件数
	 * @param mail ログインユーザのメールアドレス
	 * @param projectKeyString プロジェクトKey文字列
	 * @param openStatus 未完了を意味するステータス配列
	 * @return 該当レコード
	 */
	public List<TicketModel> getDashbordList(int limit, String mail, String projectKeyString,
			String[] openStatus) {
		
		//ステータスが未完了で、指定したLimit分取得する
		
		//期限が設定されていて、未完了のものを取得
		Param param = new Param();
		param.status = new String[]{TicketMst.NO_FINISH};
		param.openStatus = openStatus;
		param.limit = limit;
		param.targetMember = Datastore.keyToString(
				Datastore.createKey(MemberModel.class, mail));
		param.projectKeyString = projectKeyString;
		param.toPeriod = CurrentDateUtils.getInstance().getMaxDate();
		param.orderByPeriod = true;
		
		List<TicketModel> list = getList(param);
		if(list.size() >= limit) {
			//指定件数分取得できた場合、終了
			return list;
		}
		
		//期限が設定されておらず、未完了のものを取得
		param.toPeriod = null;
		param.periodNull = true;
		param.orderByPeriod = false;
		List<TicketModel> periodNullList = getList(param);
		for(TicketModel target : periodNullList) {
			list.add(target);
			if(list.size() >= limit) {
				//追加後、上限件数を超えた場合、終了
				break;
			}
		}
		return list;
	}

	/**
	 * 子Ticket情報取得.
	 * 自分の子どもとして登録されているTicketのKeyListを取得します。
	 * @param self 自Key
	 * @param projectKey プロジェクトKey
	 * @return 子どもとして登録されているTicketのKeyList
	 */
	public List<Key> getChildList(Key self, Key projectKey) {
		TicketModelMeta e = (TicketModelMeta) getModelMeta();
		
		return Datastore.query(e).filter(e.parentTicketKey.equal(self), e.projectKey.equal(projectKey))
				.sortInMemory(e.key.asc).asKeyList();
	}

	/**
	 * Map取得.
	 * 指定したKey配列に合致するデータを取得します。
	 * @param projectKeyString プロジェクトKey文字列
	 * @param keys key配列
	 * @return 該当Map
	 */
	public Map<Key, TicketModel> getMap(String projectKeyString, Key...keys) {
		TicketModelMeta e = (TicketModelMeta) getModelMeta();
		Set<FilterCriterion> filterSet = new HashSet<FilterCriterion>();
		Map<Key, TicketModel> map = new HashMap<Key, TicketModel>();
		if(keys != null && keys.length != 0) {
			filterSet.add(e.key.in(keys));
		} else {
			return map;
		}
		
		Key projectKey = Datastore.stringToKey(projectKeyString);
		filterSet.add(e.projectKey.equal(projectKey));
		
		List<TicketModel> list = Datastore.query(e).filter(filterSet.toArray(new FilterCriterion[0])).asList();
		for(TicketModel target : list) {
			map.put(target.getKey(), target);
		}
		return map;
	}

	/**
	 * Model取得.
	 * @param key TicketModelのKey
	 * @param projectKey ProjectModelのKey
	 * @return 存在すればModelインスタンス
	 */
	public TicketModel getWithProjectKey(Key key, Key projectKey) {
		
		TicketModel model = get(key);
		if(model != null) {
			if(model.getProjectKey().equals(projectKey) == false) {
				model = null;
			}
		}
		return model;
	}
	
	/**
	 * Model取得.
	 * @param no TicketModelのNo
	 * @param projectKey ProjectModelのKey
	 * @return 存在すればModelのKey
	 */
	public Key getWithNoAndProjectKey(Long no, Key projectKey) {
		TicketModelMeta e = (TicketModelMeta) getModelMeta();
		List<Key> list = Datastore.query(e).filter(e.no.equal(no)).sortInMemory(e.key.asc).asKeyList();
		if(list.size() != 0) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * Model取得.
	 * @param key TicketModelのKey
	 * @param version TicketModelのバージョン
	 * @param projectKey ProjectModelのKey
	 * @return 存在すればModelインスタンス
	 */
	public TicketModel get(Key key, Long version, Key projectKey) {
		
		TicketModel model = get(key, version);
		if(model != null) {
			if(model.getProjectKey().equals(projectKey) == false) {
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
		
		/** 種別. */
		public String kind;

		/** カテゴリ. */
		public String category;

		/** バージョン. */
		public String version;

		/** マイルストーン. */
		public String milestone;
		
		/** 優先度. */
		public String priority;
		
		/** 担当者. */
		public String targetMember;
		
		/** 期限From. */
		public Date fromPeriod;
		/** 期限To. */
		public Date toPeriod;
		/** 期限がnullのもののみ取得する場合、true */
		boolean periodNull;

		/** 取得上限件数. */
		public Integer limit;
		
		//必須
		/** プロジェクトKey. */
		public String projectKeyString;
		/** 未完了を意味するステータス. */
		public String[] openStatus;
		
		/** 期限の昇順でソートする場合、true */
		boolean orderByPeriod;
	}
}
