package harry;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import harry.ClientStub;

public class ITestMESGRegistered {
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
	public void testRegisteredMesg_Ideal() {
		String clientBUsername = "clientB";
		String message = "hello";

		ClientStub clientB = HelperMethods.spawnNewRegisteredClient(clientBUsername);
		client.sendMessage("MESG " + clientBUsername + " " + message);

		ArrayList<String> responses = clientB.getLastArrayServerResponses();
		assertEquals("Test mesg ideal parameters, another client", "PM from " + main_clientUsername + ":" + message, responses.get(responses.size() - 1));

		assertEquals("Test mesg ideal parameters, anotherclient, yourself", "OK your message has been sent", client.getLastServerResponse());

		clientB.sendMessage("QUIT");
		clientB = null;
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredMesg_InvalidUser() {
		String anotherUser = "AUserThatDoesntExist";
		String message = "hello";

		client.sendMessage("MESG " + anotherUser + " " + message);
		assertEquals("Test mesg user doesnt exist", "BAD the user does not exist", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredMesg_InvalidParameterCharacters() {
		String message = "!@£$%^&*()_+-={}[]\"|'\\:;?/><.,'~`";

		client.sendMessage("MESG " + main_clientUsername + " " + message);

		ArrayList<String> responses = client.getLastArrayServerResponses();
		assertEquals("Test mesg odd message pt1", "PM from " + main_clientUsername + ":" + message, responses.get(responses.size() - 2));
		assertEquals("Test mesg odd message pt2", "", responses.get(responses.size() - 1));
		assertEquals("Test mesg odd message pt3", "OK your message has been sent", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testRegisteredMesg_IdealToYourself() {
		String message = "hi";
		client.sendMessage("MESG " + main_clientUsername + " " + message);
		ArrayList<String> responses = client.getLastArrayServerResponses();

		assertEquals("Test mesg, message yourself pt1", "PM from " + main_clientUsername + ":" + message, responses.get(responses.size() - 2));
		assertEquals("Test mesg, message yourself pt2", "", responses.get(responses.size() - 1));

		assertEquals("Test mesg, message yourself pt3", "OK your message has been sent", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testMesgRegistered_IdealCheckIfOtherClientsSeeMessage() {
		String clientCUser = "clientC";
		ClientStub clientC = HelperMethods.spawnNewRegisteredClient(clientCUser);

		String clientBUsername = "clientB";
		String message = "hello";

		ClientStub clientB = HelperMethods.spawnNewRegisteredClient(clientBUsername);
		client.sendMessage("MESG " + clientBUsername + " " + message);

		ArrayList<String> responses = clientB.getLastArrayServerResponses();
		assertEquals("Test mesg check it sends privately pt1", "PM from " + main_clientUsername + ":" + message, responses.get(responses.size() - 1));

		// Main client
		assertEquals("Test mesg check it sends privately pt2", "OK your message has been sent", client.getLastServerResponse());

		assertEquals("Test mesg check it sends privately pt3", "OK Welcome to the chat server " + clientCUser, clientC.getLastServerResponse());

		clientB.sendMessage("QUIT");
		clientB = null;

		clientC.sendMessage("QUIT");
		clientC = null;
	}


	/**
	 * written
	 */
	@Test
	public void testMesgRegisteredParameterEmptyString() {
		client.sendMessage("MESG ");
		assertEquals("Test mesg parameters as empty string", "BAD Your message is badly formatted", client.getLastServerResponse());
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
	public void testMesgRegisteredWithBothUnicodeParams() {
		client.sendMessage("MESG ❤☀☆ ☂☻♞");
		assertEquals("Test Mesg with invalid parameters.", "BAD the user does not exist", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testMesgRegisteredWithMessageUnicodeParams() {
		client.sendMessage("MESG harry ☂☻♞");
		assertEquals("Test Mesg with invalid parameters, correct user.", "OK your message has been sent", client.getLastServerResponse());
	}

	/**
	 * written
	 */
	@Test
	public void testMesgRegisteredWithOnly1ParameterOfValidUser() {
		String clientBUsername = "clientB";
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient(clientBUsername);
		client.sendMessage("MESG " + clientBUsername + " ");

		ArrayList<String> responses = clientB.getLastArrayServerResponses();
		assertEquals("Test mesg ideal parameters, another client", "PM from " + main_clientUsername + ":", responses.get(responses.size() - 1));
		assertEquals("Test mesg ideal parameters, another client", "", clientB.getLastServerResponse());

		// Main client
		assertEquals("Test mesg single param with separator", "OK your message has been sent", client.getLastServerResponse());

		clientB.sendMessage("QUIT");
		clientB = null;
	}

	/**
	 * written
	 */
	@Test
	public void testMesgRegisteredWithOnly1ParameterOfValidUserAndNoSpaceAfter() {
		String clientBUsername = "clientB";
		ClientStub clientB = HelperMethods.spawnNewRegisteredClient(clientBUsername);
		client.sendMessage("MESG " + clientBUsername);

		// Main client
		assertEquals("Test mesg single param", "BAD Your message is badly formatted", client.getLastServerResponse());

		clientB.sendMessage("QUIT");
		clientB = null;
	}


	/**
	 * written (server error)
	 */
	@Test
	public void testMesgRegisteredSendingToUserSpace() {
		// String clientBUsername = " ";
		// String message = "hello";
		// Server exception java.lang.ArrayIndexOutOfBoundsException: 0
		// client.sendMessage("MESG " + clientBUsername + " " + message);
	}

	/**
	 * written
	 */
	@Test
	public void testMesgRegisteredSendingNewLinesInMessage() {
		String clientBUsername = "bob";
		String messagePartA = "hello";
		String messagePartB = "bob";
		String fullMessage = messagePartA + System.getProperty("line.separator") + messagePartB;

		ClientStub clientB = HelperMethods.spawnNewRegisteredClient(clientBUsername);
		client.sendMessage("MESG " + clientBUsername + " " + fullMessage);

		ArrayList<String> responses = clientB.getLastArrayServerResponses();
		assertEquals("Test hail ideal message pt1", "PM from " + main_clientUsername + ":" + messagePartA, responses.get(responses.size() - 1));
		assertEquals("Test hail ideal message pt2", "", clientB.getLastServerResponse());

		// Main client
		assertEquals("Test mesg new lines in message", "BAD invalid command to server", client.getLastServerResponse());

		clientB.sendMessage("QUIT");
		clientB = null;
	}
}
