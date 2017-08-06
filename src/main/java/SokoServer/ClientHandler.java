package SokoServer;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public interface ClientHandler {
	public void handleClient(Socket socket,InputStream in, OutputStream out);
}
