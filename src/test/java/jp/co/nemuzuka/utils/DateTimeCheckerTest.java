package jp.co.nemuzuka.utils;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * DateTimeCheckerのテストクラス.
 * @author kkatagiri
 */
public class DateTimeCheckerTest {

	/**
	 * isDayのテスト.
	 */
	@Test
	public void testIsDay() {
		assertThat(DateTimeChecker.isDay("20100101"), is(true));
		assertThat(DateTimeChecker.isDay("20101231"), is(true));
		assertThat(DateTimeChecker.isDay("20101302"), is(false));
		assertThat(DateTimeChecker.isDay("20100230"), is(false));
		assertThat(DateTimeChecker.isDay("201102"), is(false));
		assertThat(DateTimeChecker.isDay("201102010"), is(false));
		assertThat(DateTimeChecker.isDay("abc"), is(false));
		assertThat(DateTimeChecker.isDay("2010130a"), is(false));
	}

	/**
	 * isMonthのテスト.
	 */
	@Test
	public void testIsMonth() {
		assertThat(DateTimeChecker.isMonth("201001"), is(true));
		assertThat(DateTimeChecker.isMonth("201012"), is(true));
		assertThat(DateTimeChecker.isMonth("20100101"), is(false));
		assertThat(DateTimeChecker.isMonth("201013"), is(false));
		assertThat(DateTimeChecker.isMonth("2011"), is(false));
		assertThat(DateTimeChecker.isMonth("abc"), is(false));
		assertThat(DateTimeChecker.isMonth("20101a"), is(false));
	}

	/**
	 * isHourMinuteのテスト.
	 */
	@Test
	public void testIsHourMinute() {
		assertThat(DateTimeChecker.isHourMinute("0000"), is(true));
		assertThat(DateTimeChecker.isHourMinute("0115"), is(true));
		assertThat(DateTimeChecker.isHourMinute("1030"), is(true));
		assertThat(DateTimeChecker.isHourMinute("2345"), is(true));
		assertThat(DateTimeChecker.isHourMinute("2500"), is(false));
		assertThat(DateTimeChecker.isHourMinute("0001"), is(true));
		assertThat(DateTimeChecker.isHourMinute("105"), is(false));
		assertThat(DateTimeChecker.isHourMinute(""), is(false));
		assertThat(DateTimeChecker.isHourMinute("100a"), is(false));
	}

}
