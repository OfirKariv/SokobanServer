package com.example.SokobanServer.view;

import com.example.SokobanServer.model.AdminModel;
import com.example.SokobanServer.viewmodel.DashboardViewModel;

import SokoServer.Server;
import SokoServer.SokobanClientHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Hello world!
 *
 */
public class App extends Application
{
	 public static void main( String[] args )
	 
	 
	    {
		 System.out.println("=======================================");
	        Server server = new Server(2222, new SokobanClientHandler());
	        try {
	        	new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							server.runServer();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				
	        	}).start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        launch(args);
	    }

		@Override
		public void start(Stage stage) throws Exception {
			
			AdminModel model = AdminModel.getInstance();
			DashboardViewModel vm = new DashboardViewModel(model);
			model.addObserver(vm);

			FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminDashboard.fxml"));
	
			BorderPane root = (BorderPane) loader.load();	
			
	
			
		    DashboardController controller = loader.getController();
			controller.setViewModel(vm);
	        
	        Scene scene = new Scene(root, 300, 275);
	    
	        stage.setTitle("Admin Dashboard");
	        stage.setScene(scene);
	        stage.show();
		}
}
