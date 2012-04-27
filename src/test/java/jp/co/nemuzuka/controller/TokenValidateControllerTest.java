/*
 * Copyright 2012 Kazumune Katagiri. (http://d.hatena.ne.jp/nemuzuka)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
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
