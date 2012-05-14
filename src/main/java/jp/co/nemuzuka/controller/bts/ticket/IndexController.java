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
package jp.co.nemuzuka.controller.bts.ticket;

import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.form.ProjectForm;
import jp.co.nemuzuka.service.ProjectService;
import jp.co.nemuzuka.service.impl.ProjectServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slim3.controller.Navigation;

/**
 * TicketメインController.
 * @author kazumune
 */
public class IndexController extends HtmlController {
	
	ProjectService projectService = ProjectServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@ProjectMember
	@Override
	protected Navigation execute() throws Exception {
		
		ProjectForm form = projectService.get(getUserInfo().selectedProject);
		if(StringUtils.isNotEmpty(form.keyToString)) {
			requestScope("selectedProjectId", form.projectId + "-");
		}
		return forward("/bts/ticket/ticket.jsp");
	}

}
