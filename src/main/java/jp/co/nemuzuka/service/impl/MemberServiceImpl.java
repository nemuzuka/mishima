package jp.co.nemuzuka.service.impl;

import java.util.List;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.MemberService;

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
		put(model);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#get(java.lang.String)
	 */
	@Override
	public MemberModel get(String keyString) {
		Key key = Datastore.stringToKey(keyString);
		return memberDao.get(key);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getList(java.lang.String)
	 */
	@Override
	public List<MemberModel> getList(String name) {
		List<MemberModel> list = memberDao.getList(name);
		return createViewData(list);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#put(jp.co.nemuzuka.model.MemberModel)
	 */
	@Override
	public void put(MemberModel model) {
		memberDao.put(model);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#getAllList()
	 */
	@Override
	public List<MemberModel> getAllList() {
		List<MemberModel> list = memberDao.getAllList();
		return createViewData(list);
	}
	
	/**
	 * 表示用データ作成.
	 * 不要なデータを削除します。
	 * @param list 対象List
	 * @return 設定後List
	 */
	private List<MemberModel> createViewData(List<MemberModel> list) {
		
		for(MemberModel target : list) {
			target.setCreatedAt(null);
			target.setCreateUser(null);
			target.setUpdatedAt(null);
			target.setUpdateUser(null);
		}
		return list;
	}

}
