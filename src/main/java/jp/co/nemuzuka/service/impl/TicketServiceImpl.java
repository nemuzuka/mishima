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
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.nemuzuka.common.PeriodStatus;
import jp.co.nemuzuka.dao.TicketDao;
import jp.co.nemuzuka.dao.TicketDao.Param;
import jp.co.nemuzuka.entity.TicketModelEx;
import jp.co.nemuzuka.exception.NotExistTicketException;
import jp.co.nemuzuka.exception.ParentSelfTicketException;
import jp.co.nemuzuka.form.ProjectForm;
import jp.co.nemuzuka.form.TicketCommentForm;
import jp.co.nemuzuka.form.TicketDetailForm;
import jp.co.nemuzuka.form.TicketForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.TicketModel;
import jp.co.nemuzuka.service.CommentService;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.TicketService;
import jp.co.nemuzuka.service.UploadFileService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * TicketServiceの実装クラス.
 * @author k-katagiri
 */
public class TicketServiceImpl implements TicketService {

	TicketDao ticketDao = TicketDao.getInstance();
	
	ProjectService projectService = ProjectServiceImpl.getInstance();
	CommentService commentService = CommentServiceImpl.getInstance();
	TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	MemberService memberService = MemberServiceImpl.getInstance();
	UploadFileService uploadFileService = UploadFileServiceImpl.getInstance();
	
	private static TicketServiceImpl impl = new TicketServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static TicketServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * コンストラクタ.
	 */
	private TicketServiceImpl(){}
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#getList(jp.co.nemuzuka.dao.TicketDao.Param, java.lang.String, boolean)
	 */
	@Override
	public List<TicketModelEx> getList(Param param, String mail, boolean isDashboard) {
		List<TicketModel> modelList = null;
		if(isDashboard) {
			String memberKeyString = memberService.getKeyString(mail);
			modelList = ticketDao.getDashbordList(param.limit, memberKeyString, 
					param.projectKeyString, param.openStatus);
		} else {
			modelList = ticketDao.getList(param);
		}
		
		//プロジェクト情報を取得
		ProjectForm projectForm = projectService.get(param.projectKeyString);
		if(StringUtils.isEmpty(projectForm.keyToString)) {
			//存在しない場合、一覧はサイズ0のTicket
			return new ArrayList<TicketModelEx>();
		}
		String projectId = projectForm.projectId;
		
		//登録されている担当者情報を取得
		Set<Key> memberKeySet = new LinkedHashSet<Key>();
		for(TicketModel target : modelList) {
			if(target.getTargetMemberKey() != null) {
				memberKeySet.add(target.getTargetMemberKey());
			}
		}
		Map<Key, MemberModel> memberMap = null;
		if(memberKeySet.size() != 0) {
			memberMap = memberService.getMap(memberKeySet.toArray(new Key[0]));
		} else {
			memberMap = new HashMap<Key, MemberModel>();
		}
		
		
		//戻りListを作成
		Date today = CurrentDateUtils.getInstance().getCurrentDate();
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		SimpleDateFormat sdf2 = DateTimeUtils.createSdf("yyyy/MM/dd HH:mm");
		List<TicketModelEx> retList = new ArrayList<TicketModelEx>();
		for(TicketModel target : modelList) {
			TicketModelEx entity = new TicketModelEx();
			entity.setModel(target);
			entity.setStartDate(ConvertUtils.toString(target.getStartDate(), sdf));
			entity.setPeriod(ConvertUtils.toString(target.getPeriod(), sdf));
			entity.setCreatedAt(ConvertUtils.toString(target.getCreatedAt(), sdf2));
			
			//期限が設定されている場合
			if(target.getPeriod() != null) {
				entity.setPeriodStatus(
						createPeriodStatus(today, target.getPeriod(), target.getStatus(), param.openStatus));
			}
			
			//担当者が設定されている場合
			if(target.getTargetMemberKey() != null) {
				MemberModel member = memberMap.get(target.getTargetMemberKey());
				if(member != null) {
					entity.setTargetMemberName(member.getName());
				}
			}
			
			//ステータスが完了しているかを設定
			entity.setCloseStatus(isCloseStatus(target.getStatus(), param.openStatus));
			
			//Noを表示する
			if(StringUtils.isNotEmpty(projectId)) {
				entity.setViewNo(projectId + "-" + target.getNo());
			} else {
				entity.setViewNo(String.valueOf(target.getNo()));
			}
			retList.add(entity);
		}
		return retList;
	}

