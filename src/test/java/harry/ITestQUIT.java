package harry;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import harry.ClientStub;

public class ITestQUIT {
	private static ClientStub client = null;
	private static int number_of_clients_attached = 0;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HelperMethods.startServer();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		HelperMethods.stopServer();
	}

	@Before
	public void setUp() throws Exception {
		client = HelperMethods.spawnNewClient(); number_of_clients_attached++;
	}

	@After
	public void tearDown() throws Exception {
		client = null; number_of_clients_attached--;
	}

	/**
	 * written
	 */
	@Test
	public void testQuit_Ideal() {
		client.sendMessage("QUIT");
		assertEquals("Test quit", "OK goodbye", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testQuit_WithParameters() {
		client.sendMessage("QUIT abc123");
		assertEquals("Test quit with params", "OK goodbye", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testQuit_WithParametersNoSpace() {
		client.sendMessage("QUITabc123");
		assertEquals("Test quit with params, not space", "OK goodbye", client.getLastServerResponse());
	}


	/**
	 * written
	 */
	@Test
	public void testQuit_HasActuallyRemovedClient() {
		ClientStub clientB = HelperMethods.spawnNewClient(); number_of_clients_attached++;

		client.sendMessage("STAT");
		assertEquals("Test quit actually removes client", "OK There are currently " + number_of_clients_attached
				+ " user(s) on the server You have not logged in yet", client.getLastServerResponse());

		clientB.sendMessage("QUIT");
		clientB = null; number_of_clients_attached--;

		client.sendMessage("STAT");
		assertEquals("Test quit actually removes client", "OK There are currently " + number_of_clients_attached
				+ " user(s) on the server You have not logged in yet", client.getLastServerResponse());

	}
}
