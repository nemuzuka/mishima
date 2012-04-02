package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import jp.co.nemuzuka.tester.ControllerTestCase4HRD;

import org.junit.Test;

public class Submit2ControllerTest extends ControllerTestCase4HRD {

	/**
	 * 通常テスト.
	 * validate無し。エラー無し。
	 * GAE管理者.
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		
		tester.environment.setEmail("admin@example.com");
		tester.environment.setAdmin(true);

		HttpServletRequest request = tester.request;
		request.setAttribute("dummy", "123");
		tester.start("/submit2");
		Submit2Controller controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.getDestinationPath(), is("/index.jsp"));
	}
}
