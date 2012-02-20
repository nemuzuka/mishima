package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class Submit2ControllerTest extends ControllerTestCase {

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
