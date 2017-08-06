package com.example.SokobanServer.view;

import com.example.SokobanServer.viewmodel.DashboardViewModel;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class DashboardController {
	@FXML
    private Button button;
    
    @FXML
    private Label label;

    @FXML
    private ListView<String> myListView;
   
    private DashboardViewModel vm;
    
    public void setViewModel(DashboardViewModel vm) {
    	this.vm = vm;
    	myListView.itemsProperty().bind(vm.clientsList);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {        
    	myListView.getItems().remove(myListView.getSelectionModel().getSelectedItem());
    } 
    
    
    public DashboardController(){}
}
