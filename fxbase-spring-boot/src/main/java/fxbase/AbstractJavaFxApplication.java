package fxbase;

import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sun.javafx.image.impl.ByteIndexed.Getter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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
	
	private static InputStream icon;
	 

	@Override
	public void init() throws Exception {
		Class<? extends AbstractJavaFxApplication> app = getClass();
		applicationContext = SpringApplication.run(app, savedArgs);
	}

	@Override
	public void start(Stage stage) throws Exception {
		AbstractJavaFxApplication.stage = stage;
		
		javax.swing.SwingUtilities.invokeLater(this::createTrayIcon);
		
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
	
	protected InputStream getTrayIcon() {
		return null;
	}
	
	protected PopupMenu createTrayMenu(){
		return new PopupMenu();
	}
	
	
	protected void createTrayIcon(){
		InputStream icon = getTrayIcon();
		
		java.awt.Toolkit.getDefaultToolkit();
		
		boolean sup  = java.awt.SystemTray.isSupported();
		
		if (java.awt.SystemTray.isSupported() && icon != null) {
			PopupMenu popup = createTrayMenu();
			
			 Platform.setImplicitExit(false);
			 SystemTray tray = SystemTray.getSystemTray();
			 
			 java.awt.Image image = null;
			 try {
				 
				 
				 
				 image = ImageIO.read(icon);
				// image = ImageIO.read(new URL("http://icons.iconarchive.com/icons/scafer31000/bubble-circle-3/16/GameCenter-icon.png"));
			} catch (IOException ex) {
				log.log(Level.SEVERE, "initTray load icon", ex);
			}
			 
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t) {
					hide(stage);
				}
			});
			
			final ActionListener closeListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    System.exit(0);
                }
            };
            
            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };
            

            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popup.add(showItem);

            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(closeListener);
            popup.add(closeItem);
            /// ... add other items
            // construct a TrayIcon
            trayIcon = new TrayIcon(image, "Title", popup);
            
            trayIcon.setImageAutoSize(true);
            
            // set the TrayIcon properties
            trayIcon.addActionListener(showListener);
            // ...
            // add the tray image
            try {
                tray.add(trayIcon);
            } catch (AWTException ex) {
            	log.log(Level.SEVERE, "createTrayIcon", ex);
                System.err.println(ex);
            }
			
		}  
	}
	
	private static void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                } else {
                    System.exit(0);
                }
            }
        });
    }
	
	protected static void launchApp(Class<? extends AbstractJavaFxApplication> appClass, Class<? extends AbstractView> view, String[] args) {
		System.setProperty("java.awt.headless", System.getProperty("java.awt.headless", Boolean.toString(false)));
		
		savedInitialView = view;
		savedArgs = args;
		Application.launch(appClass, args);
		
		
	}
	
	protected  List<Image> loadIcons() {
		return null;
	}
	
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
