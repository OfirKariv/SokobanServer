package com.example.SokobanServer.model;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class AdminModel extends Observable {
	 private Map<String,Socket> connectedClients=new HashMap<>();

	    private static final AdminModel instance=new AdminModel();
	    private AdminModel(){}
	    public static AdminModel getInstance(){return instance;}

		public void addClient(String userName, Socket socket) {
			connectedClients.put(userName, socket);
			setChanged();
			List<String> params = new LinkedList<String>();
			params.add("Add");
			params.add(userName);
			notifyObservers(params);
		}
		
		public void removeClient(String userName) {
			connectedClients.remove(userName);
		}
		
	    public List<String> getClients()
	    {
	       List<String> clients=new ArrayList<>();
	        clients.addAll(connectedClients.keySet());
	        return clients;
	    }

	    public void disconnectClient(String userName) {
			Socket socket = connectedClients.get(userName);
			try {
				socket.close();
				connectedClients.remove(userName);
				setChanged();
				List<String> params = new LinkedList<String>();
				params.add("Remove");
				params.add(userName);
				notifyObservers(params);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
}
