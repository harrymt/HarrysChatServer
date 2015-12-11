package integrationTests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chatClient.ClientStub;

public class ITestQUITRegistered {
	private static ClientStub client = null;
	private static String main_clientUsername = "harry";
	
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
		client = HelperMethods.spawnNewRegisteredClient(main_clientUsername); 
	}

	@After
	public void tearDown() throws Exception {
		client = null;
	}
	
	/**
	 * written
	 */
	@Test
	public void testQuit_Ideal() {
		int messagesSent = 0;
		
		client.sendMessage("QUIT");
		assertEquals("Test quit", "OK thank you for sending " + messagesSent
				+ " message(s) with the chat service, goodbye. ", client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testQuit_MessagesSent() {		
		int messagesSent = 0;
		
		client.sendMessage("HAIL hello"); messagesSent++;
		client.sendMessage("QUIT");
		assertEquals("Test quit, messages sent", "OK thank you for sending " + messagesSent
				+ " message(s) with the chat service, goodbye. ", client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testQuit_RemovedUserFromSTAT() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("bob"); 
		
		int messagesSent = 0;
		int numberOfUsersOnServer = 2;
		client.sendMessage("STAT");
		assertEquals("Test Quit removed from STAT with 2 users on server", "OK There are currently "+ numberOfUsersOnServer +
				" user(s) on the server You are logged in and have sent " + messagesSent +
				" message(s)", client.getLastServerResponse());	
		
		
		clientB.sendMessage("QUIT"); numberOfUsersOnServer--;
		clientB = null;
		
		client.sendMessage("STAT");
		assertEquals("Test Quit removed from STAT with 2 users on server", "OK There are currently "+ numberOfUsersOnServer +
				" user(s) on the server You are logged in and have sent " + messagesSent +
				" message(s)", client.getLastServerResponse());	
		
		client.sendMessage("QUIT");
	}
	
	/**
	 * written
	 */
	@Test
	public void testQuit_RemovedUserFromLIST() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("bob"); 
		
		client.sendMessage("LIST");
		assertEquals("Test Quit removed from LIST with 2 users on server",
				"OK " + main_clientUsername + ", " + "bob, "
				, client.getLastServerResponse());	
		
		
		clientB.sendMessage("QUIT");
		clientB = null;
		
		client.sendMessage("LIST");
		assertEquals("Test Quit removed from LIST with 2 users on server",
				"OK " + main_clientUsername + ", "
				, client.getLastServerResponse());	
		
		client.sendMessage("QUIT");
	}
}
