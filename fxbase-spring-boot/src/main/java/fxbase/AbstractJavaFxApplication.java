package fxbase;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import utils.DialogsUtil;


public abstract class AbstractJavaFxApplication extends Application  {
	
	protected static final Logger log = Logger.getLogger(AbstractJavaFxApplication.class.getName());   
	
	private static String[] savedArgs;

	private static Class<? extends AbstractView> savedInitialView;

	private static ConfigurableApplicationContext applicationContext;
	
	private static Locale locale;
	private static List<String> defaultCSS;

	private static Stage stage;
	private static Scene scene; 
	
	private static TrayIcon trayIcon;
	private static SystemTray tray;
	private static boolean startInTray = true;

	private static Image defaultIcon;
	
	private Rectangle windowPosition = new Rectangle(0, 0);  

	@Override
	public void init() throws Exception {
		Class<? extends AbstractJavaFxApplication> app = getClass();
		applicationContext = SpringApplication.run(app, savedArgs);
	}

	@Override
	public void start(Stage stage) throws Exception {
		AbstractJavaFxApplication.stage = stage;
		
		AbstractView view = showScene(savedInitialView);
		createTrayIcon(view.titleProperty().get());
		
		stage.setScene(scene);
		stage.setResizable(true);
		stage.centerOnScreen();
	 
		Image icon = getDefaultIcon();
		if(icon  !=null  ){
			stage.getIcons().add(icon);	
			DialogsUtil.defaultIcon(icon);
		}
	 
		if(trayIcon == null || !startInTray) {
			stage.show();
		}
	}
	
	protected InputStream getTrayIcon() {
		return null;
	}
	
	protected PopupMenu createTrayMenu(){
		return new PopupMenu();
	}
	
	
	protected void createTrayIcon(String tooltip) {
		InputStream icon = getTrayIcon();
		java.awt.Toolkit.getDefaultToolkit();

		if (java.awt.SystemTray.isSupported() && icon != null) {
			PopupMenu popup = createTrayMenu();

			Platform.setImplicitExit(false);
			tray = SystemTray.getSystemTray();

			java.awt.Image image = null;
			try {
				image = ImageIO.read(icon);
			} catch (IOException ex) {
				log.log(Level.SEVERE, "initTray load icon", ex);
			}
			
			
			stage.xProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					double x = newValue.doubleValue();
					if(x != -32000) {
						windowPosition.setX(x);
					}
				}
			});
			
			stage.yProperty().addListener(new ChangeListener<Number>() {
				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					double y = newValue.doubleValue();
					if(y != -32000) {
						windowPosition.setY(y);	
					}
				}
			});
			

			trayIcon = new TrayIcon(image, tooltip, popup);
			trayIcon.setImageAutoSize(true);
			trayIcon.addActionListener(event -> Platform.runLater(() -> {
				if (stage != null && !stage.isShowing()) {

					stage.setIconified(false);
					if(windowPosition.getWidth() != 0 ) {
						stage.setX(windowPosition.getX());
						stage.setY(windowPosition.getY());
						stage.setWidth(windowPosition.getWidth());
						stage.setHeight(windowPosition.getHeight());	
					}
				    stage.show();
					stage.toFront();
					
				} else {
					hideAndSavePosition();
				}
			}));

			try {
				tray.add(trayIcon);
			} catch (AWTException ex) {
				log.log(Level.SEVERE, "createTrayIcon", ex);
				ex.printStackTrace();
			}
			
			
			stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					if(newValue) {
						hideAndSavePosition();
					}
				}
			});
			
			stage.setOnCloseRequest( event -> {
				closeApplication();
			});

		}
	}
	
	private void hideAndSavePosition() {
		windowPosition.setWidth(stage.getWidth());
		windowPosition.setHeight(stage.getHeight());
		stage.hide();
	}
	
	public void closeApplication() {
		if(tray != null) {
			tray.remove(trayIcon);
		}
		Platform.exit();
	}
	
	protected static void launchApp(Class<? extends AbstractJavaFxApplication> appClass, Class<? extends AbstractView> view, String[] args) {
		System.setProperty("java.awt.headless", System.getProperty("java.awt.headless", Boolean.toString(false)));
		savedInitialView = view;
		savedArgs = args;
		Application.launch(appClass, args);
	}
	
	
	@SuppressWarnings("restriction")
	protected static void launchApp(Class<? extends AbstractJavaFxApplication> appClass, Class<? extends AbstractView> view, Class<? extends Preloader> preloader, String[] args) {
		System.setProperty("java.awt.headless", System.getProperty("java.awt.headless", Boolean.toString(false)));
		savedInitialView = view;
		savedArgs = args;
		LauncherImpl.launchApplication(appClass, preloader, args);
	}
	
	public static Image getDefaultIcon(){
		return AbstractJavaFxApplication.defaultIcon;
	}
	
	public static void setDefaultIcon(Image defaultIcon) {
		AbstractJavaFxApplication.defaultIcon = defaultIcon;
	}

	public AbstractView showScene(Class<? extends AbstractView> newView) {
		AbstractView view = applicationContext.getBean(newView);
		stage.titleProperty().bind(view.titleProperty());
		
		if (AbstractJavaFxApplication.scene == null) {
			AbstractJavaFxApplication.scene = new Scene(view.getView());
		}
		else {  
			AbstractJavaFxApplication.scene.setRoot(view.getView());
		}
		
		return view;
	}

	@Override
	public void stop() throws Exception {
		super.stop();
		applicationContext.close();
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
	
	public static void setStartInTray(boolean startInTray) {
		AbstractJavaFxApplication.startInTray = startInTray;
	}
	
	public static boolean isStartInTray() {
		return startInTray;
	}
}
