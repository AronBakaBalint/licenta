package aron.utcn.licenta.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class LicensePlateReceiver implements Runnable {

	private static ServerSocket server;
	
	private static int port = 9876;
	
	@Override
	public void run() {
		try {
			waitForLicensePlate();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void waitForLicensePlate() throws IOException, ClassNotFoundException {
		server = new ServerSocket(port);
		while(true) {
			Socket socket = server.accept();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String message = (String)ois.readObject();
			System.out.println(message);
			ois.close();
			socket.close();
		}
	}

}
