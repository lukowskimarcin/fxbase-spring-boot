package fxbase;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.application.Preloader.StateChangeNotification.Type;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Component
public abstract class AbstractPreloaderView extends Preloader {
	protected Stage preloaderStage;
	protected double width = 600;
	protected double height = 400;
	
	protected ProgressBar loadProgress;
	
	/**
	 * Splash screen background
	 * @return
	 */
	public abstract Image getBackground();
	
	public String getTitle(){
		return null;
	}
	
	public boolean isContinousProgress(){
		return true;
	}
	
	public Parent getView() {
		Image background = getBackground();
		
		ImageView splash = new ImageView(getBackground());
        loadProgress = new ProgressBar();
        loadProgress.setPrefHeight(20);
        
        Pane splashLayout = new VBox();
        splashLayout.getChildren().addAll(splash, loadProgress);
        splashLayout.setEffect(new DropShadow());
		
        width = background.getWidth();
		height = background.getHeight() + loadProgress.getPrefHeight();
		loadProgress.setPrefWidth(width);
        
		return  splashLayout;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		preloaderStage = primaryStage;
		primaryStage.initStyle(StageStyle.UNDECORATED);
		
		Image icon = AbstractJavaFxApplication.getDefaultIcon();
		if(icon!=null){
			primaryStage.getIcons().add(icon);
		}

		Scene scene = new Scene(getView(), Color.TRANSPARENT);
		primaryStage.setWidth(width);
		primaryStage.setHeight(height);
		primaryStage.setScene(scene);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.centerOnScreen();
		
		primaryStage.setTitle(getTitle());
		primaryStage.show();
	}
	
	
	@Override
    public void handleApplicationNotification(PreloaderNotification info) {
        if (info instanceof ProgressNotification && !isContinousProgress()) {
        	Platform.runLater(()-> {
        		loadProgress.setProgress(((ProgressNotification) info).getProgress());
        	});
        }
    }

	@Override
	public void handleStateChangeNotification(StateChangeNotification stateChangeNotification) {
		if (stateChangeNotification.getType() == Type.BEFORE_START) {
			preloaderStage.hide();
		}
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

}
