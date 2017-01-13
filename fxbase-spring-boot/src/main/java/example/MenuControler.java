package example;

import java.io.FileNotFoundException;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import fxbase.AbstractView;
import fxbase.ControlerCreateMode;
import fxbase.FXMLView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

@FXMLView(value="/fxml/Menu.fxml", bundle=".fxml/menux")
@Scope("prototype")
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
		TestControler controler =  loadView(TestControler.class, ControlerCreateMode.SEPARATE );
		main.getPane().setCenter(controler.getView());

		TestControler controler2 =  loadView(TestControler.class);
		main.getPane().setLeft(controler2.getView());
	}

	@FXML
	void onCompress(ActionEvent event) {
		System.out.println("onCompress");
		main.reload();
		starter.showScene(MainControler.class);
		main.init();
		
	}

	
	@FXML
	void onDecompress(ActionEvent event) {
		System.out.println("onDecompress"); 
	}

}
