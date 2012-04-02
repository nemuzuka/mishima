package jp.co.nemuzuka.tester;

import org.slim3.tester.ControllerTestCase;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

/**
 * HRD環境の為の規定テストクラス.
 * @author kazumune
 */
public class ControllerTestCase4HRD extends ControllerTestCase {
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
