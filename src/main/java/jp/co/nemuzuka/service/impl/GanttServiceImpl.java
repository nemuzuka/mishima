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
import java.util.List;

import org.apache.commons.lang.StringUtils;

import jp.co.nemuzuka.dao.MilestoneDao;
import jp.co.nemuzuka.dao.TicketDao.Param;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.service.GanttService;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

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

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.GanttService#getList(jp.co.nemuzuka.dao.TicketDao.Param, java.lang.String)
	 */
	@Override
	public Result getList(Param param, String projectKeyString) {

		//一覧取得
		List<TicketModelEx> ticketList = ticketService.getList(param, "", false);
		
		Result result = new Result();
		result.ticketList = ticketList;

		//マイルストーン情報取得
		Date startDate = null;
		Date endDate = null;
		if(StringUtils.isNotEmpty(param.milestone)) {
			MilestoneModel milestone = milestoneDao.get(param.milestone);
			startDate = milestone.getStartDate();
			endDate = milestone.getEndDate();
			result.milestoneName = milestone.getMilestoneName();
		}
		
		//チケットの日付を再設定する
		setDate(ticketList, startDate, endDate);
		return result;
	}

	/**
	 * チケット開始、終了日付再設定.
	 * @param ticketList チケット一覧
	 * @param startDate マイルストーン開始日
	 * @param endDate マイルストーン終了日
	 */
	private void setDate(List<TicketModelEx> ticketList, Date startDate,
			Date endDate) {
		
		Date targetStartDate = startDate;
		Date targetEndDate = endDate;
		
		for(TicketModelEx target : ticketList) {
			targetStartDate = checkDate(target.getModel().getStartDate(), targetStartDate, true);
			targetEndDate = checkDate(target.getModel().getPeriod(), targetEndDate, false);
		}
		
		//開始日がnullの場合、システム日付を設定する
		if(targetStartDate == null) {
			targetStartDate = CurrentDateUtils.getInstance().getCurrentDate();
		}
		//終了日がnullの場合、システム日付の翌月最終日を設定する
		if(targetEndDate == null) {
			Date currentDate = CurrentDateUtils.getInstance().getCurrentDate();
			Date nextMonthDate = DateTimeUtils.addMonth(currentDate, 1);
			SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMM");
			List<Date> startEndList = DateTimeUtils.getStartEndDate(sdf.format(nextMonthDate));
			targetEndDate = startEndList.get(1);
		}
		
		//Ticketの開始日、期限がnullのものに対して値を設定する
		replaceNullDate(ticketList, targetStartDate, targetEndDate);
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
			}
			if(StringUtils.isEmpty(target.getPeriod())) {
				target.setPeriod(end);
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
