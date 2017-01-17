package example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;


import fxbase.AbstractJavaFxApplication;
import javafx.scene.image.Image;

@Lazy
@ComponentScan(basePackages= {"example", "service"})
@SpringApplicationConfiguration
public class Starter extends AbstractJavaFxApplication {
	
	public static void main(String[] args) {
		addDefaultCSS("/css/main.css");
		launchApp(Starter.class, MainControler.class, args);
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

	@Override
	protected List<Image> loadIcons() {
		Image image = new Image(getTrayIcon());
		List<Image> list = new ArrayList<Image>();
		list.add(image);
		return list;
	}
	 
	
}
