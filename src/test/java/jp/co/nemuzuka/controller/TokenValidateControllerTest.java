package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.nemuzuka.core.entity.UserInfo;
import jp.co.nemuzuka.form.TestForm;
import jp.co.nemuzuka.tester.ControllerTestCase4HRD;

import org.junit.Test;

public class TokenValidateControllerTest extends ControllerTestCase4HRD {

	/**
	 * 通常テスト.
	 * validateあり。エラー無し。
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		HttpServletRequest request = tester.request;
		HttpSession session = request.getSession();
		session.setAttribute("userInfo", new UserInfo());
		session.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("dummy", "123");
		request.setAttribute("memo", "メモです。");
		tester.start("/tokenValidate");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual.toString(), is("{\"errorMsg\":[],\"infoMsg\":[],\"result\":null,\"status\":0,\"token\":null}"));
		
		TokenValidateController controller = tester.getController();
		TestForm testForm = controller.testForm;
		assertThat(testForm.getDummy(), is("123"));
		assertThat(testForm.getMemo(), is("メモです。"));
		
		testForm = controller.testForm2;
		assertThat(testForm, is(nullValue()));
	}

	/**
	 * 通常テスト.
	 * validateあり。エラーあり。
	 */
	@Test
	public void validateErrorTest() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		HttpServletRequest request = tester.request;
		HttpSession session = request.getSession();
		session.setAttribute("userInfo", new UserInfo());
		session.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("dummy", "a");
		tester.start("/tokenValidate");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual.toString(), is("{\"errorMsg\":[\"ダミーは整数でなければいけません。\"],\"infoMsg\":[],\"result\":null,\"status\":-1,\"token\":null}"));
	}
	
	/**
	 * SessionTimeoutテスト.
	 */
	@Test
	public void sesionCheckErrortest() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		HttpServletRequest request = tester.request;
		HttpSession session = request.getSession();
		session.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("dummy", "123");
		request.setAttribute("memo", "メモです。");
		tester.start("/tokenValidate");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual.toString(), is("{\"errorMsg\":[\"一定時間操作されなかったのでタイムアウトしました。\"],\"infoMsg\":[],\"result\":null,\"status\":-99,\"token\":null}"));
	}
	
}
