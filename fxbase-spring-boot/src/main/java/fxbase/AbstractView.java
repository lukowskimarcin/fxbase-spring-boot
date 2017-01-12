package fxbase;

import static java.util.ResourceBundle.getBundle;

import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class AbstractView implements ApplicationContextAware, IFxmlLoader {
	private static final String FXML_PATH = "/fxml/";
	protected static final Logger log = Logger.getLogger(AbstractView.class.getName());   
	
	protected Parent parentView;
	//protected Object controler;
	
	protected StringProperty title = new SimpleStringProperty();
	protected URL fxmlFilePath;
	protected FXMLLoader fxmlLoader;
	protected ResourceBundle bundle;
	private ApplicationContext applicationContext;
	private String fxmlRoot;
	
	public AbstractView() {
		this(FXML_PATH);
	}
	
	public AbstractView(String fxmlRoot) {
		this.fxmlRoot = fxmlRoot;
		
		FXMLView annotation = getFXMLAnnotation();
		if(annotation!= null && !annotation.value().equals("")){
			fxmlFilePath = getClass().getResource(annotation.value());
		} else {
			String fileName = fxmlRoot + "/" + getConventionalName() + ".fxml";
			fxmlFilePath = getClass().getResource(fileName);
		}
		this.bundle = getResourceBundle(getBundleName());
	
	}
	
	
	public void reload(){
		fxmlLoader = null;
		//controler = null;
	}
	
	
	private FXMLLoader loadFXML(URL resource, ResourceBundle bundle) {
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(resource, bundle);
			//loader.setController(this);
			loader.setControllerFactory(this::createControllerForType);
			loader.load();
			
		} catch (Exception ex) {
			throw new IllegalStateException("Cannot load " + resource.getPath(), ex);
		}
		return loader;
	}
	
	private Object createControllerForType(Class<?> type) {
		return this;
		//return applicationContext.getBean(type);
	}
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (this.applicationContext == null) {
			this.applicationContext = applicationContext;
		}
	}
	
	
	private String getConventionalName() {
		String name = getClass().getSimpleName().toLowerCase();
		return name;
	}
	
	private ResourceBundle getResourceBundle(String name) {
		try {
			return getBundle(name);
		} catch (MissingResourceException ex) {
			return null;
		}
	}
	
	private void addCSSIfAvailable(Parent parent) {
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
		return fxmlRoot + getConventionalName() + ".css";
	}
	
	private FXMLView getFXMLAnnotation() {
		Class<? extends AbstractView> theClass = this.getClass();
		FXMLView annotation = theClass.getAnnotation(FXMLView.class);
		return annotation;
	}
	
	public String getBundleName() {
        FXMLView annotation = getFXMLAnnotation();
        if (annotation != null && !annotation.bundle().equals("")) {
           return annotation.bundle();
        } else {
            return getClass().getPackage().getName() + "." + getConventionalName();
        }
	}
	
	public Parent getView() {
		initializeFXMLLoader();
		return parentView;
	}
	
	@Override	
	@SuppressWarnings("unchecked")
	public <T> T loadView(Class<? extends AbstractView> newView) {
		AbstractView view = applicationContext.getBean(newView);
		return (T)view;
	}
	 
	private void initializeFXMLLoader() {
		if (fxmlLoader == null) {
			fxmlLoader = loadFXML(fxmlFilePath, bundle);
			//controler = fxmlLoader.getController();
			parentView = fxmlLoader.getRoot();
			
			
			addCSSIfAvailable(parentView);
		}
	}
	
	
	
	public Node getViewWithoutRootContainer(){
		ObservableList<Node> children = getView().getChildrenUnmodifiable();
		if (children.isEmpty()) {
			return null;
		}
		return children.listIterator().next();
	}
	
	
	
	public void setTitle(String title) {
	    this.title.setValue(title);
	}
	
	public StringProperty titleProperty() {
	    return title;
	}
	
}
