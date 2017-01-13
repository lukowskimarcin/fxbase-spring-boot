package example;

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
	protected List<Image> loadIcons() {
		return null;
	}

	
	 
	
}
