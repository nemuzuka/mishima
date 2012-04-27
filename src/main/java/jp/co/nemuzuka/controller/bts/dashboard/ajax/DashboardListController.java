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
package jp.co.nemuzuka.controller.bts.dashboard.ajax;

import org.apache.commons.lang.StringUtils;

import jp.co.nemuzuka.core.controller.JsonController;
import jp.co.nemuzuka.core.entity.JsonResult;
import jp.co.nemuzuka.service.DashboardService;
import jp.co.nemuzuka.service.TicketMstService;
import jp.co.nemuzuka.service.impl.DashboardServiceImpl;
import jp.co.nemuzuka.service.impl.TicketMstServiceImpl;

/**
 * ダッシュボード情報取得Contoroller.
 * @author kazumune
 */
public class DashboardListController extends JsonController {

	protected DashboardService dashboardService = DashboardServiceImpl.getInstance();
	protected TicketMstService ticketMstService = TicketMstServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.JsonController#execute()
	 */
	@Override
	protected Object execute() throws Exception {
		
		String selectedProject = getUserInfo().selectedProject;
		String[] openStatus = new String[0];
		if(StringUtils.isNotEmpty(selectedProject)) {
			openStatus = ticketMstService.getTicketMst(selectedProject).openStatus;
		}
		
		JsonResult result = new JsonResult();
		result.setToken(this.setToken());
		result.setResult(
				dashboardService.getDashboardInfo(userService.getCurrentUser().getEmail(), 
						selectedProject, openStatus, getUserInfo().dashboardLimitCnt));
		return result;
	}
}
