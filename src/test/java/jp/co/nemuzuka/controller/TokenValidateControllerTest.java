package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import jp.co.nemuzuka.form.TestForm;
import net.sf.json.JSONObject;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class TokenValidateControllerTest extends ControllerTestCase {

	/**
	 * 通常テスト.
	 * validateあり。エラー無し。
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		HttpServletRequest request = tester.request;
		HttpSession session = request.getSession();
		session.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("dummy", "123");
		request.setAttribute("memo", "メモです。");
		tester.start("/tokenValidate");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		JSONObject actual = JSONObject.fromObject(tester.response.getOutputAsString());
		assertThat(actual.toString(), is("{\"errorMsg\":[],\"infoMsg\":[],\"result\":null,\"status\":0}"));
		
		TokenValidateController controller = tester.getController();
		TestForm testForm = controller.testForm;
		assertThat(testForm.getDummy(), is("123"));
		assertThat(testForm.getMemo(), is("メモです。"));
	}

	/**
	 * 通常テスト.
	 * validateあり。エラーあり。
	 */
	@Test
	public void validateErrorTest() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		HttpServletRequest request = tester.request;
		HttpSession session = request.getSession();
		session.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("jp.co.nemuzuka.token", "123");
		request.setAttribute("dummy", "a");
		tester.start("/tokenValidate");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		JSONObject actual = JSONObject.fromObject(tester.response.getOutputAsString());
		assertThat(actual.toString(), is("{\"errorMsg\":[\"ダミーは整数でなければいけません。\"],\"infoMsg\":[],\"result\":-1,\"status\":0}"));
	}

	/* (非 Javadoc)
	 * @see org.slim3.tester.ControllerTestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();
		tester.response.flushBuffer();
	}
}
