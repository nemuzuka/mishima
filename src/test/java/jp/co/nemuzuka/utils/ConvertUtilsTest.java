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
 * ConvertUtilsのテストクラス.
 * @author kkatagiri
 */
public class ConvertUtilsTest {


	/**
	 * convertのテスト.
	 */
	@Test
	public void testConvertLong() {

		String[] targets = new String[]{"10","3"};
		Long[] actual = ConvertUtils.convert(targets);
		assertThat(actual.length, is(2));
		assertThat(actual[0], is(10L));
		assertThat(actual[1], is(3L));

		targets = null;
		assertThat(ConvertUtils.convert(targets), is(new Long[0]));
	}


	/**
	 * convertのテスト.
	 */
	@Test
	public void testConvertString() {

		Long[] targets = new Long[]{10L,3L};
		String[] actual = ConvertUtils.convert(targets);
		assertThat(actual.length, is(2));
		assertThat(actual[0], is("10"));
		assertThat(actual[1], is("3"));

		targets = null;
		assertThat(ConvertUtils.convert(targets), is(new String[0]));

	}

}
