package fxbase;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 * 
 * TODO: Otwieranie w trybie jednego okna
 * TODO: Otwieranie wielu okien, kazde inne
 *
 */

public class AbstractSeparateView extends AbstractView {
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public AbstractSeparateView(){
		 
	}
	
	public AbstractSeparateView initOwner(Stage owner) {
		getStage().initOwner(owner);
		return this;
	}
	
	public AbstractSeparateView title(String title) {
		getStage().setTitle(title);
		return this;
	}
	
	
	public AbstractSeparateView initModality(Modality modality) {
		getStage().initModality(modality);
		return this;
	}
	
	
	public void show() {
		getStage().setScene(getScene());
		getStage().titleProperty().bind(titleProperty());
		getStage().show();
	}
	
	public void showAndWait() {
		getStage().setScene(getScene());
		getStage().titleProperty().bind(titleProperty());
		getStage().showAndWait();
	}
	
	public Scene getScene(){
		if(root==null) {
			root = getView();
		}
		
		if(root.getScene()!=null) {
			scene = root.getScene();
		} else {
			scene = new Scene(root);
		}
		return scene;
	}
	
	public Stage getStage() {
		if(stage==null) {
			stage = new Stage();
		}
		return stage;
	}
	
	public void close(){
		getStage().close();
		stage = null;
	}
	

}
