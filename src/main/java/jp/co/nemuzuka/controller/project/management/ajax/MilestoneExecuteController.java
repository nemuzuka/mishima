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
package jp.co.nemuzuka.controller.project.management.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectAdmin;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.annotation.TokenCheck;
import jp.co.nemuzuka.core.annotation.Validation;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.MilestoneForm;
import jp.co.nemuzuka.service.MilestoneService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.MilestoneServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;
import jp.co.nemuzuka.utils.ConvertUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.slim3.controller.validator.Validators;
import org.slim3.util.ApplicationMessage;

/**
 * マイルストーン登録・更新Controller.
 * @author kazumune
 */
public class MilestoneExecuteController extends JsonController {

	/** ActionForm. */
	@ActionForm
	protected MilestoneForm form;
	
	protected MilestoneService milestoneService = MilestoneServiceImpl.getInstance();
	protected TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@TokenCheck
	@ProjectAdmin
	@ProjectMember
	@Validation(method="validate", input="jsonError")
	protected Object execute() throws Exception {
		
		JsonResult errorResult = validator();
		if(errorResult != null) {
			return errorResult;
		}

		
		//登録・更新する
		milestoneService.put(form, getUserInfo().selectedProject);
		ticketMstService.initRefreshStartTime(getUserInfo().selectedProject);

		JsonResult result = new JsonResult();
		result.getInfoMsg().add(ApplicationMessage.get("info.success"));
		return result;
	}

	/**
	 * 相関チェック.
	 * @return エラーが存在する場合、JsonResultのインスタンス。エラーが存在しない場合、null
	 */
	private JsonResult validator() {
		
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		Date startDate = ConvertUtils.toDate(form.startDate, sdf);
		Date endDate = ConvertUtils.toDate(form.endDate, sdf);
		if(startDate != null && endDate != null) {
			long startTime = startDate.getTime();
			long endTime = endDate.getTime();
			if(startTime > endTime) {
				return createErrorMsg("validator.check.between", "終了日","開始日","日付");
			}
		}
		return null;
	}

	/**
	 * validate設定.
	 * @return validate
	 */
	protected Validators validate() {
		Validators v = new Validators(request);
		v.add("milestoneName", v.required(), v.maxlength(128));
		v.add("startDate", v.dateType("yyyyMMdd"));
		v.add("endDate", v.dateType("yyyyMMdd"));
		return v;
	}
}
