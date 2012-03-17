package jp.co.nemuzuka.service.impl;

import java.util.ArrayList;
import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.UniqueKey;
import jp.co.nemuzuka.core.entity.LabelValueBean;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.exception.AlreadyExistKeyException;
import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.utils.ConvertUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;

/**
 * MemberServiceの実装クラス.
 * @author kazumune
 */
public class MemberServiceImpl implements MemberService {

	MemberDao memberDao = new MemberDao();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#checkAndCreateAdminMember(java.lang.String)
	 */
	@Override
	public void checkAndCreateAdminMember(String mail) {
		MemberModel model = memberDao.get(mail);
		if(model != null) {
			return;
		}
		//存在しないので、登録する
		model = new MemberModel();
		model.createKey(mail);
		model.setName(mail);
		model.setAuthority(Authority.admin);
		memberDao.put(model);
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
			if(model != null) {
				setForm(form, model);
			}
		}
		form.authorityList = createAuthorityList();
		return form;
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getList(java.lang.String)
	 */
	@Override
	public List<MemberModel> getList(String name) {
		List<MemberModel> list = memberDao.getList(name);
		return list;
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
		} else {
			//新規の場合、入力されたKey項目相当が存在するかチェック
			if (Datastore.putUniqueValue(UniqueKey.member.name(), form.mail)) {
				throw new AlreadyExistKeyException();
			}
			model = new MemberModel();
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
		//削除
		memberDao.delete(model.getKey());
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getAllList()
	 */
	@Override
	public List<MemberModel> getAllList() {
		List<MemberModel> list = memberDao.getAllList();
		return list;
	}
	
	/**
	 * ユーザ権限LabelValueBean生成.
	 * @return 生成ユーザ権限LabelValueBeanのList
	 */
	private List<LabelValueBean> createAuthorityList() {
		
		List<LabelValueBean> list = new ArrayList<LabelValueBean>();
		Authority[] authoritys = Authority.values();
		for(Authority target : authoritys) {
			list.add(new LabelValueBean(target.getLabel(), target.getCode()));
		}
		list.add(0, new LabelValueBean("", ""));
		return list;
	}

	/**
	 * Form情報設定.
	 * @param form 設定対象Form
	 * @param model 設定Model
	 */
	private void setForm(MemberForm form, MemberModel model) {
		form.keyToString = model.getKeyToString();
		form.mail = model.getMail();
		form.name = model.getName();
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
		}
	}
}
