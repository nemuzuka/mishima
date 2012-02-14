package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.slim3.tester.ControllerTestCase;

public class SampleControllerTest extends ControllerTestCase {

	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		tester.start("/sample");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		assertThat(tester.response.getOutputAsString(), is("{\"errorMsg\":[],\"infoMsg\":[],\"result\":null,\"status\":0}"));
	}

}
