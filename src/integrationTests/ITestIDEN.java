package integrationTests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import chatClient.ClientStub;

public class ITestIDEN {
	private static ClientStub client = null;
	
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
		client = HelperMethods.spawnNewClient(); 
	}

	@After
	public void tearDown() throws Exception {
		client.sendMessage("QUIT"); 
		client = null;
	}

	/**
	 * written
	 */
	@Test
	public void testIden_ideal() {
		String username = "harry";
		client.sendMessage("IDEN " + username);
		assertEquals("Test iden with ideal params.", "OK Welcome to the chat server " + username, client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testIden_numeric() {
		String username = "123";
		client.sendMessage("IDEN " + username);
		assertEquals("Test iden with numeric username.", "OK Welcome to the chat server " + username, client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testIden_usernameHasSpaces() {
		String usernamePartA = "harry";
		String usernamePartB = "mumford";
		
		client.sendMessage("IDEN " + usernamePartA + " " + usernamePartB);
		assertEquals("Test iden with username spaces.", "OK Welcome to the chat server " + usernamePartA, client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testIden_usernameHasNewLines() {
		String usernamePartA = "harry";
		String usernamePartB = "mumford";
		
		client.sendMessage("IDEN " + usernamePartA + System.getProperty("line.separator") + usernamePartB);
		
		ArrayList<String> responses = client.getLastArrayServerResponses();
		assertEquals("Test iden new lines pt1", "OK Welcome to the chat server " + usernamePartA, responses.get(responses.size() - 1));	
		assertEquals("Test iden new lines pt2", "BAD command not recognised", client.getLastServerResponse());	
	}

	
	/**
	 * written
	 */
	@Test
	public void testIden_usernameHasInvalidCharacters() {
		String username = "!@£$%^&*()_+-={}[]\"|'\\:;?/><.,'~`";
		
		client.sendMessage("IDEN " + username);
		assertEquals("Test iden odd username characters", "OK Welcome to the chat server " + username, client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testIden_usernameIsUnicode() {
		String username = "❤☀☆☂☻♞";
		
		client.sendMessage("IDEN " + username);
		assertEquals("Test iden unicode username characters", "OK Welcome to the chat server " + username, client.getLastServerResponse());	
	}
}
