/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.co.nemuzuka.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.dao.MilestoneDao;
import jp.co.nemuzuka.dao.TicketDao.Param;
import jp.co.nemuzuka.entity.ChildKeyListEntity;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.service.GanttService;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;

import com.google.appengine.api.datastore.Key;

/**
 * GanttServiceの実装クラス.
 * @author k-katagiri
 */
public class GanttServiceImpl implements GanttService {

	TicketService ticketService = TicketServiceImpl.getInstance();
	MilestoneDao milestoneDao = MilestoneDao.getInstance();
	
	private static GanttServiceImpl impl = new GanttServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static GanttServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private GanttServiceImpl(){}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.GanttService#getList(jp.co.nemuzuka.dao.TicketDao.Param)
	 */
	@Override
	public Result getList(Param param) {

		//一覧取得
		List<TicketModelEx> ticketList = ticketService.getList(param, "", false);
		reCreateTicketList(ticketList);
		
		Result result = new Result();
		result.ticketList = ticketList;

		//マイルストーン情報取得
		Date startDate = null;
		Date endDate = null;
		if(StringUtils.isNotEmpty(param.milestone)) {
			MilestoneModel milestone = milestoneDao.get(param.milestone);
			if(milestone != null) {
				startDate = milestone.getStartDate();
				endDate = milestone.getEndDate();
				result.milestoneName = milestone.getMilestoneName();
				SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
				result.startDate = ConvertUtils.toString(startDate, sdf);
				result.endDate = ConvertUtils.toString(endDate, sdf);
				result.milestoneStartDate = ConvertUtils.toString(startDate, sdf);
				result.milestoneEndDate = ConvertUtils.toString(endDate, sdf);
			}
		}

		//チケットの日付を再設定する
		setDate(result, startDate, endDate);
		reCreateTicketList(result.ticketList);
		return result;
	}

	/**
	 * チケット一覧再作成.
	 * 子チケットを意識してチケット一覧を最生成します。
	 * @param ticketList チケット一覧
	 */
	void reCreateTicketList(List<TicketModelEx> ticketList) {
		
		//Keyに紐付くTicketMapと、親Keyに紐付く子Ticket管理用Entityを生成
		Map<Key, TicketModelEx> map = new HashMap<Key, TicketModelEx>();
		Map<Key, ChildKeyListEntity> parentMap = new LinkedHashMap<Key, ChildKeyListEntity>();
		for(TicketModelEx target : ticketList) {
			Key key = target.getModel().getKey();
			map.put(key, target);
			
			Key parentKey = target.getModel().getParentTicketKey();
			ChildKeyListEntity entity = parentMap.get(parentKey);
			if(entity == null) {
				entity = new ChildKeyListEntity();
				parentMap.put(parentKey, entity);
			}
			int index = entity.childKeys.size();
			entity.childKeys.add(key);
			entity.childMap.put(key, index);
			entity.grandchildList.add(null);
		}
		
		boolean isContinue = true;
		Set<Key> doneParentKeySet = new HashSet<Key>();
		while(isContinue) {
			int beforeSize = parentMap.size();
			for(Map.Entry<Key, ChildKeyListEntity> e : parentMap.entrySet()) {
				Key parentKey = e.getKey();
				if(parentKey == null) {
					//親Keyが存在しないTicket群の場合、次のレコードへ
					continue;
				}
				if(doneParentKeySet.contains(parentKey)) {
					//処理済みのKeyの場合、次のレコードへ
					continue;
				}
				
				//処理対象の親Keyが子供になる箇所を検索し、移動する
				if(moveParentKey(parentMap, parentKey, e.getValue())) {
					//存在したので、Mapから削除し、loop終了
					parentMap.remove(parentKey);
					break;
				} else {
					//処理対象の親Keyが存在しなかったので、次のレコードが処理対象
					doneParentKeySet.add(parentKey);
				}
			}
			int afterSize = parentMap.size();
			
			if(beforeSize == afterSize) {
				//処理前と処理後のMapのサイズが等しい場合、処理終了
				isContinue = false;
			}
		}
		
		//ソートしたMapを元に、ネストを設定する
		createList(ticketList, map, parentMap);
		return;
	}


