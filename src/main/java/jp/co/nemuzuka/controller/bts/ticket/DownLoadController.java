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

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.text.SimpleDateFormat;

import jp.co.nemuzuka.core.annotation.ProjectMember;
import jp.co.nemuzuka.core.controller.HtmlController;
import jp.co.nemuzuka.core.entity.GlobalTransaction;
import jp.co.nemuzuka.core.entity.TransactionEntity;
import jp.co.nemuzuka.model.UploadFileModel;
import jp.co.nemuzuka.service.UploadFileService;
import jp.co.nemuzuka.service.impl.UploadFileServiceImpl;
import jp.co.nemuzuka.utils.CurrentDateUtils;
import jp.co.nemuzuka.utils.DateTimeUtils;

import org.slim3.controller.Navigation;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;

/**
 * ダウンロードController.
 * @author kazumune
 */
public class DownLoadController extends HtmlController {
	
	protected UploadFileService uploadFileService = UploadFileServiceImpl.getInstance();
	
	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.core.controller.HtmlController#execute()
	 */
	@ProjectMember
	@Override
	protected Navigation execute() throws Exception {
		
		String keyString = asString("keyString");
		String ticketKeyToString = asString("ticketKeyString");
		UploadFileModel model = uploadFileService.get(keyString, ticketKeyToString, 
				getUserInfo().selectedProject);

		//トランザクションは終了させておく
		TransactionEntity entity = GlobalTransaction.transaction.get();
		if(entity != null) {
			entity.rollback();
		}

		if(model != null) {
			//ファイルを取得し、レスポンスに書き込む
			writeOutputStream(model);
		}
		return null;
	}

	/**
	 * ファイル書き込み.
	 * blobKey情報よりファイル情報を取得し、レスポンスに書き込みます。
	 * @param model uploadFileインスタンス
	 * @throws Exception 例外
	 */
	private void writeOutputStream(UploadFileModel model) throws Exception {
		FileService fileService = FileServiceFactory.getFileService();
		BlobKey blobKey = new BlobKey(model.getBlobKey());
		AppEngineFile blobFile = fileService.getBlobFile(blobKey);
		FileReadChannel fileRead = fileService.openReadChannel(blobFile, false);

		InputStream istream = Channels.newInputStream(fileRead);
		OutputStream os = response.getOutputStream();
		String tmpFile = model.getFilename();
		//最初の「.」までの文字を削除し、download-システム日時 + 「拡張子」の文字列に変換する
		int index = tmpFile.indexOf(".");
		if(index != -1) {
			String suffix = tmpFile.substring(index);
			SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd-HHmmss");
			tmpFile = "download-" + sdf.format(CurrentDateUtils.getInstance().getCurrentDateTime()) + suffix;
		}
		
		response.setHeader("Content-Disposition", "inline;filename=\""+ tmpFile + "\"");
		response.setContentType("application/octet-stream");
		response.setContentLength(model.getSize().intValue());

		int cnt =0 ;
		byte [] buf = new byte[4096];
		while ( (cnt = istream.read(buf, 0, buf.length)) > 0 ){
			os.write(buf, 0, cnt);
		}
		istream.close();
		os.flush();
		os.close();
	}
}
