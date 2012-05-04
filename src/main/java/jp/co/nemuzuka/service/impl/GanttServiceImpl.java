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

import jp.co.nemuzuka.dao.MilestoneDao;
import jp.co.nemuzuka.dao.TicketDao.Param;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.model.MilestoneModel;
import jp.co.nemuzuka.service.GanttService;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;

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
		return result;
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
			targetEndDate = DateTimeUtils.addMonth(targetStartDate, 1);
		}
		
		//日数を算出して、開始日〜終了日までの期間が15日以下の場合、終了日を開始日から15日後に設定
		if(DateTimeUtils.getDays(targetStartDate, targetEndDate) < 15) {
			targetEndDate = DateTimeUtils.addDay(targetStartDate, 15);
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
