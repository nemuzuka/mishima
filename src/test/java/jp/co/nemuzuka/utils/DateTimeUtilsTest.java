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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Test;

/**
 * DateTimeUtilsのテストクラス.
 * @author kkatagiri
 */
public class DateTimeUtilsTest {


	/**
	 * getStartEndDateのテスト
	 * @throws ParseException
	 */
	@Test
	public void testGetStartEndDate() throws ParseException{
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");

		List<Date> list = DateTimeUtils.getStartEndDate("201102");
		assertThat(list.get(0), is(sdf.parse("20110201")));
		assertThat(list.get(1), is(sdf.parse("20110228")));

		list = DateTimeUtils.getStartEndDate("201101");
		assertThat(list.get(0), is(sdf.parse("20110101")));
		assertThat(list.get(1), is(sdf.parse("20110131")));

		list = DateTimeUtils.getStartEndDate("201202");
		assertThat(list.get(0), is(sdf.parse("20120201")));
		assertThat(list.get(1), is(sdf.parse("20120229")));

		try {
			DateTimeUtils.getStartEndDate("201113");
			fail();
		} catch(RuntimeException e) {}
	}

	/**
	 * getStartEndDate4SunDayのテスト
	 * @throws ParseException
	 */
	@Test
	public void testGetStartEndDate4SunDay() throws ParseException{
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		List<Date> list = DateTimeUtils.getStartEndDate4SunDay("201110");
		assertThat(list.get(0), is(sdf.parse("20110925")));
		assertThat(list.get(1), is(sdf.parse("20111105")));

		list = DateTimeUtils.getStartEndDate4SunDay("201201");
		assertThat(list.get(0), is(sdf.parse("20120101")));
		assertThat(list.get(1), is(sdf.parse("20120204")));

		list = DateTimeUtils.getStartEndDate4SunDay("201112");
		assertThat(list.get(0), is(sdf.parse("20111127")));
		assertThat(list.get(1), is(sdf.parse("20111231")));

	}

	/**
	 * getStartEndDateListのテスト.
	 * @throws ParseException
	 */
	@Test
	public void testGetStartEndDateList() throws ParseException {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		List<Date> list = DateTimeUtils.getStartEndDateList("201102");
		assertThat(list.size(), is(28));
		assertThat(list.get(0), is(sdf.parse("20110201")));
		assertThat(list.get(1), is(sdf.parse("20110202")));
		assertThat(list.get(27), is(sdf.parse("20110228")));
	}

	/**
	 * getStartEndDate4SunDayListのテスト.
	 * @throws ParseException
	 */
	@Test
	public void testGetStartEndDate4SunDayList() throws ParseException {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		List<Date> list = DateTimeUtils.getStartEndDate4SunDayList("201112");
		assertThat(list.size(), is(35));
		assertThat(list.get(0), is(sdf.parse("20111127")));
		assertThat(list.get(1), is(sdf.parse("20111128")));
		assertThat(list.get(34), is(sdf.parse("20111231")));
	}

	/**
	 * getDaysのテスト.
	 * @throws ParseException
	 */
	@Test
	public void testGetDays() throws ParseException {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		Date startDate = sdf.parse("20100101");
		Date endDate = sdf.parse("20100101");
		assertThat(DateTimeUtils.getDays(startDate, endDate), is(1));

		endDate = sdf.parse("20100102");
		assertThat(DateTimeUtils.getDays(startDate, endDate), is(2));

		endDate = sdf.parse("20100103");
		assertThat(DateTimeUtils.getDays(startDate, endDate), is(3));

	}

	/**
	 * rangeCheckのテスト.
	 */
	@Test
	public void testRangeCheck() {
		assertThat(DateTimeUtils.rangeCheck("", "", "", ""), is(false));
		assertThat(DateTimeUtils.rangeCheck("1010", "1009", "1110", "1109"), is(false));

		//基準が範囲の場合
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1030", "1130"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1100", "1130"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1101", "1130"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "0930", "1030"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "0930", "1000"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "0930", "0959"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1000", "1100"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "0959", "1101"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1001", "1059"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "0959", "0959"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1000", "1000"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1030", "1030"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1100", "1100"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1000", "1100", "1101", "1101"), is(false));

		//基準が点の場合
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1030", "1031"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1031", "1032"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1029", "1030"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1028", "1029"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1029", "1031"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1029", "1029"), is(false));
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1030", "1030"), is(true));
		assertThat(DateTimeUtils.rangeCheck("1030", "1030", "1031", "1031"), is(false));
	}

	/**
	 * addWeekのテスト.
	 * @throws ParseException
	 */
	@Test
	public void testAddWeek() throws ParseException {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");
		assertThat(DateTimeUtils.addWeek(sdf.parse("20111025"), -1).getTime(), is(sdf.parse("20111018").getTime()));
		assertThat(DateTimeUtils.addWeek(sdf.parse("20111025"), 1).getTime(), is(sdf.parse("20111101").getTime()));
	}

	/**
	 * calcAgeのテスト.
	 */
	@Test
	public void testCalcAge() throws ParseException {
		SimpleDateFormat sdf = DateTimeUtils.createSdf("yyyyMMdd");

		Date baseDate = sdf.parse("20120106");
		Date birthDay = sdf.parse("19770106");
		assertThat(DateTimeUtils.calcAge(birthDay, baseDate), is(35));

		baseDate = sdf.parse("20120107");
		assertThat(DateTimeUtils.calcAge(birthDay, baseDate), is(35));

		baseDate = sdf.parse("20120105");
		assertThat(DateTimeUtils.calcAge(birthDay, baseDate), is(34));

	}

}
