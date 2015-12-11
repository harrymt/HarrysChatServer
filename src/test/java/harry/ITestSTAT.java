package harry;

import static org.junit.Assert.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import harry.ClientStub;

/**
 * Integration tests for the STAT command in the client's Unregistered state.
 * @author Harry Mumford-Turner
 *
 */
public class ITestSTAT {
	private static ClientStub client = null;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HelperMethods.startServer();
		  client = HelperMethods.spawnNewClient();
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
	public void testJustStat() {
		client.sendMessage("STAT");
		int numberOfUsersOnServer = 1; // Tests the server.getNumberOfUsers() method
		assertEquals("Test unregsitered stat", "OK There are currently " + numberOfUsersOnServer
				+ " user(s) on the server You have not logged in yet", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testStatWithOtherUsersLoggedIn() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("second_client");
		clientB.sendMessage("HAIL hi"); // bump client B's message count
		client.sendMessage("STAT");
		int numberOfUsersOnServer = 2; // Tests the server.getNumberOfUsers() method
		assertEquals("Test unregistered stat with other logged in users.", "OK There are currently " + numberOfUsersOnServer
				+ " user(s) on the server You have not logged in yet", client.getLastServerResponse());
		clientB.sendMessage("QUIT");
		clientB = null;
		numberOfUsersOnServer--;
	}

	/**
	 * written
	 */
	@Test
	public void testStatWithParams() {
		client.sendMessage("STAT random_params");
		assertEquals("Test unregisterd stat with parameters.", "OK There are currently 1"
				+ " user(s) on the server You have not logged in yet", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testStatWithUnicodeParams() {
		client.sendMessage("STAT ❤☀☆☂☻♞");
		assertEquals("Test unregistered stat with unicode parameters.", "OK There are currently 1"
				+ " user(s) on the server You have not logged in yet", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testStatWithLowercase() {
		client.sendMessage("stat");
		assertEquals("Test stat in lowercase.", "BAD command not recognised", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testStatWithSpaceBefore() {
		client.sendMessage(" STAT");
		assertEquals("Test stat with space before.", "BAD command not recognised", client.getLastServerResponse());
	}
}
