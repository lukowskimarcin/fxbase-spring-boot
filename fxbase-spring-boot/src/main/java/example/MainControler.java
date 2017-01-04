package example;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import fxbase.AbstractControler;
import fxbase.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

@FXMLView("/fxml/Main.fxml")
public class MainControler extends AbstractControler {

	@Autowired
	private MenuControler menu;
	
	@Autowired
	private Starter starter;


	@FXML
	private Button mButton;

	@FXML
	void show(ActionEvent event) {
		 
		
		BorderPane borderPane = (BorderPane)getView();
		borderPane.setTop(menu.getView());
	}
	
	@PostConstruct
	private void init() {
		System.out.println("MainControler: " + starter.hashCode());
		BorderPane borderPane = (BorderPane)getView();
		borderPane.setTop(menu.getView());
	}
	

	
	 
}
