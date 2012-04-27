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
 * DecimalCheckerのテストクラス.
 * @author kkatagiri
 */
public class DecimalCheckerTest {

	/**
	 * checkScaleのテスト.
	 */
	@Test
	public void testCheckScale() {
		assertThat(DecimalChecker.checkScale("100.00", 2), is(true));
		assertThat(DecimalChecker.checkScale("100.000", 2), is(false));
		assertThat(DecimalChecker.checkScale("100.0", 2), is(true));
	}

	/**
	 * checkIntのテスト.
	 */
	@Test
	public void testCheckInt() {
		assertThat(DecimalChecker.checkInt("100.00", 2), is(false));
		assertThat(DecimalChecker.checkInt("100.00", 3), is(true));
		assertThat(DecimalChecker.checkInt("100.00", 4), is(true));
		assertThat(DecimalChecker.checkInt("-1234567.89012345", 8), is(true));
		assertThat(DecimalChecker.checkInt("-1234567.89012345", 7), is(false));

	}
}
