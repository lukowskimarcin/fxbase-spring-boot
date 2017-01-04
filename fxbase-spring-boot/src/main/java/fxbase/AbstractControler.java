package fxbase;

import static java.util.ResourceBundle.getBundle;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public  abstract  class AbstractControler  implements ApplicationContextAware  {
	
	private static String FXML_PATH = "/fxml/";

	protected ObjectProperty<AbstractControler> presenterProperty;
	protected StringProperty title = new SimpleStringProperty();
	protected FXMLLoader fxmlLoader;
	protected ResourceBundle bundle;

	protected URL resource;

	private ApplicationContext applicationContext;
	private String fxmlRoot;
	
	public AbstractControler() {
		this(FXML_PATH);
	}

	public AbstractControler(String path) {
		setFxmlRootPath(path);

		// TODO refactor me!
		FXMLView annotation = getFXMLAnnotation();
		if (annotation != null && !annotation.value().equals("")) {
			this.resource = getClass().getResource(annotation.value());
		} else {
			this.resource = getClass().getResource(getFxmlPath());
		}

		this.presenterProperty = new SimpleObjectProperty<AbstractControler>();
		this.bundle = getResourceBundle(getBundleName());
	}
	
	 
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext != null) {
			return;
		}
		this.applicationContext = applicationContext;
	}
	
	private FXMLView getFXMLAnnotation() {
		Class<? extends AbstractControler> theClass = this.getClass();
		FXMLView annotation = theClass.getAnnotation(FXMLView.class);
		return annotation;
	}
	
	private Object createControllerForType(Class<?> type) {
		return this.applicationContext.getBean(type);
	}

	private void setFxmlRootPath(String path) {
		if (path.endsWith("/")) {
			this.fxmlRoot = path;
		} else {
			this.fxmlRoot = path + "/";
		}
	}
	
	private FXMLLoader loadSynchronously(URL resource, ResourceBundle bundle) throws IllegalStateException {
		FXMLLoader loader = new FXMLLoader(resource, bundle);
		loader.setControllerFactory(this::createControllerForType);

		try {
			loader.load();
		} catch (IOException ex) {
			throw new IllegalStateException("Cannot load " + getConventionalName(), ex);
		}

		return loader;
	}

	private void ensureFxmlLoaderInitialized() {
		if (this.fxmlLoader != null) {
			return;
		}
		this.fxmlLoader = loadSynchronously(resource, bundle);
		this.presenterProperty.set((AbstractControler)this.fxmlLoader.getController());
	}
	
	/**
	 * Initializes the view by loading the FXML (if not happened yet) and
	 * returns the top Node (parent) specified in the FXML file.
	 *
	 * @return
	 */
	public Parent getView() {
		ensureFxmlLoaderInitialized();

		Parent parent = fxmlLoader.getRoot();
		addCSSIfAvailable(parent);
		return parent;
	}
	 
	/**
	 * Initializes the view synchronously and invokes the consumer with the
	 * created parent Node within the FX UI thread.
	 *
	 * @param consumer
	 *            - an object interested in received the {@link Parent} as
	 *            callback
	 */
	public void getView(Consumer<Parent> consumer) {
		CompletableFuture.supplyAsync(this::getView, Platform::runLater).thenAccept(consumer);
	}
	
	/**
	 * Scene Builder creates for each FXML document a root container. This
	 * method omits the root container (e.g. {@link AnchorPane}) and gives you
	 * the access to its first child.
	 *
	 * @return the first child of the {@link AnchorPane}
	 */
	public Node getViewWithoutRootContainer() {
		ObservableList<Node> children = getView().getChildrenUnmodifiable();
		if (children.isEmpty()) {
			return null;
		}
		return children.listIterator().next();
	}
	
	private void addCSSIfAvailable(Parent parent) {
		
		// TODO refactor me!
		FXMLView annotation = getFXMLAnnotation();
		if(annotation != null && annotation.css().length > 0) {
			for (String cssFile : annotation.css()) {
				URL uri = getClass().getResource(cssFile);
				String uriToCss = uri.toExternalForm();
				parent.getStylesheets().add(uriToCss);
			}
			return;
		}
		
		URL uri = getClass().getResource(getStyleSheetName());
		if (uri == null) {
			return;
		}

		String uriToCss = uri.toExternalForm();
		parent.getStylesheets().add(uriToCss);
	}

	public String getStyleSheetName() {
		return fxmlRoot + getConventionalName(".css");
	}
	
	/**
	 * @param ending
	 *            the suffix to append
	 * @return the conventional name with stripped ending
	 */
	protected String getConventionalName(String ending) {
		return getConventionalName() + ending;
	}

	/**
	 * @return the name of the view without the "View" prefix in lowerCase. For
	 *         AirhacksView just airhacks is going to be returned.
	 */
	protected String getConventionalName() {
		return stripEnding(getClass().getSimpleName().toLowerCase());
	}

	String getBundleName() {
	    // TODO refactor me!
        FXMLView annotation = getFXMLAnnotation();
        if (annotation != null && !annotation.bundle().equals("")) {
           return annotation.bundle();
        } else {
            return getClass().getPackage().getName() + "." + getConventionalName();
        }
	}

	static String stripEnding(String clazz) {

		if (!clazz.endsWith("view")) {
			return clazz;
		}

		return clazz.substring(0, clazz.lastIndexOf("view"));
	}

	/**
	 * @return the relative path to the fxml file derived from the FXML view.
	 *         e.g. The name for the AirhacksView is going to be
	 *         <PATH>/airhacks.fxml.
	 */

	final String getFxmlPath() {
		return fxmlRoot + getConventionalName(".fxml");
	}

	private ResourceBundle getResourceBundle(String name) {
		try {
			return getBundle(name);
		} catch (MissingResourceException ex) {
			return null;
		}
	}

	/**
	 * @return an existing resource bundle, or null
	 */
	public ResourceBundle getResourceBundle() {
		return this.bundle;
	}
	
	protected void setTitle(String title) {
	    this.title.setValue(title);
	}
	
	public StringProperty titleProperty() {
	    return title;
	}

	

}
