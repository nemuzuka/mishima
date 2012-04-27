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
 * HtmlStringUtilsのテストクラス.
 * @author kkatagiri
 */
public class HtmlStringUtilsTest {


	/**
	 * escapeTextAreaStringのテスト.
	 */
	@Test
	public void testEscapeTextAreaString() {

		String target = "hoge";
		String actual = HtmlStringUtils.escapeTextAreaString(target);
		assertThat(actual, is("hoge"));

		target = "<input type=\"text\">";
		actual = HtmlStringUtils.escapeTextAreaString(target);
		assertThat(actual, is("&lt;input type=&quot;text&quot;&gt;"));

		target = "<input type='text'>";
		actual = HtmlStringUtils.escapeTextAreaString(target);
		assertThat(actual, is("&lt;input type=&#39;text&#39;&gt;"));

		target = "リロ & スティッチ";
		actual = HtmlStringUtils.escapeTextAreaString(target);
		assertThat(actual, is("リロ &amp; スティッチ"));

	}

	/**
	 * createAddressのテスト
	 */
	@Test
	public void testCreateAddress() {
		String actual = HtmlStringUtils.createAddress("a","b","c","d","e");
		assertThat(actual, is("a<br />b c d e "));

		actual = HtmlStringUtils.createAddress("","b","c","","e");
		assertThat(actual, is("b c e "));
	}

}
