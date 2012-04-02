package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import jp.co.nemuzuka.tester.ControllerTestCase4HRD;

import org.junit.Test;

public class NoResultControllerTest extends ControllerTestCase4HRD {

	/**
	 * 通常テスト.
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		tester.start("/noResult");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual.toString(), is("{\"errorMsg\":[\"サーバでエラーが発生しました。\"],\"infoMsg\":[],\"result\":null,\"status\":-3,\"token\":null}"));
	}
}
