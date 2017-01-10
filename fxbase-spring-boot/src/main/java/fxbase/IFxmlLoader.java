package fxbase;

public interface IFxmlLoader {

	 <T> T loadView(Class<? extends AbstractView> newView);
	 
}
