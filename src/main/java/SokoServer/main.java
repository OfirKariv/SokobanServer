package SokoServer;

public class main {

	public static void main(String[] args) {
		
		
		SokobanClientHandler ch = new SokobanClientHandler();
		Server server = new Server(2222, ch);
		try {
			server.runServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
