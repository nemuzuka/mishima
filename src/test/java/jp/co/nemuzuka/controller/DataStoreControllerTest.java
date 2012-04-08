package jp.co.nemuzuka.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.co.nemuzuka.model.Slim3Model;
import jp.co.nemuzuka.service.Slim3Service;
import jp.co.nemuzuka.tester.ControllerTestCase4HRD;

import org.junit.Test;

public class DataStoreControllerTest extends ControllerTestCase4HRD {

	/**
	 * 通常テスト.
	 * validateあり。エラー無し。
	 */
	@Test
	public void test() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		
		List<Slim3Model> actualList = Slim3Service.queryAll();
		assertThat(actualList.size(), is(0));

		HttpServletRequest request = tester.request;
		request.setAttribute("dummy", "テストでんがな");
		tester.start("/dataStore");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual, is("{\"errorMsg\":[],\"infoMsg\":[\"処理が正常に終了しました。反映に時間がかかる場合があります。\"],\"result\":{\"hoge\":\"終了でーす。\"},\"status\":0,\"token\":null}"));
		
		//データストアに格納されていることを確認
		actualList = Slim3Service.queryAll();
		assertThat(actualList.size(), is(1));
		assertThat(actualList.get(0).getProp1(), is("テストでんがな"));
	}

	/**
	 * エラーテスト.
	 * ロールバックされること
	 */
	@Test
	public void errorTest() throws NullPointerException, IllegalArgumentException, IOException, ServletException {
		List<Slim3Model> actualList = Slim3Service.queryAll();
		assertThat(actualList.size(), is(0));

		HttpServletRequest request = tester.request;
		request.setAttribute("dummy", "hoge");
		tester.start("/dataStore");
		assertThat(tester.response.getStatus(),
				is(equalTo(HttpServletResponse.SC_OK)));
		String actual = tester.response.getOutputAsString();
		assertThat(actual, is("{\"errorMsg\":[\"サーバでエラーが発生しました。\"],\"infoMsg\":[],\"result\":null,\"status\":-3,\"token\":null}"));
		
		//データストアに格納されていないことを確認
		actualList = Slim3Service.queryAll();
		assertThat(actualList.size(), is(0));
	}
}
