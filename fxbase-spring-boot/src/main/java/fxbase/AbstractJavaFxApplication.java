package fxbase;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public abstract class AbstractJavaFxApplication extends Application  {
	
	private static String[] savedArgs;

	private static Class<? extends AbstractView> savedInitialView;

	private static ConfigurableApplicationContext applicationContext;

	private static Stage stage;
	private static Scene scene; 

	@Override
	public void init() throws Exception {
		Class<? extends AbstractJavaFxApplication> app = getClass();
		applicationContext = SpringApplication.run(app, savedArgs);
		System.out.println("init end");
	}

	@Override
	public void start(Stage stage) throws Exception {
		AbstractJavaFxApplication.stage = stage;
		showScene(savedInitialView);
		
		stage.setScene(scene);
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
	}
	
	
	
	public void showScene(Class<? extends AbstractView> newView) {
		AbstractView view = applicationContext.getBean(newView);
		stage.titleProperty().bind(view.titleProperty());
		
		if (AbstractJavaFxApplication.scene == null) {
			AbstractJavaFxApplication.scene = new Scene(view.getView());
		}
		else {  
			AbstractJavaFxApplication.scene.setRoot(view.getView());
		}
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		applicationContext.close();
	}

	protected static void launchApp(Class<? extends AbstractJavaFxApplication> appClass, Class<? extends AbstractView> view, String[] args) {
		savedInitialView = view;
		savedArgs = args;
		Application.launch(appClass, args);
	}
	
	
	public Stage getStage() {
		return stage;
	}
	
	public Scene getScene() {
		return scene;
	}
}
