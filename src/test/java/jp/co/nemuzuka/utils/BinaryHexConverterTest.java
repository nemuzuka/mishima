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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;


/**
 * BinaryHexConverterのテストクラス.
 * @author kkatagiri
 */
public class BinaryHexConverterTest {


	/**
	 * bytesToHexStringのテスト.
	 */
	@Test
	public void testBytesToHexString() {

		byte[] array = {0x00,0x01,0x0f,0x10, (byte)0xFF};
		assertThat(BinaryHexConverter.bytesToHexString(array), is("00010F10FF"));

	}


	/**
	 * hexStringToBytesのテスト.
	 */
	@Test
	public void testHexStringToBytes() {
		String target = "00010F10FF";
		byte[] array = BinaryHexConverter.hexStringToBytes(target);
		assertThat(array.length, is(5));
		assertThat(array[0], is((byte)0x00));
		assertThat(array[1], is((byte)0x01));
		assertThat(array[2], is((byte)0x0f));
		assertThat(array[3], is((byte)0x10));
		assertThat(array[4], is((byte)0xff));
	}

}
