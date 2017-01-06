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
		starter.showView(TestControler.class);
	}
	
	@PostConstruct
	public void init() {
		System.out.println("@PostConstruct MainControler ");
		BorderPane borderPane = (BorderPane)getView();
		borderPane.setTop(menu.getView());
	}
	

	public BorderPane getPane() {
		return  (BorderPane)getView();
	}
	
	 
}
