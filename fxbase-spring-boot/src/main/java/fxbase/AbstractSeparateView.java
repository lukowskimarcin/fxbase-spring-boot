package fxbase;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class AbstractSeparateView extends AbstractView {
	private Stage stage;
	private Stage owner;
	private Scene scene;
	private Parent root;
	
	public AbstractSeparateView initOwner(Stage owner) {
		this.owner = owner;
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
	
	public AbstractSeparateView center() {
		if (owner != null) {
			stage.setOnShown( e -> {
				double centerXPosition = owner.getX() + owner.getWidth() / 2d;
				double centerYPosition = owner.getY() + owner.getHeight() / 2d;
				
				double childXPosition = centerXPosition - stage.getWidth() / 2d;
				double childYPosition = centerYPosition - stage.getHeight() / 2d;
				
				stage.setX(childXPosition);
				stage.setY(childYPosition);
			});
		}
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
			Image icon = AbstractJavaFxApplication.getDefaultIcon();
			if(icon!=null){
				stage.getIcons().add(icon);	
			}
		}
		return stage;
	}
	
	public void close(){
		getStage().close();
		stage = null;
	}
	

}
