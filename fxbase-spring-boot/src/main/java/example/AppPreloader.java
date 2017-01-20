package example;

import fxbase.AbstractPreloaderView;
import javafx.scene.image.Image;

public class AppPreloader extends AbstractPreloaderView {

	@Override
	public Image getBackground() {
		return new Image("http://fxexperience.com/wp-content/uploads/2010/06/logo.png");
	}
	
	@Override
	public boolean isContinousProgress() {
		return true;
	}

}
