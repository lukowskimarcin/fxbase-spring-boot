package fxbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import utils.DialogsUtil;


public abstract class AbstractJavaFxApplication extends Application  {
	
	private static String[] savedArgs;

	private static Class<? extends AbstractView> savedInitialView;

	private static ConfigurableApplicationContext applicationContext;
	
	private static Locale locale;
	private static List<String> defaultCSS;

	private static Stage stage;
	private static Scene scene; 

	@Override
	public void init() throws Exception {
		Class<? extends AbstractJavaFxApplication> app = getClass();
		applicationContext = SpringApplication.run(app, savedArgs);
	}

	@Override
	public void start(Stage stage) throws Exception {
		AbstractJavaFxApplication.stage = stage;
		
		showScene(savedInitialView);
		
		stage.setScene(scene);
		stage.setResizable(true);
		stage.centerOnScreen();
		
		List<Image> icons = loadIcons();
		if(icons!=null && !icons.isEmpty()){
			stage.getIcons().addAll(loadIcons());	
			DialogsUtil.defaultIcon(icons.get(0));
		}
		stage.show();
	}
	
	
	protected abstract List<Image> loadIcons();
	
	public static Image getDefaultIcon(){
		Image icon = null;
		if(stage!=null && stage.getIcons().size()>0) {
			icon = stage.getIcons().get(0);
		}
		return icon;
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
	
	public static void setLocale(Locale locale) {
		AbstractJavaFxApplication.locale = locale;
	}
	
	public static Locale getLocale() {
		if(locale==null) {
			return Locale.getDefault();
		}
		return locale;
	}
	
	public static void addDefaultCSS(String css) {
		if(defaultCSS==null) {
			defaultCSS = new ArrayList<String>();
		}
		defaultCSS.add(css);
	}
	
	public static List<String> getDefaultCSS() {
		return defaultCSS;
	}
}
