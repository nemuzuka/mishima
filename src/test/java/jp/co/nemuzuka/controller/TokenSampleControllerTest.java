package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class TokenSampleControllerTest extends ControllerTestCase {

	/**
	 * 通常テスト.
	 * TokenCheckあり。エラー無し。
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		HttpServletRequest request = tester.request;
		HttpSession session = request.getSession();
		session.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("jp.co.nemuzuka.token", "123");
		tester.start("/tokenSample");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual.toString(), is("{\"errorMsg\":[],\"infoMsg\":[],\"result\":null,\"status\":0}"));
	}

	/**
	 * 通常テスト.
	 * TokenCheckあり。エラーあり。
	 */
	@Test
	public void validateErrorTest() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		HttpServletRequest request = tester.request;
		HttpSession session = request.getSession();
		session.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("jp.co.nemuzuka.token", "1234");
		tester.start("/tokenSample");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual.toString(), is("{\"errorMsg\":[\"ブラウザの戻るボタンが押された可能性があります。もう一度操作してください。\"],\"infoMsg\":[],\"result\":null,\"status\":-2}"));
	}
}
