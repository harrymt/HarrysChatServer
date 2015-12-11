package integrationTests;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import chatClient.ClientStub;

public class ITestSTATRegistered {
	private static ClientStub client = null;
	private static String main_clientUsername = "harry";
	private static int main_clientHailCount = 0; // main client message count
	private static int number_of_clients_attached = 0;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HelperMethods.startServer();
		  client = HelperMethods.spawnNewRegisteredClient(main_clientUsername);
		  number_of_clients_attached++;
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
	public void testRegisteredStat() {
		int numberOfUsersOnServer = 1; // Tests the server.getNumberOfUsers() method			
		String output = "";
		
		client.sendMessage("STAT");
		output = client.getLastServerResponse();
		assertEquals("Test STAT with 1 user on server", "OK There are currently "+ numberOfUsersOnServer +
				" user(s) on the server You are logged in and have sent " + main_clientHailCount +
				" message(s)", output);	
	}
	
	/**
	 * written
	 */
	@Test
	public void testRegisteredStatWith2Users() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("second_client");
		number_of_clients_attached++;
		int clientBMessageCount = 0;
		clientB.sendMessage("STAT");
		assertEquals("Test STAT with 2 users on server, from first user", "OK There are currently "+ number_of_clients_attached +
				" user(s) on the server You are logged in and have sent " + clientBMessageCount +
				" message(s)", clientB.getLastServerResponse());
		
		client.sendMessage("STAT");
		assertEquals("Test STAT with 2 users on server, from second user", "OK There are currently "+ number_of_clients_attached +
				" user(s) on the server You are logged in and have sent " + main_clientHailCount +
				" message(s)", client.getLastServerResponse());	
		
		number_of_clients_attached--;
		clientB.sendMessage("QUIT");
		clientB = null;
	}
	
	/**
	 * written
	 */
	@Test
	public void testRegisteredStatWithOddUsers() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("second_client");
		number_of_clients_attached++;
		
		ClientStub clientC = HelperMethods.spawnNewRegisteredClient("third_client");
		number_of_clients_attached++;
		int clientCMessageCount = 0;
		clientC.sendMessage("STAT");
		assertEquals("Test STAT with odd users on server, from third user", "OK There are currently "+ number_of_clients_attached +
				" user(s) on the server You are logged in and have sent " + clientCMessageCount +
				" message(s)", clientC.getLastServerResponse());

		clientB.sendMessage("QUIT");
		clientB = null;
		number_of_clients_attached--;
		
		clientC.sendMessage("QUIT");
		clientC = null;
		number_of_clients_attached--;
	}
	
	/**
	 * written
	 */
	@Test
	public void testRegisteredStatMessageCountWithMultipleUsers() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("second_client");
		number_of_clients_attached++;
		
		client.sendMessage("HAIL just to bump the main clients message count");
		main_clientHailCount++;
		client.sendMessage("STAT");
		assertEquals("Test STAT message count with 2 users on server, from user who sent message.",
				"OK There are currently " + number_of_clients_attached +
				" user(s) on the server You are logged in and have sent " + main_clientHailCount +
				" message(s)", client.getLastServerResponse());
		
		clientB.sendMessage("STAT");
		assertEquals("Test STAT message count with 2 users on server, from user who sent message.",
				"OK There are currently " + number_of_clients_attached +
				" user(s) on the server You are logged in and have sent " + 0 +
				" message(s)", clientB.getLastServerResponse());
		
		number_of_clients_attached--;
		clientB.sendMessage("QUIT");
		clientB = null;
	}
	
}
