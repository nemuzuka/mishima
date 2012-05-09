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
package jp.co.nemuzuka.service;


import java.util.List;

import jp.co.nemuzuka.model.UploadFileModel;

import org.slim3.controller.upload.FileItem;

/**
 * ファイルアップロードに関するService.
 * @author kazumune
 */
public interface UploadFileService {
	/**
	 * ファイル情報登録.
	 * 引数の情報でアップロード対象データを登録します。
	 * ※トランザクションは終了させておく必要があります。
	 * @param fileItem FileItemインスタンス
	 * @param comment コメント
	 * @param ticketKeyToString チケットKey文字列
	 * @param projectKeyString プロジェクトKey文字列
	 * @return 登録後Key文字列
	 */
	String put(FileItem fileItem, String comment, String ticketKeyToString, String projectKeyString);
	
	/**
	 * ファイル情報削除.
	 * 引数の情報でアップロード対象データを削除します。
	 * @param uploadFileKeyString ファイルKey文字列
	 * @param ticketKeyToString チケットKey文字列
	 * @param projectKeyString プロジェクトKey文字列
	 * @param version バージョンNo
	 */
	void delete(String uploadFileKeyString, String ticketKeyToString, String projectKeyString, Long version);
	
	/**
	 * ファイル情報削除.
	 * 引数の情報でアップロード対象データを削除します。
	 * @param ticketKeyToString チケットKey文字列
	 * @param projectKeyString プロジェクトKey文字列
	 */
	void delete4ticketKeyString(String ticketKeyToString, String projectKeyString);
	
	/**
	 * ファイル情報取得.
	 * 引数の情報でアップロードファイル情報を取得します。
	 * @param keyString アップロードKey文字列
	 * @param ticketKeyToString チケットKey文字列
	 * @param projectKeyString プロジェクトKey文字列
	 * @return 該当データ（存在しない場合、null）
	 */
	UploadFileModel get(String keyString, String ticketKeyToString, String projectKeyString);
	
	/**
	 * ファイル一覧取得.
	 * 引数に合致するファイル一覧情報を取得します。
	 * @param ticketKeyToString チケットKey文字列
	 * @param projectKeyString プロジェクトKey文字列
	 * @return ファイル一覧情報
	 */
	List<UploadFileModel> getList(String ticketKeyToString, String projectKeyString);
}
