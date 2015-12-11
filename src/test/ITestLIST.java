package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.ClientStub;

public class ITestLIST {
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
	public void testListIdeal() {
		client.sendMessage("LIST");
		assertEquals("Test unregistered list", "BAD You have not logged in yet", client.getLastServerResponse());	
	}
	

	/**
	 * written
	 */
	@Test
	public void testListInLowercase() {
		client.sendMessage("list");
		assertEquals("Test unregistered list, lowercase", "BAD command not recognised", client.getLastServerResponse());	
	}
	

	/**
	 * written
	 */
	@Test
	public void testListSpaceBefore() {
		client.sendMessage(" LIST");
		assertEquals("Test unregistered list, space before", "BAD command not recognised", client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testListWithParameters() {
		client.sendMessage("LIST abc");
		assertEquals("Test unregistered list with parameters", "BAD You have not logged in yet", client.getLastServerResponse());
	}
	
	/**
	 * written
	 */
	@Test
	public void testListWithParametersInvalid() {
		client.sendMessage("LIST ❤☀☆☂☻♞");
		assertEquals("Test unregistered list with unicode parameters", "BAD You have not logged in yet", client.getLastServerResponse());
	}
}
