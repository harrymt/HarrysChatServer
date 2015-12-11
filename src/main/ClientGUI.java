package main;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTextArea tbOutput = new JTextArea(10, 50);
	private JTextField tbInput = new JTextField();
	private JButton connectToServerButton = new JButton("Start");
	private JButton sendMessageButton = new JButton("Send");

	ClientStub client;

	public ClientGUI() {
		StartServer onClickStartHandler = new StartServer();
		SendMessage onClickSendMessageHandler = new SendMessage();

		// Needed for GUI testing TODO talk about this in report
		connectToServerButton.setName("btnStart");
		sendMessageButton.setName("btnSend");
		tbOutput.setName("txaOutputText");
		tbInput.setName("txfTypedText");

		// Setup GUI
		tbOutput.setEditable(false);
		connectToServerButton.addActionListener(onClickStartHandler);
		sendMessageButton.addActionListener(onClickSendMessageHandler);

		GridLayout gridLayout = new GridLayout(1, 3);
		JPanel bottomPanel = new JPanel(gridLayout);

		bottomPanel.add(tbInput);
		bottomPanel.add(sendMessageButton);
		bottomPanel.add(connectToServerButton);
		this.add(new JScrollPane(tbOutput));
		this.add(bottomPanel, BorderLayout.SOUTH);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		tbInput.requestFocusInWindow();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new ClientGUI();
	}
	
	public void connectToServer() {
		// Create new client
		client = new ClientStub(9000, "localhost");
	}

	public boolean validCommand(String cmd) {
		cmd = cmd.toUpperCase();
		if(cmd.length() >= 4) {
			String cmdChars = cmd.substring(0, 4);

			if (!cmdChars.equals("STAT") &&
				!cmdChars.equals("LIST") &&
				!cmdChars.equals("HAIL") &&
				!cmdChars.equals("IDEN") &&
				!cmdChars.equals("MESG") &&
				!cmdChars.equals("QUIT")) {
				return false; // Not 1 of the 6 server commands
			}

			if(cmdChars.equals("HAIL") ||
					cmdChars.equals("IDEN") ||
					cmdChars.equals("MESG")) {

				if (cmd.length() > 5) {
					if (!cmd.substring(4, 5).equals(" ")) {
						return false;
					}
				} else {
					return false;
				}

				if(cmdChars.equals("MESG")) {
					if(cmd.length() > 6) {
						// show that it has 2 spaces in
						if(!cmd.substring(5).contains(" ")) {
							return false;
						}
					} else {
						return false;
					}
				}
			}

		} else {
			return false;
		}
		return true;
	}

	public class StartServer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Start")) {
				connectToServer();
			}
		}
	}

	public class SendMessage implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Send")) {
				String serverResponse = "";

				if(validCommand(tbInput.getText())) {
					client.sendMessage(tbInput.getText()); // Send message
					serverResponse = client.getLastServerResponse(); // Get Response
				} else {
					serverResponse = ">Invalid Command";
				}

				// Wipe the current text box
				tbInput.setText("");

				// Update the chat list with response
				tbOutput.insert(serverResponse + "\n", tbOutput.getText().length());

				tbOutput.setCaretPosition(tbOutput.getText().length());

				// Set the current text box to still be in focus.
				tbInput.requestFocusInWindow();
			}
		}
	}
}