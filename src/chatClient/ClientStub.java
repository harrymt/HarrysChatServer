package chatClient;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import chatserver.ProjectSettings;

public class ClientStub {
	private BufferedReader in;
	private PrintWriter out;

	private Socket socket;

	String address;
	int port;

	// The last message the server responded with.
	private String lastServerResponse;
	private ArrayList<String> lastServerResponses = new ArrayList<>();

	public ClientStub(int port, String address) {
		this.port = port;
		this.address = address;
		
		if (!this.start())
			throw new RuntimeException();
	}

	public ArrayList<String> getLastArrayServerResponses() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // Sleep to guarantee that we have received all server messages
		return lastServerResponses;
	}

	public String getLastServerResponse() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // Sleep to guarantee that we have received all server messages
		return lastServerResponse;
	}

	public void sendMessage(String msg) {
		out.println(msg);
		if (msg.length() >= 4 && msg.substring(0, 4) == "QUIT") {
			disconnect();
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public boolean start() {
		try {
			socket = new Socket(address, port);
		} catch (Exception e) {
			System.out.println("Error connecting to server:" + e);
			return false;
		}

		if (ProjectSettings.print_client_messages)
			System.out.println("Connectected to " + socket.getInetAddress() + ":" + socket.getPort());

		try {
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: " + eIO);
			return false;
		}

		// creates the Thread to listen from the server
		new Thread(new ListenFromServer()).start();
		return true;
	}

	/*
	 * When something goes wrong Close the Input/Output streams and disconnect
	 * not much to do in the catch clause
	 */
	void disconnect() {
		if (ProjectSettings.print_client_messages)
			System.out.println("disconnect()");
		try {
			if (in != null) {
				in.close();
			} // Close input stream
		} catch (IOException e) {
			System.out.println(e);
		}

		if (out != null)
			out.close();

		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	class ListenFromServer implements Runnable {
		public void run() {
			while (true) {
				try {
					String temp = in.readLine();
					if (ProjectSettings.print_client_messages)
						System.out.println("> " + temp);

					if (temp == null) {
						break;
					}
					lastServerResponses.add(lastServerResponse);
					lastServerResponse = temp;
				} catch (IOException e) {
					if (ProjectSettings.print_client_messages)
						System.out.println("> Client stopped listening" + e); // Closed connection
					
					break;
				}
			}
		}
	}
}
