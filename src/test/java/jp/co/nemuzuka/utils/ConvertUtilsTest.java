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
