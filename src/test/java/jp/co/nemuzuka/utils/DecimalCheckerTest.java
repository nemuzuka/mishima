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
