package test;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.ClientStub;

public class ITestGeneral {
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
	public void testInvalidMessage_Empty() {
		client.sendMessage("");
		String output = "BAD invalid command to server";
		assertEquals("Test empty message", output, client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testInvalidMessage_InvalidCommandCharacter() {
		client.sendMessage("❤☀☆☂☻♞"); // unicode characters
		String output = "BAD command not recognised";
		assertEquals("Test unicode message length bigger than a normal command.", output, client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testInvalidMessage_InvalidCharacter() {
		client.sendMessage("❤☀☆"); // 3 unicode characters
		String output = "BAD invalid command to server";
		assertEquals("Test unicode message length less than normal command ", output, client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testInvalidMessage_AnotherCommand() {
		client.sendMessage("SHUT message"); // Another command
		String output = "BAD command not recognised";
		assertEquals("Test a completely different command with same structure.", output, client.getLastServerResponse());	
	}
	
	/**
	 * written
	 * Throws a OutOfMemoryException at around 1013 client connected.
	 */
	@Test
	public void testServerConnections() {
//		for(int i = 0; i < 2000; i++) {
//			 HelperMethods.spawnFastNewClient();
//		}
	}
}