	/**
	 * TicketList再設定.
	 * 親Ticket管理Mapを元に、ソート済みのListを再設定します。
	 * @param ticketList 設定対象TicketList
	 * @param map 全Ticket管理Map
	 * @param parentMap 親Ticket管理Map
	 */
	private void createList(List<TicketModelEx> ticketList,
			Map<Key, TicketModelEx> map, Map<Key, ChildKeyListEntity> parentMap) {
		
		ticketList.clear();
		
		for(Map.Entry<Key, ChildKeyListEntity> e : parentMap.entrySet()) {
			ChildKeyListEntity targetEntity = e.getValue();
			int nestingLevel = 0;
			setTicketList(targetEntity, nestingLevel, ticketList, map);
		}
	}

	/**
	 * List設定.
	 * 子TicketKeyListを元に戻り値Listを再設定します。
	 * 孫Ticketが存在する場合、戻り値Listに含めます。
	 * ※再帰呼び出しします。
	 * @param targetEntity 対象子Ticket管理用Entity
	 * @param nestingLevel ネストの深さ
	 * @param ticketList 設定対象TicketList
	 * @param map 全Ticket管理Map
	 */
	void setTicketList(ChildKeyListEntity targetEntity, int nestingLevel,
			List<TicketModelEx> ticketList, Map<Key, TicketModelEx> map) {
		//子TicketKeyListを元に設定
		int index = 0;
		for(Key key : targetEntity.childKeys) {
			TicketModelEx targetModelEx = map.get(key);
			targetModelEx.setNestingLevel(nestingLevel);
			ticketList.add(targetModelEx);
			
			//孫TicketKeyに対して設定
			ChildKeyListEntity grandchild = targetEntity.grandchildList.get(index);
			index++;
			if(grandchild == null) {
				continue;
			}
			setTicketList(grandchild, (nestingLevel + 1), ticketList, map);
		}
	}

