package jp.co.nemuzuka.service.impl;

import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.common.UniqueKey;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.exception.AlreadyExistKeyException;
import jp.co.nemuzuka.form.MemberForm;
import jp.co.nemuzuka.form.PersonForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.utils.ConvertUtils;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

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

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#checkAndCreateAdminMember(java.lang.String, java.lang.String)
	 */
	@Override
	public void checkAndCreateAdminMember(String mail, String nickName) {
		MemberModel model = memberDao.get(mail);
		if(model != null) {
			return;
		}
		//存在しないので、登録する
		MemberForm form = new MemberForm();
		form.mail = mail;
		form.name = nickName;
		form.authority = Authority.admin.name();
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
		List<MemberModel> list = memberDao.getList(name, mail);
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
		
		MemberModel model = memberDao.get(email);
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
	}
}
