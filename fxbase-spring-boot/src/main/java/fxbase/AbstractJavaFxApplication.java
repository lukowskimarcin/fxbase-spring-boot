package fxbase;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public abstract class AbstractJavaFxApplication extends Application implements IFxmlLoader {
	
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
		showView(savedInitialView);
		
		stage.setScene(scene);
		stage.setResizable(true);
		stage.centerOnScreen();
		stage.show();
	}
	
	@Override	
	public <T> T loadView(Class<? extends AbstractView> newView) {
		return loadView(newView, true);
	}
	 
	
	@Override	
	@SuppressWarnings("unchecked")
	public <T> T loadView(Class<? extends AbstractView> newView, boolean reload) {
		AbstractView view = applicationContext.getBean(newView);
		return (T)view;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T showView(Class<? extends AbstractView> newView) {
		AbstractView view = applicationContext.getBean(newView);
		stage.titleProperty().bind(view.titleProperty());
		if (AbstractJavaFxApplication.scene == null) {
			AbstractJavaFxApplication.scene = new Scene(view.getView());
		}
		else {  
			AbstractJavaFxApplication.scene.setRoot(view.getView());
		}
		
		return (T)view;
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
