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
