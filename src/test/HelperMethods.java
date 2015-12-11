package test;

import main.ClientStub;
import main.Server;

public class HelperMethods {

	static Server server;

	static class RunServer implements Runnable {
		public void run() {
			server = new Server(9000);
		}
	}

	static ClientStub spawnFastNewClient() {
		return new ClientStub(9000, "localhost");
	}

	static ClientStub spawnNewClient() {
		ClientStub newSpawn = new ClientStub(9000, "localhost");

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // Sleep, just to guarantee that the server has setup

		return newSpawn;
	}

	static ClientStub spawnNewRegisteredClient(String user) {
		ClientStub newSpawn = new ClientStub(9000, "localhost");

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} // Sleep, just to guarantee that the server has setup

		newSpawn.sendMessage("IDEN " + user);
		return newSpawn;
	}

	static Thread serverThread;

	static void startServer() {

		serverThread = new Thread(new RunServer());
		serverThread.start();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	static void stopServer() {
		server.shouldServerListen = false; // stop server from listening to new
											// connections
		// Send a throw-away new client to unblock the blocking server.accept
		// call.
		spawnNewClient();
		serverThread = null;
	}
}
