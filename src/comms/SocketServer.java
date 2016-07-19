package comms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class SocketServer extends Thread {

	private ServerSocket listener = null;
	private boolean isEnabled = false;
	private ArrayList<String> msgQueue = null;

	public SocketServer(int port) {
		super("SERVER");
		try {
			listener = new ServerSocket(port);
			msgQueue = new ArrayList<String>();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isEnabled = true;
	}

	@Override
	public void run() {
		super.run();
		try {
			if (!this.isEnabled) {
				throw new Exception("ERROR: Socket it is not initialised!");
			}

			Socket socket = listener.accept();
			while (this.isEnabled) {

				// READ
				int numMsg = socket.getInputStream().available();
				if (numMsg > 0) {
					BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String answer = input.readLine();
					msgQueue.add(answer);
				}
			}

			socket.close();
			listener.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList<String> getMessages() {
		ArrayList<String> result = new ArrayList<String>(this.msgQueue);
		this.msgQueue.clear();
		;
		return result;
	}

	public boolean isMessage() {
		boolean result = false;
		if (this.msgQueue.isEmpty() == false) {
			result = true;
		}
		return result;
	}

	public void stopServer() {
		this.isEnabled = false;

	}
}
