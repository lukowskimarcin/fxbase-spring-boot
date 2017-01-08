package fxbase;

public interface IFxmlLoader {

	 <T> T loadView(Class<? extends AbstractView> newView);
	 
	 <T> T loadView(Class<? extends AbstractView> newView, boolean reload);
	 
	 <T> T showView(Class<? extends AbstractView> newView);
	
}
