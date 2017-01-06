package fxbase;

public interface IFxmlLoader {

	 <T> T loadView(Class<? extends AbstractControler> newView);
	 
	 <T> T loadView(Class<? extends AbstractControler> newView, boolean reload);
	 
	 <T> T showView(Class<? extends AbstractControler> newView);
	
}
