package example;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import fxbase.AbstractView;
import fxbase.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import service.Test;

@FXMLView(fxml="/fxml/Main.fxml")
public class MainControler extends AbstractView  {

	@Autowired
	private MenuControler menu;
	
	@Autowired 	private MenuControler menu2;
	
	@Autowired
	private Starter starter;

	@FXML
	private Button mButton;
	
	@Autowired
	private Test x;
	
	@Autowired
	private Test y;

	@FXML
	void show(ActionEvent event) {
		starter.showScene(TestControler.class);
	}
	
	@PostConstruct
	public void init() {
		System.out.println("@PostConstruct MainControler ");
		
		setTitle("Example");
		
		BorderPane borderPane = (BorderPane)getView();
		borderPane.setTop(menu.getView());
		borderPane.setBottom(menu2.getView());
	
		Test a = new Test();
		System.out.println("a: " + a);
		Test b = new Test();
		System.out.println("b: " + b);
		
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		
		
		borderPane.setRight(new Button("wygenerowany"));
	}

	public BorderPane getPane() {
		return  (BorderPane)getView();
	}
	 
}
