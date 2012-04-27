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

import jp.co.nemuzuka.model.MemberModel;
import jp.co.nemuzuka.tester.ControllerTestCase4HRD;

import org.junit.Test;
import org.slim3.datastore.Datastore;

public class SubmitControllerTest extends ControllerTestCase4HRD {

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
