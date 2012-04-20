package jp.co.nemuzuka.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.entity.MilestoneModelEx;
import jp.co.nemuzuka.entity.TicketMstEntity;
import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.form.StatusForm;
import jp.co.nemuzuka.service.CategoryService;
import jp.co.nemuzuka.service.KindService;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.service.PriorityService;
import jp.co.nemuzuka.service.StatusService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.VersionService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeChecker;
import jp.co.nemuzuka.utils.LabelValueBeanUtils;

/**
 * TicketMstServiceの実装クラス.
 * @author k-katagiri
 */
public class TicketMstServiceImpl implements TicketMstService {

	CategoryService categoryService = CategoryServiceImpl.getInstance();
	KindService kindService = KindServiceImpl.getInstance();
	PriorityService priorityService = PriorityServiceImpl.getInstance();
	StatusService statusService = StatusServiceImpl.getInstance();
	VersionService versionService = VersionServiceImpl.getInstance();
	MilestoneService milestoneService = MilestoneServiceImpl.getInstance();
	
	private static TicketMstServiceImpl impl = new TicketMstServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TicketMstServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private TicketMstServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketMstService#getTicketMst(java.lang.String)
	 */
	@Override
	public TicketMst getTicketMst(String projectKeyString) {
		
		//Cash情報を取得
		Cache cache = null;
		try {
			cache = CacheManager.getInstance().getCacheFactory().createCache(Collections.emptyMap());
		} catch (CacheException e) {
			throw new RuntimeException(e);
		}
		TicketMstEntity ticketMstEntity = (TicketMstEntity)cache.get(TicketMstEntity.class.getName());
		if(ticketMstEntity == null) {
			ticketMstEntity = new TicketMstEntity();
			cache.put(TicketMstEntity.class.getName(), ticketMstEntity);
		}
		
		//Cash情報から指定した情報を元にTicketデータを取得する
		return getTicketMst(projectKeyString, ticketMstEntity);
	}

	/**
	 * TicketMst情報取得.
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @param ticketMstEntity チケット用選択肢マスタ管理オブジェクト
	 * @return 該当TicketMst
	 */
	private TicketMst getTicketMst(String projectKeyString,
			TicketMstEntity ticketMstEntity) {
		
		TicketMst ticketMst = ticketMstEntity.map.get(projectKeyString);
		if(ticketMst == null || DateTimeChecker.isOverRefreshStartTime(ticketMst.refreshStartTime)) {
			//新しく作成する
			ticketMst = createTicketMst(projectKeyString);
			ticketMstEntity.map.put(projectKeyString, ticketMst);
		}
		return ticketMst;
	}

	/**
	 * TicketMst情報作成.
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @return TicketMstインスタンス
	 */
	private TicketMst createTicketMst(String projectKeyString) {
		
		TicketMst mst = new TicketMst();
		mst.priorityList = priorityService.getList(projectKeyString);
		setStatus(projectKeyString, mst);
		mst.kindList = kindService.getList(projectKeyString);
		mst.categoryList = categoryService.getList(projectKeyString);
		mst.milestoneList = createMilestoneList(projectKeyString);
		mst.versionList = versionService.getList(projectKeyString);
		
		//現在時刻に加算分の時刻(分)を加算し、設定する
		Date date = CurrentDateUtils.getInstance().getCurrentDateTime();
		int min = ConvertUtils.toInteger(System.getProperty("jp.co.nemuzuka.ticket.mst.refresh.min", "15"));
		date = DateUtils.addMinutes(date, min);
		mst.refreshStartTime = date;

		return mst;
	}
	
	/**
	 * マイルストーン選択肢生成.
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @return マイルストーン選択肢
	 */
	private List<LabelValueBean> createMilestoneList(String projectKeyString) {
		
		List<MilestoneModelEx> list = milestoneService.getAllList(projectKeyString);
		List<LabelValueBean> milestoneList = new ArrayList<LabelValueBean>();
		for(MilestoneModelEx target : list) {
			milestoneList.add(new LabelValueBean(target.model.getMilestoneName(), target.model.getKeyToString()));
		}
		milestoneList.add(0, new LabelValueBean("", ""));
		
		return milestoneList;
	}

	/**
	 * ステータス選択肢、未完了を意味するステータス設定.
	 * @param projectKeyString キー文字列(プロジェクトKey)
	 * @param mst　TicketMstインスタンス
	 */
	private void setStatus(String projectKeyString, TicketMst mst) {
		
		//ステータス選択肢の作成
		StatusForm form = statusService.get(projectKeyString);
		String statusName = "";
		String closeStatusName = "";
		if(form != null) {
			statusName = form.statusName;
			closeStatusName = form.closeStatusName;
		}
		mst.statusList = LabelValueBeanUtils.createList(statusName, true);
		
		//完了とみなすステータスのSetを作成
		List<LabelValueBean> closeList = LabelValueBeanUtils.createList(closeStatusName, true);
		Set<String> closeStatusSet = new LinkedHashSet<String>();
		for(LabelValueBean target : closeList) {
			if(StringUtils.isEmpty(target.getValue())) {
				continue;
			}
			closeStatusSet.add(target.getValue());
		}
		
		//ステータスのうち、完了とみなすステータスに含まれないものを未完了のステータスとして設定する
		Set<String> openStatusSet = new LinkedHashSet<String>();
		for(LabelValueBean target : mst.statusList) {
			if(StringUtils.isEmpty(target.getValue())) {
				continue;
			}
			if(closeStatusSet.contains(target.getValue()) == false) {
				openStatusSet.add(target.getLabel());
			}
		}
		mst.openStatus = openStatusSet.toArray(new String[0]);
		
		//ステータス検索用の情報を設定
		List<LabelValueBean> searchStatusList = LabelValueBeanUtils.createList(statusName, false);
		searchStatusList.add(0, new LabelValueBean("未完了", "未完了"));
		mst.searchStatusList = searchStatusList;
	}
}
