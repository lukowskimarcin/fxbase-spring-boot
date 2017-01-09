package example;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import fxbase.AbstractJavaFxApplication;

@Lazy
@ComponentScan
@SpringApplicationConfiguration
public class Starter extends AbstractJavaFxApplication {
	
	public static void main(String[] args) {
		launchApp(Starter.class, MainControler.class, args);
	}
	
	 
	
}
//C:/Users/Marcin/git/fxbase-spring-boot/fxbase-spring-boot/target/classes/fxml/Main.fxml