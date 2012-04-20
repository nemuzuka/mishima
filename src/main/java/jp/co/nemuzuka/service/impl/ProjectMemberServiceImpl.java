package jp.co.nemuzuka.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slim3.datastore.Datastore;

import jp.co.nemuzuka.common.ProjectAuthority;
import jp.co.nemuzuka.dao.MemberDao;
import jp.co.nemuzuka.dao.ProjectMemberDao;
import jp.co.nemuzuka.entity.ProjectMemberModelEx;
import jp.co.nemuzuka.form.ProjectMemberForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.model.ProjectMemberModel;
import jp.co.nemuzuka.service.ProjectMemberService;

import com.google.appengine.api.datastore.Key;

/**
 * ProjectMemberServiceの実装クラス.
 * @author kazumune
 */
public class ProjectMemberServiceImpl implements ProjectMemberService {

	MemberDao memberDao = MemberDao.getInstance();
	ProjectMemberDao projectMemberDao = ProjectMemberDao.getInstance();
	
	private static ProjectMemberServiceImpl impl = new ProjectMemberServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static ProjectMemberServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private ProjectMemberServiceImpl(){}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectMemberService#getProjectMemberModelList(java.lang.String)
	 */
	@Override
	public List<ProjectMemberModelEx> getProjectMemberModelList(
			String selectedProject) {
		//全てのメンバー情報を取得
		List<MemberModel> memberList = memberDao.getAllList();
		
		//指定プロジェクトに紐付くプロジェクトメンバー情報を取得
		List<ProjectMemberModel> projectMemberList = projectMemberDao.getList(selectedProject, null);
		Map<Key, ProjectMemberModel> projectMemberMap = createProjectMemberMap(projectMemberList);
		return createMemberList(memberList, projectMemberMap);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectMemberService#getProjectMemberOnlyModelList(java.lang.String)
	 */
	@Override
	public List<ProjectMemberModelEx> getProjectMemberOnlyModelList(
			String selectedProject) {
		//指定プロジェクトに紐付くプロジェクトメンバー情報を取得
		List<ProjectMemberModel> projectMemberList = projectMemberDao.getList(selectedProject, null);
		Map<Key, ProjectMemberModel> projectMemberMap = createProjectMemberMap(projectMemberList);
		
		//プロジェクトメンバーの情報を取得
		Set<Key> memberKeySet = new LinkedHashSet<Key>();
		for(ProjectMemberModel target : projectMemberList) {
			memberKeySet.add(target.getMemberKey());
		}
		List<MemberModel> memberList = null;
		if(memberKeySet.size() != 0) {
			memberList = memberDao.getList(memberKeySet.toArray(new Key[0]));
		} else {
			memberList = new ArrayList<MemberModel>();
		}
		return createMemberList(memberList, projectMemberMap);
	}

	
	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.ProjectMemberService#updateProjectMember(java.lang.String, jp.co.nemuzuka.form.ProjectMemberForm)
	 */
	@Override
	public void updateProjectMember(String selectedProject,
			ProjectMemberForm form) {
		
		//プロジェクトKey文字列に紐付くプロジェクトメンバー情報を全件削除
		List<ProjectMemberModel> list = projectMemberDao.getList(selectedProject, "");
		Set<Key> projectMemberKeySet = new LinkedHashSet<Key>();
		for(ProjectMemberModel target : list) {
			projectMemberKeySet.add(target.getKey());
		}
		if(projectMemberKeySet.size() != 0) {
			projectMemberDao.delete(projectMemberKeySet.toArray(new Key[0]));
		}

		//form情報を元に更新
		int index = 0;
		Key projectKey = Datastore.stringToKey(selectedProject);
		for(String target : form.memberKeyArray) {
			
			ProjectMemberModel model = new ProjectMemberModel();
			Key memberKey = Datastore.stringToKey(target);
			model.createKey(projectKey, memberKey);
			ProjectAuthority projectAuthority = ProjectAuthority.fromCode(form.authorityCodeArray[index]);
			if(projectAuthority == null) {
				projectAuthority = ProjectAuthority.type3;
			}
			model.setProjectAuthority(projectAuthority);
			projectMemberDao.put(model);
			index++;
		}
	}

	/**
	 * プロジェクトメンバーMap生成.
	 * KeyはメンバーKey,valueは該当データを設定します。
	 * @param projectMemberList 取得List
	 * @return プロジェクトメンバーMap
	 */
	private Map<Key, ProjectMemberModel> createProjectMemberMap(
			List<ProjectMemberModel> projectMemberList) {
		
		Map<Key, ProjectMemberModel> map = new HashMap<Key, ProjectMemberModel>();
		for(ProjectMemberModel target : projectMemberList) {
			map.put(target.getMemberKey(), target);
		}
		return map;
	}

	/**
	 * プロジェクトメンバーデータList生成.
	 * 引数の情報を元にプロジェクトメンバーListを生成します。
	 * メールアドレスは空文字に設定します。
	 * @param memberList メンバーList
	 * @param projectMemberMap プロジェクトメンバーMap
	 * @return プロジェクトメンバーデータList
	 */
	private List<ProjectMemberModelEx> createMemberList(
			List<MemberModel> memberList, Map<Key, ProjectMemberModel> projectMemberMap) {
		List<ProjectMemberModelEx> result = new ArrayList<ProjectMemberModelEx>();
		for(MemberModel target : memberList) {
			ProjectMemberModelEx entity = new ProjectMemberModelEx();
			entity.setMember(target);
			
			ProjectMemberModel model = projectMemberMap.get(target.getKey());
			if(model != null) {
				//プロジェクトメンバーである、と設定されているメンバーの場合
				entity.setProjectMember(true);
				if(model.getProjectAuthority() != null ) {
					entity.setAuthorityCode(model.getProjectAuthority().getCode());
					entity.setAuthorityName(model.getProjectAuthority().getLabel());
				}
			}
			//メールアドレスは空文字に置き換える
			target.setMail("");
			result.add(entity);
		}
		return result;
	}
}
