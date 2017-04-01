package example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import com.sun.javafx.application.LauncherImpl;

import fxbase.AbstractJavaFxApplication;
import fxbase.AbstractPreloaderView;
import javafx.scene.image.Image;

import javafx.application.Preloader;

@Lazy
@ComponentScan(basePackages= {"example", "service"})
@SpringApplicationConfiguration
public class Starter extends AbstractJavaFxApplication {
	
	public static void main(String[] args) {
		addDefaultCSS("/css/main.css");
		loadIcon();
		setStartInTray(true);
		
		//launchApp(Starter.class, MainControler.class, args);
		launchApp(Starter.class, MainControler.class, AppPreloader.class, args);
	}
	
	@SuppressWarnings("restriction")
	@Override
    public void init() throws Exception {
		super.init();
		
		double COUNT_LIMIT = 10000;
        // Perform some heavy lifting (i.e. database start, check for application updates, etc. )
        for (int i = 0; i < COUNT_LIMIT; i++) {
            double progress =  i / COUNT_LIMIT;
            LauncherImpl.notifyPreloader(this, new Preloader.ProgressNotification(progress));
        }
    }
	
	private static void loadIcon() {
		try {
			InputStream img = Starter.class.getClassLoader().getResourceAsStream("images/tray.png");
			Image icon = new Image(img);
			setDefaultIcon(icon);
		} catch (Exception ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
	}
	
	
	@Override
	protected InputStream getTrayIcon() {
		InputStream is = null;
		try {
			is = new FileInputStream("src/main/resources/images/tray.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return is;
	}
	
	
	
}
