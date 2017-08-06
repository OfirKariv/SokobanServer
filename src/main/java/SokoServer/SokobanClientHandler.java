package SokoServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.example.SokobanServer.model.AdminModel;

import Plannable.PredicateLevelBuilder;
import Plannable.SokobanPlannable;
import Predicate.Action;
import SearchLib.SearchAction;
import common.Level;
import stripsLib.Plannable;
import stripsLib.Planner;
import stripsLib.Strips;

public class SokobanClientHandler implements ClientHandler {

	private Level level;

	private static final long serialVersionUID = 4999957715502665288L;

	public void handleClient(Socket socket, InputStream in, OutputStream out) {

		String str = null;
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;

		try {
			ois = new ObjectInputStream(in);

			oos = new ObjectOutputStream(out);

			Level input = (Level) ois.readObject();

			String userName = socket.getInetAddress().toString();
			AdminModel.getInstance().addClient(userName, socket);

			// ask the web service if has a solution V
			// if true we need to return the solustion in the outputstream V
			// if false we need to send the level to the planner and plannable V
			if (input instanceof Level) {

				this.level = (Level) input;
				String sol = getSolutionFromService(level.generateName());

				if (sol == null) {// not in service DB

					LinkedList<Action> list = findSolution(level);

					

					if (list != null) {

						 str = solInNum(list);
						 sendSolutionToService(level.generateName(), str);
						oos.writeObject(toListString(str));

					} else {// cant solve the level --no solution
						oos.writeObject(null);// ("there is no solution");

					}

				} else {// the solution is in the web service we just need to
						// send to the client
					oos.writeObject(toListString(sol));
					

				}
			}
			// if (input instanceof highScore){///instance of highscore

			// }

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {

				if (oos != null)
					oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public LinkedList<Action> findSolution(Level level) {
		PredicateLevelBuilder plb = new PredicateLevelBuilder();
		LinkedList<Action> list = new LinkedList<>();
		Planner strips = new Strips();
		Plannable p = new SokobanPlannable(plb, level);
		list = (LinkedList<Action>) strips.plan(p);

		return list;

	}

	public void sendSolutionToService(String levelName, String solution) {
		String url = "http://localhost:8080/SokobanWS/webapi/solutions?levelID=" + levelName + "&solution=" + solution;
		System.out.println(url);
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(url);
		Response response = webTarget.request(MediaType.TEXT_PLAIN).get(Response.class);
		
		
		

	}

	public String getSolutionFromService(String levelName) throws IOException {
		String url = "http://localhost:8080/SokobanWS/webapi/solutions/" + levelName;
		Client client = ClientBuilder.newClient();

		WebTarget webTarget = client.target(url);

		Response response = webTarget.request(MediaType.TEXT_PLAIN).get(Response.class);
		
		
			
		
		if (response.getStatus() == 200) {
			String solution = response.readEntity(new GenericType<String>() {
			});

			System.out.println("handler found webservice solution: " + solution);

			return solution;

		} else if (response.getStatus() == 204) {
			System.out.println("could not find level in DB");
			return null;

		} else {
			System.out.println(response.getHeaderString("errorResponse"));
			throw new IOException("Could not reach web service");
		}
	}

	public String solInNum(List<Action> list) {
		StringBuilder strBld = new StringBuilder();

		for (Action a : list) {
			ArrayList<SearchAction> arr = a.getTheRealMoves();
			for (SearchAction sa : arr) {

				switch (sa.getAction()) {
				case "Move up": {
					strBld.append(8);
					break;
				}
				case "Move down": {
					strBld.append(2);
					break;
				}
				case "Move right": {
					strBld.append(6);
					break;
				}
				case "Move left": {
					strBld.append(4);
					break;
				}
				default:
					break;
				}
			}
		}
		System.out.println(strBld.toString());
		return strBld.toString();
	}

	public LinkedList<String> toListString(String nums) {

		char temp;
		LinkedList<String> outputList = new LinkedList<>();

		for (int i = 0; i < nums.length(); i++) {
			temp = nums.charAt(i);
			switch (temp) {
			case '8':
				outputList.add("up");
				break;
			case '2':
				outputList.add("down");
				break;
			case '4':
				outputList.add("left");
				break;
			case '6':
				outputList.add("right");
				break;

			default:
				break;
			}

		}

		return outputList;
	}

	public LinkedList<String> toStringList(List<Action> list) {
		LinkedList<String> strList = new LinkedList<>();
		for (Action a : list) {
			ArrayList<SearchAction> arr = a.getTheRealMoves();
			for (SearchAction sa : arr) {

				{
					String temp = sa.getAction();
					temp = temp.substring(5);
					
					strList.add(temp);
				}
			}
		}

		return strList;
	}

}
