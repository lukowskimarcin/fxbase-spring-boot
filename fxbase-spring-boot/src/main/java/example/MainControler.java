package example;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fxbase.AbstractControler;
import fxbase.AbstractView;
import fxbase.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

@Component
public class MainControler  {

	@Autowired
	private MenuControler menu;
	
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
		starter.showView(TestControler.class);
	}
	
	@PostConstruct
	public void init() {
		System.out.println("@PostConstruct MainControler ");
//		BorderPane borderPane = (BorderPane)getView();
//		borderPane.setTop(menu.getView());
//		s
		
	
		Test a = new Test();
		System.out.println("a: " + a);
		Test b = new Test();
		System.out.println("b: " + b);
		
		System.out.println("x: " + x);
		System.out.println("y: " + y);
		
	}
	

//	public BorderPane getPane() {
//		return  (BorderPane)getView();
//	}
//	
	 
}
