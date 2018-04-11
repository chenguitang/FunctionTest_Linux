package com.posin.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.print.attribute.standard.Finishings;

public class ServerSocketManager {

	private static final ServerSocketManager SERVER_SOCKET_MANAGER_INSTANCE = new ServerSocketManager();

	private ServerSocketManager() {
	}

	public static ServerSocketManager getInstance() {
		return SERVER_SOCKET_MANAGER_INSTANCE;
	}

	public void startSocketServer(final SockectCallback sockectCallback) {
		new Thread() {
			public void run() {
				try {

					ServerSocket serverSocket = new ServerSocket(8888);
					Socket socket = null;

					System.out.println("***server starting , wait for the client's connection....");

					while (true) {
						socket = serverSocket.accept();
						ServerThread serverThread = new ServerThread(socket,sockectCallback);
						serverThread.start();
						InetAddress address = socket.getInetAddress();
						System.out.println("Currently connected to the client IP£º" + address.getHostAddress());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		}.start();
	}

}
