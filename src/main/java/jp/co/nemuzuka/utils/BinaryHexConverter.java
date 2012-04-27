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
package jp.co.nemuzuka.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 16進文字列変換Utils.
 * @author k-katagiri
 */
public class BinaryHexConverter {

	/**
	 * byte型配列から16進数表記文字列へ変換する
	 * @param fromByte 変換対象Byte型配列
	 * @return 16進数表記に変換後の文字列(変換対象byte配列がnullの場合、null)
	 */
	public static String bytesToHexString(byte[] fromByte) {

		if(fromByte == null) {
			return null;
		}

		StringBuilder hexStrBuilder = new StringBuilder();

		for (int i = 0; i < fromByte.length; i++) {
			// 16進数表記で1桁数値だった場合、2桁目を0で埋める
			if ((fromByte[i] & 0xff) < 0x10) {
				hexStrBuilder.append("0");
			}
			hexStrBuilder.append(Integer.toHexString(0xff & fromByte[i]).toUpperCase());
		}
		return hexStrBuilder.toString();
	}

	/**
	 * 16進数表記文字列からByte型配列へ変換する
	 * @param fromHexStr 変換対象の16進数表記文字列
	 * @return 変換後のByte型配列(変換対象の16進数表記文字列がnullか空文字の場合、null)
	 */
	public static byte[] hexStringToBytes(String fromHexStr) {

		if(StringUtils.isEmpty(fromHexStr)) {
			return null;
		}

		//16進数表記では2文字で1バイトを表現するため、
		//Byte型配列に変換する際には、配列の長さは1/2で良い
		byte[] toByte = new byte[fromHexStr.length() / 2];

		//16進数表記文字列を、2文字ずつByte型へ変換していく
		for (int i = 0; i < toByte.length; i++) {
			toByte[i] = (byte) Integer.parseInt(fromHexStr.substring(i * 2, (i + 1) * 2), 16);
		}
		return toByte;
	}
}

