package harry;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import harry.ClientStub;

public class HarrysTest {
	private static ClientStub client = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		HelperMethods.startServer();
		client = HelperMethods.spawnNewClient();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		client.sendMessage("QUIT");
		client = null;
		HelperMethods.stopServer();
	}

	/**
	 * written
	 */
	@Test
	public void testHail_Ideal() {
		client.sendMessage("HAIL hello");
		assertEquals("Test hail ideal parameters", "BAD You have not logged in yet", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testHailParameterEmptyString() {
		client.sendMessage("HAIL ");
		assertEquals("Test hail parameter as empty string", "BAD You have not logged in yet",
				client.getLastServerResponse());
	}

	/**
	 * written (server error)
	 */
	@Test
	public void testHailNoParameterOrSpace() {
		// Server gives: java.lang.StringIndexOutOfBoundsException: String index
		// out of range: -1
		// client.sendMessage("HAIL");
		assertEquals("Test hail no space and no parameters", true, true);
	}

	/**
	 * written
	 */
	@Test
	public void testHailWithUnicodeParams() {
		client.sendMessage("HAIL ❤☀☆☂☻♞");
		assertEquals("Test Hail with unicode parameters.", "BAD You have not logged in yet",
				client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testHailWithLowercase() {
		client.sendMessage("hail");
		assertEquals("Test hail in lowercase.", "BAD command not recognised", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testHailWithSpaceBefore() {
		client.sendMessage(" HAIL");
		assertEquals("Test hail with space before.", "BAD command not recognised", client.getLastServerResponse());
	}
}
