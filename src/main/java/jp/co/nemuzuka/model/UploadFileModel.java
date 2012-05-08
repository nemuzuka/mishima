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
package jp.co.nemuzuka.model;

import java.util.Date;

import net.arnx.jsonic.JSONHint;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.Model;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

/**
 * アップロードされたファイルを管理するModel.
 * @author kazumune
 */
@Model(schemaVersion = 1)
public class UploadFileModel extends AbsModel {

	/** アップロードファイルKey. */
	@Attribute(primaryKey=true)
	private Key key;

	/** 親Key. */
	//今回は、TicketのKey
	private Key parentKey;
	
	/** プロジェクトKey. */
	//プロジェクトのKey
	private Key projectKey;
	
	/** blobKey. */
	private String blobKey;
	
	/** ファイル名. */
	private String filename;
	
	/** contentType. */
	private String contentType;
	
	/** ファイルサイズ. */
	private Long size;

	/** 登録日時. */
	private Date creation;
	
	/** ハッシュ値. */
	private String md5Hash;

	/**
	 * @return key
	 */
	@JSONHint(ignore=true)
	public Key getKey() {
		return key;
	}

	/**
	 * @param key the key to set
	 */
	public void setKey(Key key) {
		this.key = key;
	}

	/**
	 * @return the blobKey
	 */
	@JSONHint(ignore=true)
	public String getBlobKey() {
		return blobKey;
	}

	/**
	 * @param blobKey the blobKey to set
	 */
	public void setBlobKey(String blobKey) {
		this.blobKey = blobKey;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * @param filename the filename to set
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * @return the contentType
	 */
	@JSONHint(ignore=true)
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the size
	 */
	public Long getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(Long size) {
		this.size = size;
	}

	/**
	 * @return the creation
	 */
	@JSONHint(ignore=true)
	public Date getCreation() {
		return creation;
	}

	/**
	 * @param creation the creation to set
	 */
	public void setCreation(Date creation) {
		this.creation = creation;
	}

	/**
	 * @return the md5Hash
	 */
	@JSONHint(ignore=true)
	public String getMd5Hash() {
		return md5Hash;
	}

	/**
	 * @param md5Hash the md5Hash to set
	 */
	public void setMd5Hash(String md5Hash) {
		this.md5Hash = md5Hash;
	}

	/**
	 * @return the parentKey
	 */
	@JSONHint(ignore=true)
	public Key getParentKey() {
		return parentKey;
	}

	/**
	 * @param parentKey the parentKey to set
	 */
	public void setParentKey(Key parentKey) {
		this.parentKey = parentKey;
	}

	/**
	 * @return the projectKey
	 */
	@JSONHint(ignore=true)
	public Key getProjectKey() {
		return projectKey;
	}

	/**
	 * @param projectKey the projectKey to set
	 */
	public void setProjectKey(Key projectKey) {
		this.projectKey = projectKey;
	}

	/**
	 * イメージURL.
	 * このURLを使用することで、ファイルをダウンロードできます。
	 * @return イメージURL
	 */
	public String getImageURL() {
		BlobKey blobKey = new BlobKey(this.blobKey);
		ImagesService imagesService = ImagesServiceFactory.getImagesService();
		return imagesService.getServingUrl(blobKey);
	}
}
