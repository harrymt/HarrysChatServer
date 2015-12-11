package harry;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;


public class Server {

	private ServerSocket server;
	private ArrayList<Connection> list;
	public volatile boolean shouldServerListen = true;

	public Server (int port) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.Server()");
		try {
			server = new ServerSocket(port);
			System.out.println("Server has been initialised on port " + port);
		}
		catch (IOException e) {
			System.err.println("error initialising server");
			e.printStackTrace();
		}
		list = new ArrayList<Connection>();

		new Thread(new ServerListener(this)).start();
	}

	class ServerListener implements Runnable {
		Server serverRef;

		public ServerListener(Server ref) {
			this.serverRef = ref;
		}

		public void run() {
			if (ProjectSettings.print_server_method_calls) System.out.println("Server.ServerListener.run()");
			
			while(shouldServerListen) {
				Connection c = null;
				try {
					c = new Connection(server.accept(), serverRef);
				}
				catch (IOException e) {
					System.err.println("error setting up new client connection");
					e.printStackTrace();
				}
				Thread t = new Thread(c);
				t.start();
				list.add(c);
			}
			if (ProjectSettings.print_server_method_calls) System.out.println("Sever has stopped listening to connections");
			try {
				// Force the server to close
				server.close();
			} catch (IOException e) { System.out.println("Testing failed"); }
		}
	}

	public ArrayList<String> getUserList() {
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.getUserList()");
		ArrayList<String> userList = new ArrayList<String>();
		for( Connection clientThread: list){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				userList.add(clientThread.getUserName());
			}
		}
		return userList;
	}

	public boolean doesUserExist(String newUser) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.doesUserExist()");
		boolean result = false;
		for( Connection clientThread: list){
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				result = clientThread.getUserName().compareTo(newUser)==0;
			}
		}
		return result;
	}

	public void broadcastMessage(String theMessage){
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.broadcastMessage()");
		System.out.println(theMessage);
		for( Connection clientThread: list){
			clientThread.messageForConnection(theMessage + System.lineSeparator());
		}
	}

	public boolean sendPrivateMessage(String message, String user) {
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.sendPrivateMessage()");
		for( Connection clientThread: list) {
			if(clientThread.getState() == Connection.STATE_REGISTERED) {
				if(clientThread.getUserName().compareTo(user)==0) {
					clientThread.messageForConnection(message + System.lineSeparator());
					return true;
				}
			}
		}
		return false;
	}

	public void removeDeadUsers(){
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.removeDeadUsers()");
		Iterator<Connection> it = list.iterator();
		while (it.hasNext()) {
			Connection c = it.next();
			if(!c.isRunning())
				it.remove();
		}
	}

	public int getNumberOfUsers() {
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.getNumberOfUsers()");
		return list.size();
	}

	protected void finalize() throws IOException {
		if (ProjectSettings.print_server_method_calls) System.out.println("Server.finalize()");
		server.close();
	}

}
