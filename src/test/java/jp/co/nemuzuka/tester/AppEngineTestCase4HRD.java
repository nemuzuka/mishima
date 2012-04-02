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
