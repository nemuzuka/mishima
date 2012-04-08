package jp.co.nemuzuka.controller.management.ajax;

import java.util.List;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.SystemManager;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.MemberSearchForm;
import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.service.MemberService;
import jp.co.nemuzuka.service.impl.MemberServiceImpl;

import org.slim3.util.ApplicationMessage;

/**
 * メンバー検索Contoroller.
 * @author kazumune
 */
public class MemberListController extends JsonController {

	protected MemberService memberService = new MemberServiceImpl();
	
	/** ActionForm. */
	@ActionForm
	protected MemberSearchForm form;
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@SystemManager
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		List<MemberModel> list = memberService.getList(form.name, form.mail);
		result.setResult(list);
		
		if(list.size() == 0) {
			result.getInfoMsg().add(ApplicationMessage.get("info.empty"));
		}
		
		return result;
	}

}
