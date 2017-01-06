package example;


import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import fxbase.AbstractControler;
import fxbase.FXMLView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

@FXMLView("/fxml/Test.fxml")
public class TestControler extends AbstractControler {
	
	@Autowired
	private Starter starter;
	
	
	private String text;
	
	public TestControler(){
		Date date = new Date();
		text = "" + date.getTime();
	}
	
	
	@FXML
	private Button mButton;

	@FXML
	void show(ActionEvent event) {
		MainControler x = starter.showView(MainControler.class);
		x.init();
	}

	
	public String getText() {
		return text;
	}
}
