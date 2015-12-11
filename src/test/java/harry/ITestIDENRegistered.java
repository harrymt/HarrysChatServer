package harry;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import harry.ClientStub;

public class ITestIDENRegistered {
	private static ClientStub client = null;
	private static String main_clientUsername = "harry";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HelperMethods.startServer();
		  client = HelperMethods.spawnNewRegisteredClient(main_clientUsername);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client.sendMessage("QUIT"); client = null;
		HelperMethods.stopServer();
	}

	/**
	 * written
	 */
	@Test
	public void testIdenRegistered_AfterRegistered() {
		String username = "bob";
		client.sendMessage("IDEN " + username);
		assertEquals("Test iden with ideal params.", "BAD you are already registered with username " + main_clientUsername, client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testIdenRegistered_AfterRegisteredSameUsername() {
		client.sendMessage("IDEN " + main_clientUsername);
		assertEquals("Test iden with same username params.", "BAD you are already registered with username " + main_clientUsername, client.getLastServerResponse());
	}


	/**
	 * written
	 */
	@Test
	public void testIdenRegistered_AfterRegisteredSameUsernameInLowercase() {
		client.sendMessage("IDEN " + main_clientUsername.toUpperCase());
		assertEquals("Test iden with same username params in lowercase.", "BAD you are already registered with username " + main_clientUsername, client.getLastServerResponse());
	}
}
