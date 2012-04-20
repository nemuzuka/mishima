package jp.co.nemuzuka.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

import jp.co.nemuzuka.dao.CommentDao;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.entity.CommentModelEx;
import jp.co.nemuzuka.model.CommentModel;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.CommentService;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

/**
 * CommentServiceの実装クラス.
 * @author k-katagiri
 */
public class CommentServiceImpl implements CommentService {

	CommentDao commentDao = CommentDao.getInstance();
	MemberDao memberDao = MemberDao.getInstance();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	private static CommentServiceImpl impl = new CommentServiceImpl();
	
	public static CommentServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private CommentServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.CommentService#getList(com.google.appengine.api.datastore.Key)
	 */
	@Override
	public List<CommentModelEx> getList(Key refsKey) {
		
		List<CommentModel> list = commentDao.getList(refsKey);
		Set<Key> memberKey = new LinkedHashSet<Key>();
		for(CommentModel target : list) {
			memberKey.add(target.getCreateMemberKey());
		}
		
		//メンバー情報を取得
		Map<Key, MemberModel> memberMap = memberDao.getMap(memberKey.toArray(new Key[0]));
		
		//戻りListを作成
		SimpleDateFormat sdf = DateTimeUtils.createSdf("MM/dd HH:mm");
		List<CommentModelEx> retList = new ArrayList<CommentModelEx>();
		for(CommentModel target : list) {
			CommentModelEx entity = new CommentModelEx();
			entity.setModel(target);
			
			String createMemberName = null;
			MemberModel memberModel = memberMap.get(target.getCreateMemberKey());
			if(memberModel != null) {
				createMemberName = memberModel.getName();
			} else {
				createMemberName = "";
			}
			entity.setCreateMemberName(createMemberName);
			
			entity.setCreatedAt(ConvertUtils.toString(target.getCreatedAt(), sdf));
			
			retList.add(entity);
		}
		return retList;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.CommentService#put(com.google.appengine.api.datastore.Key, java.lang.String, java.lang.String)
	 */
	@Override
	public void put(Key refsKey, String comment, String email) {
		
		CommentModel model = new CommentModel();
		model.setComment(new Text(StringUtils.defaultString(comment)));
		model.setCreateMemberKey(Datastore.createKey(MemberModel.class, email));
		model.setRefsKey(refsKey);
		commentDao.put(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.CommentService#delete(com.google.appengine.api.datastore.Key, java.lang.String, java.lang.Long)
	 */
	@Override
	public void delete(Key refsKey, String commentKeyString, Long commentVersionNo) {
		CommentModel model = commentDao.get(commentKeyString, commentVersionNo, refsKey);
		if(model == null) {
			//該当レコードが存在しない場合、Exceptionをthrow
			throw new ConcurrentModificationException();
		}
		commentDao.delete(model.getKey());
	}

}
