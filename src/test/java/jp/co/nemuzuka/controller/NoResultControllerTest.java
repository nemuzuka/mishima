package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class NoResultControllerTest extends ControllerTestCase {

	/**
	 * 通常テスト.
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		tester.start("/noResult");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		JSONObject actual = JSONObject.fromObject(tester.response.getOutputAsString());
		assertThat(actual.toString(), is("{\"errorMsg\":[\"サーバでエラーが発生しました。\"],\"infoMsg\":[],\"result\":-3,\"status\":0}"));
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
