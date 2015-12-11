package chatClient;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClientUnitTests {
	static ClientGUI client;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		client = new ClientGUI();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client = null;
	}

	/**
	 * written
	 */
	@Test
	public void testValidIdealServerCommands() {
		String input = "STAT";
		assertTrue(client.validCommand(input));
		input = "LIST";
		assertTrue(client.validCommand(input));
		input = "IDEN harry";
		assertTrue(client.validCommand(input));
		input = "MESG harry hi";
		assertTrue(client.validCommand(input));
		input = "HAIL hello";
		assertTrue(client.validCommand(input));
		input = "QUIT";
		assertTrue(client.validCommand(input));
	}
	
	/**
	 * written
	 */
	@Test
	public void testValidServerCommandsInvalidParametersIDEN() {
		String input = "IDEN";
		assertFalse(client.validCommand(input));
		input = "IDEN ";
		assertFalse(client.validCommand(input));
		// TODO discuss how this is allowed
		input = "IDEN  ";
		assertTrue(client.validCommand(input));
	}
	
	/**
	 * written
	 */
	@Test
	public void testValidServerCommandsInvalidParametersHAIL() {
		String input = "HAIL";
		assertFalse(client.validCommand(input));
		input = "HAIL ";
		assertFalse(client.validCommand(input));
	}
	
	/**
	 * written
	 */
	@Test
	public void testValidServerCommandsInvalidParametersMESG() {
		String input = "MESG";
		assertFalse(client.validCommand(input));
		input = "MESG ";
		assertFalse(client.validCommand(input));
		input = "MESG  ";
		assertFalse(client.validCommand(input));
	}
	
	/**
	 * written
	 */
	@Test
	public void testInvalidText() {
		String input = " ";
		assertFalse(client.validCommand(input));
		input = "❤☀☆☂☻♞";
		assertFalse(client.validCommand(input));
	}
}