	/**
	 * 子要素参照変更.
	 * 対象のKey情報が子要素となる位置を参照し、存在する場合参照を変更します。
	 * @param parentMap 元Map
	 * @param targetKey 対象Key
	 * @param targetEntity 対象参照元
	 * @return 対象のKey要素が子要素となる箇所が存在した場合、true
	 */
	boolean moveParentKey(Map<Key, ChildKeyListEntity> parentMap,
			Key targetKey, ChildKeyListEntity targetEntity) {
		for(Map.Entry<Key, ChildKeyListEntity> e : parentMap.entrySet()) {
			//自分の要素の場合は次の処理へ
			if(e.getKey() != null && e.getKey().equals(targetKey)) {
				continue;
			}
			ChildKeyListEntity entity = e.getValue();
			if(moveParentKey(targetKey, entity, targetEntity)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 子要素参照設定.
	 * 引数の対象Key情報が子要素となる位置を参照し、
	 * 存在する場合参照を設定します。
	 * 存在しない場合、孫要素に対して同処理を行います。
	 * @param targetKey 対象Key
	 * @param checkTarget チェック対象子Ticket管理用Entity
	 * @param targetEntity 対象参照元
	 * @return 対象のKey要素が子要素となる箇所が存在した場合、true
	 */
	boolean moveParentKey(Key targetKey, ChildKeyListEntity checkTarget, 
			ChildKeyListEntity targetEntity) {
		
		//対象要素の中に、自分の親Keyが存在する場合参照を変更する
		Integer index = checkTarget.childMap.get(targetKey);
		if(index != null) {
			checkTarget.grandchildList.set(index, targetEntity);
			return true;
		} else {
			//対象要素の孫Ticket管理用EntityListに対して同じ処理を繰り返し行う
			for(ChildKeyListEntity target : checkTarget.grandchildList) {
				if(target != null) {
					if(moveParentKey(targetKey, target, targetEntity)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * チケット開始、終了日付再設定.
	 * @param result ガントチャート出力情報
	 * @param startDate マイルストーン開始日
	 * @param endDate マイルストーン終了日
	 */
	void setDate(Result result, Date startDate,
			Date endDate) {
		
		Date targetStartDate = startDate;
		Date targetEndDate = endDate;

		for(TicketModelEx target : result.ticketList) {
			
			Date ticketStartDate = target.getModel().getStartDate();
			Date ticketEndDate = target.getModel().getPeriod();
			if(ticketEndDate != null && ticketStartDate == null) {
				//開始日が未設定で終了日が設定されている場合、比較対象として終了日を設定する
				//このケースの時に開始日の最小値の可能性があるため
				ticketStartDate = ticketEndDate;
			}
			
			targetStartDate = checkDate(ticketStartDate, targetStartDate, true);
			targetEndDate = checkDate(ticketEndDate, targetEndDate, false);
		}
		
		//開始日がnullの場合、システム日付を設定する
		if(targetStartDate == null) {
			targetStartDate = CurrentDateUtils.getInstance().getCurrentDate();
		}
		//終了日がnullの場合、開始日の1ヶ月後を設定する
		if(targetEndDate == null) {
			targetEndDate = DateTimeUtils.addMonths(targetStartDate, 1);
		}
		
		//日数を算出して、開始日〜終了日までの期間が15日以下の場合、終了日を開始日から15日後に設定
		if(DateTimeUtils.getDays(targetStartDate, targetEndDate) < 15) {
			targetEndDate = DateTimeUtils.addDays(targetStartDate, 15);
		}
		
		//Ticketの開始日、期限がnullのものに対して値を設定する
		replaceNullDate(result.ticketList, targetStartDate, targetEndDate);
		
		//チャートの開始日・終了日を設定
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		result.startDate = ConvertUtils.toString(targetStartDate, sdf);
		result.endDate = ConvertUtils.toString(targetEndDate, sdf);
		
		//マイルストーンが設定されていて、
		//マイルストーンの開始日・終了日が空の場合、設定
		if(StringUtils.isNotEmpty(result.milestoneName)) {
			if(StringUtils.isEmpty(result.milestoneStartDate)) {
				result.milestoneStartDate = result.startDate;
				result.updateStartDate = true;
			}
			
			if(StringUtils.isEmpty(result.milestoneEndDate)) {
				result.milestoneEndDate = result.endDate;
				result.updateEndDate = true;
			}
		}
	}

	/**
	 * Ticket開始日、期限null値再設定.
	 * Ticketの開始日がnullのもの、期限がnullのものを置き換えます。
	 * @param ticketList Ticket一覧
	 * @param startDate 置換対象開始日
	 * @param endDate 置換対象終了日
	 */
	private void replaceNullDate(List<TicketModelEx> ticketList,
			Date startDate, Date endDate) {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		String start = ConvertUtils.toString(startDate, sdf);
		String end = ConvertUtils.toString(endDate, sdf);
		
		for(TicketModelEx target : ticketList) {
			if(StringUtils.isEmpty(target.getStartDate())) {
				target.setStartDate(start);
				target.setUpdateStartDate(true);
			}
			if(StringUtils.isEmpty(target.getPeriod())) {
				target.setPeriod(end);
				target.setUpdatePeriod(true);
			}
		}
	}

	/**
	 * 日付チェック.
	 * 開始日のチェックを行う場合、
	 * 	チェック対象日 < 基準日の場合、チェック対象日を返却します。
	 * 終了日のチェックを行う場合、
	 * 	チェック対象日 > 基準日の場合、チェック対象日を返却します。
	 * @param targetDate チェック対象日
	 * @param baseDate 基準日
	 * @param startDate 開始日のチェックを行う場合、true
	 * @return 設定日付
	 */
	private Date checkDate(Date targetDate, Date baseDate, boolean startDate) {
		if(baseDate == null) {
			return targetDate;
		}
		
		if(targetDate == null) {
			return baseDate;
		}
		if(startDate) {
			if(targetDate.getTime() < baseDate.getTime()) {
				return targetDate;
			}
		} else {
			if(targetDate.getTime() > baseDate.getTime()) {
				return targetDate;
			}
		}
		return baseDate;
	}
}
