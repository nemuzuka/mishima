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
import java.util.logging.Level;

import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.service.UploadFileService;
import jp.co.nemuzuka.service.impl.UploadFileServiceImpl;

import org.slim3.controller.Navigation;
import org.slim3.controller.upload.FileItem;
import org.slim3.util.ApplicationMessage;

/**
 * アップロードController.
 * @author kazumune
 */
public class UploadController extends HtmlController {
	
	protected UploadFileService uploadFileService = UploadFileServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@ProjectMember
	@Override
	protected Navigation execute() throws Exception {
		
		List<String> errorMsgs = new ArrayList<String>();
		List<String> infoMsgs = new ArrayList<String>();
		boolean isError = false;

		//トランザクションは終了させておく
		TransactionEntity entity = GlobalTransaction.transaction.get();
		if(entity != null) {
			entity.rollback();
		}
		
		//リクエストパラメータからファイル情報を取得
		FileItem fileItem = requestScope("uploadFile");
		//チケットのKey情報を取得
		String keyToString = asString("keyToString");
		//プロジェクトのKey情報を取得
		String projectKeyString = getUserInfo().selectedProject;
		try {
			uploadFileService.put(fileItem, keyToString, projectKeyString);
			infoMsgs.add(ApplicationMessage.get("info.success"));
		} catch(Exception e) {
			isError = true;
			errorMsgs.add(ApplicationMessage.get("errors.severe"));
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		
		requestScope("infoMsgs", infoMsgs);
		requestScope("errorMsgs", errorMsgs);
		requestScope("isError", isError);

		return forward("/bts/ticket/uploadFile.jsp");
	}
}
