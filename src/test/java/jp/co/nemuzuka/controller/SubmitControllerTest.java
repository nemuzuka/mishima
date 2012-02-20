package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import jp.co.nemuzuka.model.MemberModel;

import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.ControllerTestCase;

public class SubmitControllerTest extends ControllerTestCase {

	/**
	 * 通常テスト.
	 * validateあり。エラー無し。
	 * GAE管理者.
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		
		tester.environment.setEmail("admin@example.com");
		tester.environment.setAdmin(true);

		HttpServletRequest request = tester.request;
		request.setAttribute("dummy", "123");
		tester.start("/submit");
		SubmitController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.getDestinationPath(), is("/index.jsp"));
	}

	/**
	 * 通常テスト.
	 * validateあり。エラー無し。
	 * GAE管理者でない。登録済み.
	 */
	@Test
	public void test2() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		
		tester.environment.setEmail("hage@example.com");
		tester.environment.setAdmin(false);

		HttpServletRequest request = tester.request;
		request.setAttribute("dummy", "123");
		tester.start("/submit");
		SubmitController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.getDestinationPath(), is("/index.jsp"));
	}

	/**
	 * エラーテスト.
	 * validateあり。エラー無し。
	 * GAE管理者でない。未登録.
	 */
	@Test
	public void error() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		
		tester.environment.setEmail("hige@example.com");
		tester.environment.setAdmin(false);

		HttpServletRequest request = tester.request;
		request.setAttribute("dummy", "123");
		tester.start("/submit");
		SubmitController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.getDestinationPath(), is("/error/noregist/"));
	}

	/**
	 * エラーテスト.
	 * validateあり。エラー有り。
	 * GAE管理者でない。登録済み.
	 */
	@Test
	public void error2() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		
		tester.environment.setEmail("hage@example.com");
		tester.environment.setAdmin(false);

		HttpServletRequest request = tester.request;
		request.setAttribute("dummy2", "123");
		tester.start("/submit");
		SubmitController controller = tester.getController();
		assertThat(controller, is(notNullValue()));
		assertThat(tester.getDestinationPath(), is("/error.jsp"));
	}

	/* (非 Javadoc)
	 * @see org.slim3.tester.ControllerTestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		super.setUp();

		MemberModel model = new MemberModel();
		model.setKey(Datastore.createKey(MemberModel.class, "hage@example.com"));
		Datastore.put(model);
		
	}
}
