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
package jp.co.nemuzuka.service.impl;

import java.nio.ByteBuffer;
import java.util.ConcurrentModificationException;
import java.util.List;

import jp.co.nemuzuka.dao.UploadFileDao;
import jp.co.nemuzuka.model.UploadFileModel;
import jp.co.nemuzuka.service.UploadFileService;

import org.apache.commons.lang.StringUtils;
import org.slim3.controller.upload.FileItem;
import org.slim3.datastore.Datastore;
import org.slim3.util.BeanUtil;
import org.slim3.util.CopyOptions;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

/**
 * UploadFileServiceの実装クラス.
 * @author k-katagiri
 */
public class UploadFileServiceImpl implements UploadFileService {

	UploadFileDao uploadFileDao = UploadFileDao.getInstance();
	
	private static UploadFileServiceImpl impl = new UploadFileServiceImpl();
	
	/**
	 * インスタンス取得.
	 * @return インスタンス
	 */
	public static UploadFileServiceImpl getInstance() {
		return impl;
	}
	
	/**
	 * デフォルトコンストラクタ.
	 */
	private UploadFileServiceImpl(){}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.UploadFileService#put(org.slim3.controller.upload.FileItem, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String put(FileItem fileItem, String comment, 
			String ticketKeyToString, String projectKeyString) {
		
		BlobKey blobKey = null;
		try {
			FileService fileService = FileServiceFactory.getFileService();
			AppEngineFile blobFile =
					fileService.createNewBlobFile(fileItem.getContentType());
			FileWriteChannel writeChannel =
			    fileService.openWriteChannel(blobFile, true);
			writeChannel.write(ByteBuffer.wrap(fileItem.getData()));
			writeChannel.closeFinally();
			blobKey = fileService.getBlobKey(blobFile);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		//登録するデータを設定
		UploadFileModel model = new UploadFileModel();
		BlobInfoFactory factory = new BlobInfoFactory();
		model.setBlobKey(blobKey.getKeyString());
		BlobInfo blobInfo = factory.loadBlobInfo(blobKey);
        BeanUtil.copy(blobInfo, model, new CopyOptions().exclude("blobKey"));
		model.setParentKey(Datastore.stringToKey(ticketKeyToString));
		model.setProjectKey(Datastore.stringToKey(projectKeyString));
		model.setFilename(fileItem.getFileName());
		model.setComment(new Text(StringUtils.defaultString(comment)));
        
		//登録
		Datastore.put(model);
		return Datastore.keyToString(model.getKey());
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.UploadFileService#delete(java.lang.String, java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void delete(String uploadFileKeyString, String ticketKeyToString,
			String projectKeyString, Long version) {
		
		//該当レコード取得
		UploadFileModel model = uploadFileDao.get(uploadFileKeyString, ticketKeyToString, 
				projectKeyString, version);
		if(model == null) {
			//該当レコードが存在しない場合、Exception
			throw new ConcurrentModificationException();
		}
		delete(model);
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.UploadFileService#getList(java.lang.String, java.lang.String)
	 */
	@Override
	public List<UploadFileModel> getList(String ticketKeyToString,
			String projectKeyString) {
		return uploadFileDao.getList(ticketKeyToString, projectKeyString);
	}

	/* (非 Javadoc)
	 * @see jp.co.nemuzuka.service.UploadFileService#get(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public UploadFileModel get(String keyString, String ticketKeyToString,
			String projectKeyString) {
		Key key = Datastore.stringToKey(keyString);
		Key parentKey = Datastore.stringToKey(ticketKeyToString);
		Key projectKey = Datastore.stringToKey(projectKeyString);
		UploadFileModel model = uploadFileDao.get(key);
		if(model == null || 
				model.getParentKey().equals(parentKey) == false ||
				model.getProjectKey().equals(projectKey) == false) {
			return null;
		}
		return model;
	}

	/* (non-Javadoc)
	 * @see jp.co.nemuzuka.service.UploadFileService#delete4ticketKeyString(java.lang.String, java.lang.String)
	 */
	@Override
	public void delete4ticketKeyString(String ticketKeyToString,
			String projectKeyString) {
		//該当レコードを全て取得
		List<UploadFileModel> list = getList(ticketKeyToString, projectKeyString);
		for(UploadFileModel target : list) {
			//該当レコードに紐付くデータを削除
			delete(target);
		}
	}

	/**
	 * データ削除.
	 * Modelとblobデータを削除します。
	 * @param model 削除対象Model
	 */
	void delete(UploadFileModel model) {
		//blobKeyから削除し、Entityから削除
		BlobKey blobKey = new BlobKey(model.getBlobKey());
        BlobstoreService bs = BlobstoreServiceFactory.getBlobstoreService();
        bs.delete(blobKey);
		
		uploadFileDao.delete(model.getKey());
	}
}
