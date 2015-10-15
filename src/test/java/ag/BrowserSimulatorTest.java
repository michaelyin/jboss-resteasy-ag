package ag;

import static org.junit.Assert.*;

import org.junit.Test;

import ag.login.client.BrowserSimulator;

public class BrowserSimulatorTest {

	@Test
	public void testLogin() throws Exception {

		BrowserSimulator bs = new BrowserSimulator();
		
		assertTrue(bs.isUserLoggedIn());
	}

}
