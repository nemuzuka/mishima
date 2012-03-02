package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import jp.co.nemuzuka.model.HogeModel;
import jp.co.nemuzuka.service.HogeService;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class DataStore2ControllerTest extends ControllerTestCase {

	/**
	 * 通常テスト.
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		
		List<HogeModel> actualList = HogeService.queryAll();
		assertThat(actualList.size(), is(0));

		tester.start("/dataStore2");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		
		actualList = HogeService.queryAll();
		assertThat(actualList.size(), is(1));
	}

}
