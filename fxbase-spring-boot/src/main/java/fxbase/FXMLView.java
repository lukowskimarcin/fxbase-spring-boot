package fxbase;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.stereotype.Component;

@Component
@Retention(RetentionPolicy.RUNTIME)
public @interface FXMLView {
	String fxml() default "";
	String[] css() default {}; 
	String bundle() default "";
}
