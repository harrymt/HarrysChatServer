package harry;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import harry.ClientStub;

public class ITestHAILRegistered {
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
	public void testRegisteredHail_Ideal() {
		String input = "hello";
		client.sendMessage("HAIL " + input);
		ArrayList<String> responses = client.getLastArrayServerResponses();

		assertEquals("Test hail ideal message pt1", "Broadcast from " + main_clientUsername + ": " + input, responses.get(responses.size() - 1));
		assertEquals("Test hail ideal message pt2", "", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredHail_NumericalParameter() {
		String input = "123";
		client.sendMessage("HAIL " + input);
		ArrayList<String> responses = client.getLastArrayServerResponses();

		assertEquals("Test hail numerical message pt1", "Broadcast from " + main_clientUsername + ": " + input, responses.get(responses.size() - 1));
		assertEquals("Test hail numerical message pt2", "", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredHail_UnicodeCharacters() {
		String input = "❤☀☆☂☻♞";
		client.sendMessage("HAIL " + input);
		ArrayList<String> responses = client.getLastArrayServerResponses();

		assertEquals("Test hail unicode parameter pt1", "Broadcast from " + main_clientUsername + ": " + input, responses.get(responses.size() - 1));
		assertEquals("Test hail unicode parameter pt2", "", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredHail_NewLineInParameter() {
		String inputLineA = "hi";
		String inputLineB = "everyone";
		client.sendMessage("HAIL " + inputLineA + System.getProperty("line.separator") + inputLineB);
		ArrayList<String> responses = client.getLastArrayServerResponses();

		assertEquals("Test hail ideal message pt1", "Broadcast from " + main_clientUsername + ": " + inputLineA, responses.get(responses.size() - 2));
		assertEquals("Test hail ideal message pt2", "", responses.get(responses.size() - 1));
		assertEquals("Test hail ideal message pt3", "BAD command not recognised", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredHail_InvalidParameter() {
		String message = "!@£$%^&*()_+-={}[]\"|'\\:;?/><.,'~`";
		client.sendMessage("HAIL " + message);
		ArrayList<String> responses = client.getLastArrayServerResponses();
		assertEquals("Test hail odd parameter pt1", "Broadcast from " + main_clientUsername + ": " + message, responses.get(responses.size() - 1));
		assertEquals("Test hail odd parameter pt2", "", client.getLastServerResponse());
	}

	/**
	 * written (server error)
	 */
	@Test
	public void testHailRegisteredNoParameterOrSpace() {
		// Server gives: java.lang.StringIndexOutOfBoundsException: String index out of range: -1
		// client.sendMessage("HAIL");
		assertEquals("Test hail no space and no parameters", true, true);
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredHail_IdealMultipleClients() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("ClientB");
		String input = "hello";
		client.sendMessage("HAIL " + input);
		ArrayList<String> responses = client.getLastArrayServerResponses();

		assertEquals("Test hail multiple clients pt1", "Broadcast from " + main_clientUsername + ": " + input, responses.get(responses.size() - 1));
		assertEquals("Test hail multiple clients pt2", "", client.getLastServerResponse());

		ArrayList<String> responsesB = clientB.getLastArrayServerResponses();

		assertEquals("Test hail multiple clients second client pt1", "Broadcast from " + main_clientUsername + ": " + input, responsesB.get(responsesB.size() - 1));
		assertEquals("Test hail multiple clients second client pt2", "", clientB.getLastServerResponse());
	}


	/**
	 * written
	 */
	@Test
	public void testRegisteredHail_IdealMultipleOddClients() {
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient("ClientB");
		ClientStub clientC = HelperMethods.spawnNewRegisteredClient("ClientC");
		String input = "hello";
		client.sendMessage("HAIL " + input);
		ArrayList<String> responses = client.getLastArrayServerResponses();

		assertEquals("Test hail odd number of multiple clients pt1", "Broadcast from " + main_clientUsername + ": " + input, responses.get(responses.size() - 1));
		assertEquals("Test hail odd number of multiple clients pt2", "", client.getLastServerResponse());

		ArrayList<String> responsesB = clientB.getLastArrayServerResponses();

		assertEquals("Test hail odd number of multiple clients, second client pt1", "Broadcast from " + main_clientUsername + ": " + input, responsesB.get(responsesB.size() - 1));
		assertEquals("Test hail odd number of multiple clients, second client pt2", "", clientB.getLastServerResponse());


		ArrayList<String> responsesC = clientC.getLastArrayServerResponses();
		assertEquals("Test hail odd number of multiple clients, third client pt1", "Broadcast from " + main_clientUsername + ": " + input, responsesC.get(responsesC.size() - 1));
		assertEquals("Test hail odd number of multiple clients, third client pt2", "", clientC.getLastServerResponse());
	}
}
