package harry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import harry.ClientGUI;

import static org.junit.Assert.*;
import org.fest.swing.fixture.FrameFixture;

public class ClientGUITests {

	private static FrameFixture clientGUI;
	
	private final static String btnStart = "btnStart";
	private final String btnSend = "btnSend";	
	private final String textFieldUserInput = "txfTypedText";
	private final String textAreaOutputText = "txaOutputText";
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		clientGUI = new FrameFixture(new ClientGUI());
		clientGUI.button(btnStart).click();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		clientGUI.cleanUp();
	}
	
	/**
	 * written
	 * @throws InterruptedException
	 */
	@Test
	public void showResponseFromCommandEnteredCorrectly() throws InterruptedException {
		clientGUI.textBox(textFieldUserInput).enterText("STAT");
		clientGUI.button(btnSend).click();
		
		// Wait until GUI has written to text box
		clientGUI.robot.waitForIdle();

		String response = getLastResponseWritten(clientGUI.textBox(textAreaOutputText).text());
		assertTrue("Check ok server response.", response.startsWith("OK"));
	}

	/**
	 * 
	 */
	@Test
	public void checkLastResponseIsWritten() {
		String response = getLastResponseWritten("BAD asd" + System.getProperty("line.separator") + "OK asd");
		assertEquals("Check helper method", "OK asd", response);
	}
	
	/**
	 * 
	 * @param outputText
	 * @return
	 */
	private String getLastResponseWritten(String outputText) {
		if(!outputText.contains(System.getProperty("line.separator"))) {
			return "";
		}
		
		String[] output = outputText.split(System.getProperty("line.separator"));
		return output[output.length - 1];
	}

	/**
	 * written
	 * @throws InterruptedException
	 */
	@Test
	public void showResponseFromCommandEnteredInCorrectly() throws InterruptedException {
		clientGUI.textBox(textFieldUserInput).enterText("LIST");
		clientGUI.button(btnSend).click();
		
		// Wait until GUI has written to text box
		clientGUI.robot.waitForIdle();

		String response = getLastResponseWritten(clientGUI.textBox(textAreaOutputText).text());
		assertTrue("Check bad server response.", response.startsWith("BAD"));
	}
	
	/**
	 * written
	 * @throws InterruptedException
	 */
	@Test
	public void showInvalidCommandFromClient() throws InterruptedException {
		clientGUI.textBox(textFieldUserInput).enterText("MESG  ");
		clientGUI.button(btnSend).click();
		
		// Wait until GUI has written to text box
		clientGUI.robot.waitForIdle();

		String response = getLastResponseWritten(clientGUI.textBox(textAreaOutputText).text());
		assertEquals("Check bad server response.", response, ">Invalid Command");
	}
	
}
