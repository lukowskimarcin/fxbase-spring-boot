package fxbase;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AbstractSeparateView extends AbstractView {
	private Stage stage;
	
	public AbstractSeparateView(Stage owner, Modality modality){
		stage = new Stage();
		stage.initModality(modality);
		if(owner!=null) {
			stage.initOwner(owner);
		}
	}
	
	public void show() {
		Scene scene = new Scene(getView());
		stage.setScene(scene);
		stage.show();
		
	}
	
	public void showAndWait() {
		Scene scene = new Scene(getView());
		stage.setScene(scene);
		stage.showAndWait();
	}
	
	
	public Stage getStage() {
		return stage;
	}

}
