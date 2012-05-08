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

import java.util.ArrayList;
import java.util.List;

import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.service.UploadFileService;
import jp.co.nemuzuka.service.impl.UploadFileServiceImpl;

import org.apache.commons.lang.StringUtils;
import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;
import org.slim3.util.ApplicationMessage;

/**
 * アップロードCheckController.
 * @author kazumune
 */
public class UploadCheckController extends HtmlController {
	
	protected UploadFileService uploadFileService = UploadFileServiceImpl.getInstance();
	
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@ProjectMember
	@Override
	protected Navigation execute() throws Exception {
		List<String> errorMsgs = new ArrayList<String>();
		boolean isError = false;
		
		//Tokenチェックを行う
		if(isTokenCheck() == false) {
			errorMsgs.add(ApplicationMessage.get("errors.token"));
			isError = true;
		} else {
			//リクエストパラメータの必須項目チェック
			FileItem fileItem = requestScope("uploadFile");
			//チケットのKey情報を取得
			String keyToString = asString("keyToString");
			if(fileItem == null) {
				String uploadFileName = ApplicationMessage.get("label.uploadFile");
				errorMsgs.add(ApplicationMessage.get("validator.required", uploadFileName));
				isError = true;
			}
			if(StringUtils.isEmpty(keyToString)) {
				errorMsgs.add(ApplicationMessage.get("validator.required", "keyToString"));
				isError = true;
			}
		}
		requestScope("errorMsgs", errorMsgs);
		requestScope("isError", isError);
		requestScope("token", setToken());
		
		return forward("/bts/ticket/uploadFileCheck.jsp");
	}
}
