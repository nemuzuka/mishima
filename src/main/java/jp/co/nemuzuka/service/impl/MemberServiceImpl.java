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

import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.UniqueKey;
import jp.co.nemuzuka.core.controller.AbsController;
import jp.co.nemuzuka.core.entity.mock.UserServiceImpl;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.entity.MemberKeyEntity;
import jp.co.nemuzuka.exception.AlreadyExistKeyException;
import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.form.PersonForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeChecker;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;
import org.slim3.memcache.Memcache;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * MemberServiceの実装クラス.
 * @author kazumune
 */
public class MemberServiceImpl implements MemberService {

	MemberDao memberDao = MemberDao.getInstance();
	
	private static MemberServiceImpl impl = new MemberServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static MemberServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private MemberServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#checkAndCreateMember(java.lang.String, java.lang.String, jp.co.nemuzuka.common.Authority)
	 */
	@Override
	public void checkAndCreateMember(String mail, String nickName, Authority authority) {
		MemberModel model = getModel(mail);
		if(model != null) {
			return;
		}
		//存在しないので、登録する
		MemberForm form = new MemberForm();
		form.mail = mail;
		form.name = nickName;
		form.timeZone = jp.co.nemuzuka.common.TimeZone.GMT_P_9.getCode();
		form.authority = authority.name();
		put(form);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#get(java.lang.String)
	 */
	@Override
	public MemberForm get(String keyString) {
		
		MemberForm form = new MemberForm();
		if(StringUtils.isNotEmpty(keyString)) {
			//Key情報が設定されていた場合
			Key key = Datastore.stringToKey(keyString);
			MemberModel model = memberDao.get(key);
			setForm(form, model);
		}
		return form;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<MemberModel> getList(String name, String mail) {
		return memberDao.getList(name, mail);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#put(jp.co.nemuzuka.form.MemberForm)
	 */
	@Override
	public void put(MemberForm form) {
		
		MemberModel model = null;
		if(StringUtils.isNotEmpty(form.keyToString)) {
			//更新の場合
			Key key = Datastore.stringToKey(form.keyToString);
			Long version = ConvertUtils.toLong(form.versionNo);
			//versionとKeyで情報を取得
			model = memberDao.get(key, version);
			if(model == null) {
				//該当レコードが存在しない場合、Exceptionをthrow
				throw new ConcurrentModificationException();
			}
		} else {
			//新規の場合、入力されたKey項目相当が存在するかチェック
			if (Datastore.putUniqueValue(UniqueKey.member.name(), form.mail) == false) {
				throw new AlreadyExistKeyException();
			}
			model = new MemberModel();
			reSetMemberKeyEntity();
		}
		
		//取得した情報に対してプロパティを更新
		setModel(model, form);
		memberDao.put(model);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#delete(jp.co.nemuzuka.form.MemberForm)
	 */
	@Override
	public void delete(MemberForm form) {
		//versionとKeyで情報を取得
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		MemberModel model = memberDao.get(key, version);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		//削除
		memberDao.delete(model.getKey());
		//一意制約チェック用のModelからも削除する
		Datastore.deleteUniqueValue(UniqueKey.member.name(), model.getMail());

		//キャッシュ初期化
		reSetMemberKeyEntity();
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getAllList()
	 */
	@Override
	public List<MemberModel> getAllList() {
		List<MemberModel> list = memberDao.getAllList();
		return list;
	}
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getPersonForm(java.lang.String)
	 */
	@Override
	public PersonForm getPersonForm(String email) {
		
		PersonForm form = new PersonForm();
		MemberModel model = getModel(email);
		if(model == null) {
			return form;
		}
		
		form.keyToString = model.getKeyToString();
		if(model.getMemo() != null) {
			form.memo = model.getMemo().getValue();
		}
		form.name = model.getName();
		form.versionNo = ConvertUtils.toString(model.getVersion());
		return form;
	}


	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#put(jp.co.nemuzuka.form.PersonForm)
	 */
	@Override
	public void put(PersonForm form) {
		//更新の場合
		Key key = Datastore.stringToKey(form.keyToString);
		Long version = ConvertUtils.toLong(form.versionNo);
		//versionとKeyで情報を取得
		MemberModel model = memberDao.get(key, version);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		//入力値を設定して、登録
		model.setName(form.name);
		model.setMemo(new Text(StringUtils.defaultString(form.memo)));
		memberDao.put(model);
	}


	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getMap(com.google.appengine.api.datastore.Key[])
	 */
	@Override
	public Map<Key, MemberModel> getMap(Key... keys) {
		return memberDao.getMap(keys);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getKey(java.lang.String)
	 */
	@Override
	public Key getKey(String mail) {
		return getMemberKeyEntity().map.get(mail);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getKeyString(java.lang.String)
	 */
	@Override
	public String getKeyString(String mail) {
		return getMemberKeyEntity().keyStringMap.get(mail);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getTimeZone(java.lang.String)
	 */
	@Override
	public String getTimeZone(String mail) {
		return getMemberKeyEntity().timeZoneMap.get(mail);
	}
	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getModel(java.lang.String)
	 */
	@Override
	public MemberModel getModel(String mail) {
		Key key = getKey(mail);
		return memberDao.get(key);
	}

	/**
	 * MemberKeyEntityリセット.
	 * 更新時刻をリセットし、次回アクセスには最新情報を取得するようにします。
	 * 新規ユーザ登録時に呼ばれることを想定しています。
	 */
	private void reSetMemberKeyEntity() {
		MemberKeyEntity entity = Memcache.get(MemberKeyEntity.class.getName());
		if(entity == null) {
			return;
		}
		entity.refreshStartTime = null;
		Memcache.put(MemberKeyEntity.class.getName(), entity);
	}
	
	/**
	 * MemberKeyEntity取得.
	 * Memcacheよりデータを取得し、存在ない or 期限切れの場合、最新情報に更新します。
	 * @return MemberKeyEntityインスタンス
	 */
	private MemberKeyEntity getMemberKeyEntity() {
		//Memcacheよりデータを取得する
		MemberKeyEntity entity = Memcache.get(MemberKeyEntity.class.getName());
		if(entity == null || entity.refreshStartTime == null || 
				DateTimeChecker.isOverRefreshStartTime(entity.refreshStartTime)) {
			//存在しない or 更新期限切れの場合、リフレッシュ
			entity = createMemberKeyEntity();
			Memcache.put(MemberKeyEntity.class.getName(), entity);
		}
		return entity;
	}

	/**
	 * MemberKeyEntity生成.
	 * @return MemberKeyEntityインスタンス
	 */
	private MemberKeyEntity createMemberKeyEntity() {
		MemberKeyEntity entity = new MemberKeyEntity();
		
		//登録されているMember情報を取得
		List<MemberModel> list = getList("", "");
		for(MemberModel target : list) {
			entity.map.put(target.getMail(), target.getKey());
			entity.keyStringMap.put(target.getMail(), target.getKeyToString());
			entity.timeZoneMap.put(target.getMail(), target.getTimeZone());
		}
		
		//トライアル版でかつ、ダミーユーザが未アクセスの場合、ダミーユーザのKeyを追加
		if(AbsController.trialMode) {
			if(entity.map.get(UserServiceImpl.DUMMY_EMAIL) == null) {
				//まだダミーユーザがMemberModelに登録されていない状態を想定
				entity.map.put(UserServiceImpl.DUMMY_EMAIL, 
						Datastore.createKey(MemberModel.class, UserServiceImpl.DUMMY_EMAIL));
			}
		}
		
		//現在時刻に加算分の時刻(分)を加算し、設定する
		Date date = CurrentDateUtils.getInstance().getCurrentDateTime();
		int min = ConvertUtils.toInteger(System.getProperty("jp.co.nemuzuka.member.map.refresh.min", "15"));
		date = DateTimeUtils.addMinutes(date, min);
		entity.refreshStartTime = date;
		return entity;
	}
	
	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定Model
	 */
	private void setForm(MemberForm form, MemberModel model) {
		if(model == null) {
			return;
		}
		form.keyToString = model.getKeyToString();
		form.mail = model.getMail();
		form.name = model.getName();
		form.timeZone = model.getTimeZone();
		form.authority = model.getAuthority().getCode();
		form.versionNo = ConvertUtils.toString(model.getVersion());
	}

	/**
	 * Model情報設定.
	 * メールアドレスは、更新不可なのでパラメータを変更されても無視する。
	 * @param model 設定対象Model
	 * @param form 設定Form
	 */
	private void setModel(MemberModel model, MemberForm form) {
		
		if(model.getKey() == null) {
			//新規の場合
			model.createKey(form.mail);
		}
		model.setName(form.name);
		Authority authority = Authority.fromCode(form.authority);
		if(authority != null) {
			model.setAuthority(authority);
		} else {
			model.setAuthority(Authority.normal);
		}
		
		//タイムゾーンの設定
		String timeZone = form.timeZone;
		if(StringUtils.isEmpty(timeZone)) {
			timeZone = jp.co.nemuzuka.common.TimeZone.GMT_P_9.getCode();
		}
		model.setTimeZone(TimeZone.getTimeZone(timeZone).getID());
	}
}
