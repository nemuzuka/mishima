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
package jp.co.nemuzuka.controller.bts.gantt.ajax;

import org.slim3.util.ApplicationMessage;

import jp.co.nemuzuka.core.annotation.ActionForm;
import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.form.TicketSearchForm;
import jp.co.nemuzuka.service.GanttService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.GanttServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

/**
 * チャート一覧Contoroller.
 * @author kazumune
 */
public class GanttListController extends JsonController {

	protected GanttService gantService = GanttServiceImpl.getInstance();
	protected TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	
	/** ActionForm. */
	@ActionForm
	protected TicketSearchForm form;

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@ProjectMember
	@Override
	protected Object execute() throws Exception {
		
		form.status = paramValues("status[]");

		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		//検索結果を返す
		GanttService.Result ganttResult = gantService.getList(
				form.createParam(getUserInfo().selectedProject, 
						ticketMstService.getTicketMst(getUserInfo().selectedProject).openStatus),
				getUserInfo().selectedProject);
		result.setResult(ganttResult);

		if(ganttResult.ticketList.size() == 0) {
			result.getInfoMsg().add(ApplicationMessage.get("info.empty"));
		}
		
		return result;
	}
}
