package example;

import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Lazy;

import fxbase.AbstractJavaFxApplication;

@Lazy
@ComponentScan(basePackages= {"example", "service"})
@SpringApplicationConfiguration
public class Starter extends AbstractJavaFxApplication {
	
	public static void main(String[] args) {
		launchApp(Starter.class, MainControler.class, args);
	}

	@Override
	protected void initialize() {
		
	}
	
	 
	
}
