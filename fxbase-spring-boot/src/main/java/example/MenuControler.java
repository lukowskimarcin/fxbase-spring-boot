package example;

import java.io.FileNotFoundException;


import org.springframework.beans.factory.annotation.Autowired;

import fxbase.AbstractControler;
import fxbase.AbstractView;
import fxbase.FXMLView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

@FXMLView("/fxml/Menu.fxml")
public class MenuControler extends AbstractView {

	@Autowired
	private Starter starter;
	
	@Autowired
	private MainControler main;
	
	@FXML
	private MenuItem mCompress;

	@FXML
	private MenuItem mDecompress;

	@FXML
	private MenuItem mClose;

	@FXML
	private MenuItem mAbout;

	
	@FXML
	void closeAction(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void onAbout(ActionEvent event) throws FileNotFoundException {
//		TestControler controler =  starter.loadView(TestControler.class);
//		main.getPane().setCenter(controler.getView());

		//System.out.println("onAbout: " + controler.getText());
	}

	@FXML
	void onCompress(ActionEvent event) {
		System.out.println("onCompress");
	}

	
	@FXML
	void onDecompress(ActionEvent event) {
		System.out.println("onDecompress"); 
	}

}
