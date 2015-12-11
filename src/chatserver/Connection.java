package chatserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Connection implements Runnable {

	final static int STATE_UNREGISTERED = 0;
	final static int STATE_REGISTERED = 1;

	private volatile boolean running;
	private int messageCount;
	private int state;
	private Socket client;
	private Server serverReference;
	private BufferedReader in;
	private PrintWriter out;
	private String username;

	Connection (Socket client, Server serverReference) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.Connection()");
		this.serverReference = serverReference;
		this.client = client;
		this.state = STATE_UNREGISTERED;
		messageCount = 0;
	}

	public void run(){
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.run()");
		String line;
		try {
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			out = new PrintWriter(client.getOutputStream(), true);
		} catch (IOException e) {
			System.out.println("in or out failed");
			System.exit(-1);
		}
		running = true;
		this.sendOverConnection("OK Welcome to the chat server, there are currently " + serverReference.getNumberOfUsers() + " user(s) online");
		while(running) {
			try {
				line = in.readLine();
				validateMessage(line);
			} catch (IOException e) {
				System.out.println("Read failed");
				System.exit(-1);
			}
		}
	}

	private void validateMessage(String message) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.validateMessage()");

		if(message.length() < 4){
			sendOverConnection ("BAD invalid command to server");
		} else {
			switch(message.substring(0,4)){
				case "LIST":
					list();
					break;

				case "STAT":
					stat();
					break;

				case "IDEN":
					iden(message.substring(5));
					break;

				case "HAIL":
					hail(message.substring(5));
					break;

				case "MESG":
					mesg(message.substring(5));
					break;

				case "QUIT":
					quit();
					break;

				default:
					sendOverConnection("BAD command not recognised");
					break;
			}
		}

	}

	private void stat() {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.stat()");
		String status = "There are currently "+serverReference.getNumberOfUsers()+" user(s) on the server ";
		switch(state) {
			case STATE_REGISTERED:
				status += "You are logged in and have sent " + messageCount + " message(s)";
				break;

			case STATE_UNREGISTERED:
				status += "You have not logged in yet";
				break;
		}
		sendOverConnection("OK " + status);
	}

	private void list() {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.list()");
		switch(state) {
			case STATE_REGISTERED:
				ArrayList<String> userList = serverReference.getUserList();
				String userListString = new String();
				for(String s: userList) {
					userListString += s + ", ";
				}
				sendOverConnection("OK " + userListString);
				break;

			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}

	}

	private void iden(String message) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.iden()");
		switch(state) {
			case STATE_REGISTERED:
				sendOverConnection("BAD you are already registered with username " + username);
				break;

			case STATE_UNREGISTERED:
				String username = message.split(" ")[0];
				if(serverReference.doesUserExist(username)) {
					sendOverConnection("BAD username is already taken");
				} else {
					this.username = username;
					state = STATE_REGISTERED;
					sendOverConnection("OK Welcome to the chat server " + username);
				}
				break;
		}
	}

	private void hail(String message) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.hail()");
		switch(state) {
			case STATE_REGISTERED:
				serverReference.broadcastMessage("Broadcast from " + username + ": " + message);
				messageCount++;
				break;

			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}
	}

	public boolean isRunning(){
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.isRunning()");
		return running;
	}

	private void mesg(String message) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.mesg()");

		switch(state) {
			case STATE_REGISTERED:

				if(message.contains(" ")) {
					int messageStart = message.indexOf(" ");
					String user = message.substring(0, messageStart);
					String pm = message.substring(messageStart+1);
					if(serverReference.sendPrivateMessage("PM from " + username + ":" + pm, user)){
						sendOverConnection("OK your message has been sent");
					} else {
						sendOverConnection("BAD the user does not exist");
					}
				}
				else{
					sendOverConnection("BAD Your message is badly formatted");
				}
				break;

			case STATE_UNREGISTERED:
				sendOverConnection("BAD You have not logged in yet");
				break;
		}
	}

	private void quit() {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.quit()");
		switch(state) {
			case STATE_REGISTERED:
				sendOverConnection("OK thank you for sending " + messageCount + " message(s) with the chat service, goodbye. ");
				break;
			case STATE_UNREGISTERED:
				sendOverConnection("OK goodbye");
				break;
		}
		running = false;
		try {
			client.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		serverReference.removeDeadUsers();
	}

	private synchronized void sendOverConnection (String message){
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.sendOverConnection()");
		out.println(message);
	}

	public void messageForConnection (String message){
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.messageForConnection()");
		sendOverConnection(message);
	}

	public int getState() {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.getState()");
		return state;
	}

	public String getUserName() {
		if (ProjectSettings.print_server_method_calls) System.out.println("Connection.getUserName()");
		return username;
	}

}

