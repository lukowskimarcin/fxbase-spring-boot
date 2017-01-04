package fxbase;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public abstract class AbstractJavaFxApplication extends Application {
	
	private static String[] savedArgs;

	private static Class<? extends AbstractControler> savedInitialView;

	private ConfigurableApplicationContext applicationContext;

	private Stage stage;
	private Scene scene; 

	@Override
	public void init() throws Exception {
		Class<? extends AbstractJavaFxApplication> app = getClass();
		applicationContext = SpringApplication.run(app, savedArgs);
		System.out.println("init end");
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		showView(savedInitialView);
	}

	public void showView(Class<? extends AbstractControler> newView) {
		AbstractControler view = applicationContext.getBean(newView);
		stage.titleProperty().bind(view.titleProperty());
		if (scene == null) {
			scene = new Scene(view.getView());
		}
		else {  
			scene.setRoot(view.getView());
		}
		
		// stage.setTitle(windowTitle);
		stage.setScene(scene);
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		applicationContext.close();
	}

	protected static void launchApp(Class<? extends AbstractJavaFxApplication> appClass, Class<? extends AbstractControler> view, String[] args) {
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