	/**
	 * ステータスがクローズ状態かを判断します。
	 * @param status ステータス
	 * @param openStatus 未完了を意味するステータス配列
	 * @return ステータスがクローズ状態の場合、true
	 */
	private boolean isCloseStatus(String status, String[] openStatus) {
		
		if(openStatus == null || openStatus.length == 0) {
			return true;
		}
		for(String targetStatus : openStatus) {
			if(targetStatus.equals(status)) {
				return false;
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#get(java.lang.String, java.lang.String)
	 */
	@Override
	public TicketForm get(String keyString, String projectKeyString) {
		
		TicketForm form = new TicketForm();
		if(StringUtils.isNotEmpty(keyString)) {
			//Key情報が設定されていた場合
			Key key = Datastore.stringToKey(keyString);
			Key projectKey = Datastore.stringToKey(projectKeyString);
			TicketModel model = ticketDao.getWithProjectKey(key, projectKey);
			setForm(form, model);
		}
		ProjectForm projectForm = projectService.get(projectKeyString);
		form.setProjectId(projectForm.projectId);
		form.ticketMst = ticketMstService.getTicketMst(projectKeyString);
		return form;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#getDetail(java.lang.String, java.lang.String)
	 */
	@Override
	public TicketDetailForm getDetail(String keyString, String projectKeyString) {
		
		TicketForm form = get(keyString, projectKeyString);
		if(StringUtils.isEmpty(form.keyToString)) {
			return null;
		}
		TicketDetailForm detailForm = new TicketDetailForm();
		detailForm.setForm(form);
		detailForm.commentList = commentService.getList(Datastore.stringToKey(form.keyToString));
		detailForm.uploadFileList = uploadFileService.getList(keyString, projectKeyString);
		setTicketConn(detailForm, projectKeyString);
		return detailForm;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#put(jp.co.nemuzuka.form.TicketForm, java.lang.String)
	 */
	@Override
	public void put(TicketForm form, String projectKeyString) throws NotExistTicketException, ParentSelfTicketException {
		TicketModel model = null;
		Key projectKey = Datastore.stringToKey(projectKeyString);
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//KeyとプロジェクトKeyとバージョンで取得
			model = ticketDao.get(key, version, projectKey);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			model = new TicketModel();
			model.setProjectKey(projectKey);
		}
		setModel(model, form);
		ticketDao.put(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#delete(jp.co.nemuzuka.form.TicketForm, java.lang.String)
	 */
	@Override
	public void delete(TicketForm form, String projectKeyString) {
		Key projectKey = Datastore.stringToKey(projectKeyString);
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//KeyとプロジェクトKeyとバージョンで取得
		TicketModel model = ticketDao.get(key, version, projectKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		ticketDao.delete(key);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#updateTicketStatus(jp.co.nemuzuka.form.TicketForm, java.lang.String)
	 */
	@Override
	public void updateTicketStatus(TicketForm form, String projectKeyString) {
		Key projectKey = Datastore.stringToKey(projectKeyString);
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//KeyとプロジェクトKeyとバージョンで取得
		TicketModel model = ticketDao.get(key, version, projectKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		model.setStatus(form.status);
		ticketDao.put(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#putComment(jp.co.nemuzuka.form.TicketCommentForm, java.lang.String, java.lang.String)
	 */
	@Override
	public void putComment(TicketCommentForm form, String projectKeyString, String email) {
		
		//ステータスが変更されていないかチェックする
		Key ticketModelKey = Datastore.stringToKey(form.keyToString);
		Key projectKey = Datastore.stringToKey(projectKeyString);
		
		//Keyとプロジェクトでレコードを取得
		TicketModel model = ticketDao.getWithProjectKey(ticketModelKey, projectKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		
		//ステータスが変更されているかチェックする
		if(model.getStatus().equals(form.status) == false) {
			Long versonNo = ConvertUtils.toLong(form.versionNo);
			//変更されているので、更新
			if(model.getVersion().equals(versonNo) == false) {
				//他のユーザに更新されている可能性があるので、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
			model.setStatus(form.status);
			ticketDao.put(model);
		}
		//コメント登録
		commentService.put(ticketModelKey, form.comment, email);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.TicketService#deleteComment(java.lang.String, java.lang.String, java.lang.Long, java.lang.String)
	 */
	@Override
	public void deleteComment(String keyString, String commentKeyString,
			Long commentVersionNo, String projectKeyString) {
		
		Key ticketModelKey = Datastore.stringToKey(keyString);
		Key projectKey = Datastore.stringToKey(projectKeyString);
		
		//Keyとプロジェクトでレコードを取得
		TicketModel model = ticketDao.getWithProjectKey(ticketModelKey, projectKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		//コメント削除
		commentService.delete(ticketModelKey, commentKeyString, commentVersionNo);

	}
	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定Model
	 */
	private void setForm(TicketForm form, TicketModel model) {
		
		if(model == null) {
			return;
		}
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		form.keyToString = model.getKeyToString();
		form.status = model.getStatus();
		form.title = model.getTitle();
		if(model.getContent() != null) {
			form.content = model.getContent().getValue();
		}
		if(model.getEndCondition() != null) {
			form.endCondition = model.getEndCondition().getValue();
		}
		form.startDate = ConvertUtils.toString(model.getStartDate(), sdf);
		form.period = ConvertUtils.toString(model.getPeriod(), sdf);
		form.priority = model.getPriority();
		form.targetKind = model.getTargetKind();
		form.category = model.getCategory();
		if(model.getMilestone() != null) {
			form.milestone = Datastore.keyToString(model.getMilestone());
		}
		form.targetVersion = model.getTargetVersion();
		if(model.getTargetMemberKey() != null) {
			form.targetMember = Datastore.keyToString(model.getTargetMemberKey());
		}
		if(model.getParentTicketKey() != null) {
			
			//親Keyに紐付くNoを変換して、設定
			TicketModel parentModel = ticketDao.getWithProjectKey(
					model.getParentTicketKey(), model.getProjectKey());
			if(parentModel != null) {
				form.parentKey = ConvertUtils.toString(parentModel.getNo());
			}
		}
		form.versionNo = ConvertUtils.toString(model.getVersion());
		form.id = ConvertUtils.toString(model.getNo());
	}

	/**
	 * Model情報設定.
	 * @param model 設定対象Model
	 * @param form 設定Form
	 * @throws NotExistTicketException 親チケット指定時、存在しないチケットNoを指定された
	 * @throws ParentSelfTicketException 親チケット指定時、自分のチケットNoを指定された
	 */
	private void setModel(TicketModel model, TicketForm form) throws NotExistTicketException, ParentSelfTicketException {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		model.setTitle(form.title);
		model.setContent(new Text(StringUtils.defaultString(form.content)));
		model.setEndCondition(new Text(StringUtils.defaultString(form.endCondition)));
		model.setPriority(form.priority);
		model.setStatus(form.status);
		model.setTargetKind(form.targetKind);
		model.setCategory(form.category);
		
		Key milestoneKey = null;
		if(StringUtils.isNotEmpty(form.milestone)) {
			milestoneKey = Datastore.stringToKey(form.milestone);
		}
		model.setMilestone(milestoneKey);
		
		model.setTargetVersion(form.targetVersion);
		
		Key targetMemberKey = null;
		if(StringUtils.isNotEmpty(form.targetMember)) {
			targetMemberKey = Datastore.stringToKey(form.targetMember);
		}
		model.setTargetMemberKey(targetMemberKey);
		
		model.setStartDate(ConvertUtils.toDate(form.startDate, sdf));
		model.setPeriod(ConvertUtils.toDate(form.period, sdf));
		
		Key parentKey = null;
		if(StringUtils.isNotEmpty(form.parentKey)) {
			//入力されたNoに紐付くTicketを存在チェックし、存在なければ、Exception
			parentKey = ticketDao.getWithNoAndProjectKey(
					ConvertUtils.toLong(form.parentKey), model.getProjectKey());
			if(parentKey == null) {
				throw new NotExistTicketException();
			}
			
			if(parentKey.equals(model.getKey())) {
				//自分を参照する設定になっている場合、Exception
				throw new ParentSelfTicketException();
			}
		}
		model.setParentTicketKey(parentKey);
	}


	/**
	 * 期限ステータス判断.
	 * @param today システム日付
	 * @param targetDate 期限
	 * @param status ステータス
	 * @param openStatus 未完了を意味するステータス
	 * @return 期限ステータス
	 */
	PeriodStatus createPeriodStatus(Date today, Date targetDate, String status, String[] openStatus) {
		long toDayTime = today.getTime();
		long periodTime = targetDate.getTime();
		
		Set<String> openStatsSet = new LinkedHashSet<String>();
		for(String target : openStatus) {
			openStatsSet.add(target);
		}
		
		//ステータスが未完了の場合、システム日付と期限を参照して期限ステータスを返す
		PeriodStatus periodStatus = null;
		if(openStatsSet.contains(status)) {
			if(toDayTime == periodTime) {
				periodStatus = PeriodStatus.today;
			} else if(toDayTime > periodTime) {
				periodStatus = PeriodStatus.periodDate;
			}
		}
		return periodStatus;
	}
	
	/**
	 * チケット関連情報取得.
	 * @param detailForm 設定Form
	 * @param projectKeyString　プロジェクトKey文字列
	 */
	private void setTicketConn(TicketDetailForm detailForm, String projectKeyString) {
		
		detailForm.childTicketList = new ArrayList<TicketModel>();
		Key projectKey = Datastore.stringToKey(projectKeyString);

		Set<Key> targetKeySet = new LinkedHashSet<Key>();
		//自分の親が設定されている場合
		Key parentKey = null;
		if(StringUtils.isNotEmpty(detailForm.form.parentKey)) {
			
			//Noからデータを取得
			parentKey = ticketDao.getWithNoAndProjectKey(
					ConvertUtils.toLong(detailForm.form.parentKey), 
					projectKey);
			if(parentKey != null) {
				targetKeySet.add(parentKey);
			}
		}
		
		//自分が親になっているTicketを取得
		Key self = Datastore.stringToKey(detailForm.form.keyToString);
		List<Key> childKeyList = ticketDao.getChildList(self, projectKey);
		targetKeySet.addAll(childKeyList);
		if(targetKeySet.size() == 0) {
			return;
		}
		
		//取得した関連Ticketを設定する
		Map<Key, TicketModel> ticketMap = ticketDao.getMap(projectKeyString, targetKeySet.toArray(new Key[0]));
		if(parentKey != null) {
			TicketModel parentTicket = ticketMap.get(parentKey);
			if(parentTicket != null) {
				detailForm.parentTicket = parentTicket;
			}
		}
		for(Key targetKey : childKeyList) {
			TicketModel targetModel = ticketMap.get(targetKey);
			if(targetModel != null) {
				detailForm.childTicketList.add(targetModel);
			}
		}
	}
}
