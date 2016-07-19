package comms;

import java.util.ArrayList;

public class CommsServer {

	public static void main(String[] args) throws Exception {
		SocketServer srv = new SocketServer(9090);
		srv.start();

		System.out.println("SERVER has started!");
		boolean isEnabled = true;
		while (isEnabled)

		{
			if (srv.isMessage()) {
				ArrayList<String> msgs = srv.getMessages();
				for (String string : msgs) {
					System.out.println("SERVER received: " + string);
					RemoteEvent evt = new RemoteEvent(string);
					if (evt.getContent().contains("EXIT")) {
						srv.stopServer();
						isEnabled = false;
					}
				}
			}
			Thread.sleep(1000);
		}

	}
}
