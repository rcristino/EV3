package comms;

import java.io.PrintWriter;
import java.net.Socket;

public class SocketClient {

	private String serverAddress = null;
	int port;
	private Socket socket = null;

	public SocketClient(String _serverAddress, int _port) {
		this.serverAddress = _serverAddress;
		this.port = _port;
		try {
			socket = new Socket(serverAddress, 9090);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String msg) {
		try {
			if (socket == null) {
				throw new Exception("ERROR: target IP not set");
			}

			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
