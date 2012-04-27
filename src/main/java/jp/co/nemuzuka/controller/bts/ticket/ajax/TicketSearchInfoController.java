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
package jp.co.nemuzuka.controller.bts.ticket.ajax;

import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.entity.TicketMstEntity.TicketMst;
import jp.co.nemuzuka.form.TicketSearchForm;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

/**
 * Ticket検索条件取得Controller.
 * @author kazumune
 */
public class TicketSearchInfoController extends JsonController {

	TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	@ProjectMember
	protected Object execute() throws Exception {
		
		JsonResult result = new JsonResult();
		TicketSearchForm form = new TicketSearchForm();
		form.status = new String[]{TicketMst.NO_FINISH};
		form.ticketMst = ticketMstService.getTicketMst(getUserInfo().selectedProject);
		result.setResult(form);
		return result;
	}

}
