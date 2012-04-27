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
package jp.co.nemuzuka.tester;

import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * HRD環境の為のテストクラス.
 * @author kazumune
 */
public class AppEngineTestCase4HRD extends AppEngineTestCase {
	LocalServiceTestHelper helper;  
	
	/* (非 Javadoc)
	 * @see org.slim3.tester.ControllerTestCase#setUp()
	 */
	@Override
	public void setUp() throws Exception {
		LocalDatastoreServiceTestConfig dsConfig =
				new LocalDatastoreServiceTestConfig()
					.setDefaultHighRepJobPolicyUnappliedJobPercentage(0.01f);
		helper = new LocalServiceTestHelper(dsConfig);
		helper.setUp();
		super.setUp();
	}
	  
	/* (非 Javadoc)
	 * @see org.slim3.tester.ControllerTestCase#tearDown()
	 */
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		helper.tearDown();
	}
}
