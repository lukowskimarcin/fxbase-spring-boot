package example;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import fxbase.AbstractView;
import fxbase.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@FXMLView("/fxml_test/Test.fxml")
@Scope("prototype")
public class TestControler extends AbstractView {
	
	@Autowired
	private Starter starter;
	
	private static int count = 0;
	private int x;
	
	
	public TestControler(){
		count++;
		x= count;
		
		System.out.println("Construct TestControler: " + this);
	}
	
	
	@Override
	public String toString() {
		return ""+  x;
	}
	
	@FXML
	private Button mButton;

	@FXML
	void show(ActionEvent event) {
		starter.showScene(MainControler.class);
	}

	
}
