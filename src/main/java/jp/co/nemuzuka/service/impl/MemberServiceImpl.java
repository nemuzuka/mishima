package jp.co.nemuzuka.service.impl;

import jp.co.nemuzuka.common.Authority;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.MemberService;

/**
 * MemberServiceの実装クラス.
 * @author kazumune
 */
public class MemberServiceImpl implements MemberService {

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.MemberService#checkAndCreateAdminMember(java.lang.String)
	 */
	@Override
	public void checkAndCreateAdminMember(String mail) {
		MemberModel model = MemberDao.get(mail);
		if(model != null) {
			return;
		}
		//存在しないので、登録する
		model = new MemberModel();
		model.createKey(mail);
		model.setName(mail);
		model.setAuthority(Authority.admin);
		MemberDao.put(model);
	}
}
