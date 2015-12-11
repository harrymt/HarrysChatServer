package test;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import main.ClientStub;

public class ITestLISTRegistered {
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
	public void testRegisteredList() {
		client.sendMessage("LIST");
		String output = "OK " + main_clientUsername + ", ";
		assertEquals("Test list with 1 user on server", output, client.getLastServerResponse());	
	}
	
	/**
	 * written
	 */
	@Test
	public void testListRegisteredWithParameters() {
		client.sendMessage("LIST abc");
		String output = "OK " + main_clientUsername + ", ";
		assertEquals("Test list with parameters", output, client.getLastServerResponse());
	}
	
	/**
	 * written
	 */
	@Test
	public void testListRegisteredWithParametersInvalid() {
		client.sendMessage("LIST ❤☀☆☂☻♞");
		String output = "OK " + main_clientUsername + ", ";
		assertEquals("Test list with unicode parameters", output, client.getLastServerResponse());
	}
	
	/**
	 * written
	 */
	@Test
	public void testListRegisteredWith2Users() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("clientB");
		client.sendMessage("LIST");
		String output = "OK " + main_clientUsername + ", " + "clientB, ";
		assertEquals("Test list with 2 users", output, client.getLastServerResponse());
		
		clientB.sendMessage("QUIT");
		clientB = null;
	}

	/**
	 * written
	 */
	@Test
	public void testListRegisteredWithOddUsers() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("clientB");
		ClientStub clientC = HelperMethods.spawnNewRegisteredClient("clientC");
		client.sendMessage("LIST");
		String output = "OK " + main_clientUsername + ", " + "clientB, clientC, ";
		assertEquals("Test list with odd users", output, client.getLastServerResponse());
		
		clientB.sendMessage("QUIT");
		clientB = null;
		clientC.sendMessage("QUIT");
		clientC = null;
	}
}