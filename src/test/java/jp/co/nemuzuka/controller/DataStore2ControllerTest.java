package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import jp.co.nemuzuka.model.HogeModel;
import jp.co.nemuzuka.service.HogeService;
import jp.co.nemuzuka.tester.ControllerTestCase4HRD;

import org.junit.Test;

public class DataStore2ControllerTest extends ControllerTestCase4HRD {

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
