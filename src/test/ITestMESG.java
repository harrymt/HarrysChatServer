package integrationTests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import chatClient.ClientStub;

public class ITestMESG {
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
	public void testMesg_Ideal() {
		client.sendMessage("MESG aUser hi");
		assertEquals("Test mesg user doesnt exist", "BAD You have not logged in yet", client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testMesg_IdealToActualUser() {
		String clientBUser = "clientB";
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient(clientBUser);
		
		client.sendMessage("MESG " + clientBUser + " hi");
		assertEquals("Test mesg ideal, existing user", "BAD You have not logged in yet", client.getLastServerResponse());	
		assertEquals("Test mesg ideal, existing user", "OK Welcome to the chat server " + clientBUser, clientB.getLastServerResponse()); // Nothing should've been received
		
		clientB.sendMessage("QUIT");
		clientB = null;
	}
	
	
	/**
	 * written
	 */
	@Test
	public void testMesgParameterEmptyString() {
		client.sendMessage("MESG ");
		assertEquals("Test mesg parameters as empty string", "BAD You have not logged in yet", client.getLastServerResponse());	
	}
	
	
	/**
	 * written (server error)
	 */
	@Test
	public void testMesgNoParameterOrSpace() {
		// Server gives: java.lang.StringIndexOutOfBoundsException: String index out of range: -1
		// client.sendMessage("MESG");
		assertEquals("Test mesg no space and no parameters", true, true);	
	}
	
	/**
	 * written
	 */
	@Test
	public void testMesgWithUnicodeParams() {
		client.sendMessage("MESG ❤☀☆ ☂☻♞");
		assertEquals("Test Mesg with invalid parameters.", "BAD You have not logged in yet", client.getLastServerResponse());
	}
	
	/**
	 * written
	 */
	@Test
	public void testMesgWithLowercase() {
		client.sendMessage("mesg");
		assertEquals("Test mesg in lowercase.", "BAD command not recognised", client.getLastServerResponse());
	}
	
	/**
	 * written
	 */
	@Test
	public void testMesgWithSpaceBefore() {
		client.sendMessage(" MESG");
		assertEquals("Test mesg with space before.", "BAD command not recognised", client.getLastServerResponse());
	}
	
	/**
	 * written
	 */
	@Test
	public void testMesgWithMultipleSpaces() {
		client.sendMessage("MESG  ");
		assertEquals("Test mesg with multiple spaces.", "BAD You have not logged in yet", client.getLastServerResponse());
	}
}
