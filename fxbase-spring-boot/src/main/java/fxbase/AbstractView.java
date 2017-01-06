package fxbase;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import static java.util.ResourceBundle.getBundle;

public abstract class AbstractView implements ApplicationContextAware {
	private static final String FXML_PATH = "/fxml/";
	
	protected Parent parentView;
	protected AbstractView controler;
	
	protected URL fxmlFile;
	protected FXMLLoader fxmlLoader;
	protected ResourceBundle bundle;
	private ApplicationContext applicationContext;
	private String fxmlRoot;
	
	public AbstractView() {
		this(FXML_PATH);
	}
	
	public AbstractView(String fxmlRoot) {
		FXMLView annotation = getFXMLAnnotation();
		if(annotation!= null && !annotation.value().equals("")){
			fxmlFile = getClass().getResource(annotation.value());
		} else {
			String fileName = fxmlRoot + "/" + getConventionalName() + ".fxml";
			fxmlFile = getClass().getResource(fileName);
		}
		this.fxmlRoot = fxmlRoot;
		this.bundle = getResourceBundle(getBundleName());
		this.fxmlLoader = loadFXML(fxmlFile, bundle);
		this.controler = fxmlLoader.getController();
		this.parentView = fxmlLoader.getRoot();
		addCSSIfAvailable(parentView);
	}
	
	public void reload(){
		this.fxmlLoader = loadFXML(fxmlFile, bundle);
		this.controler = fxmlLoader.getController();
	}
	
	private FXMLLoader loadFXML(URL resource, ResourceBundle bundle) {
		FXMLLoader loader = null;
		try {
			loader = new FXMLLoader(resource, bundle);
			loader.setControllerFactory(this::createControllerForType);
			loader.load();
		} catch (Exception ex) {
			throw new IllegalStateException("Cannot load " + resource.getPath(), ex);
		}
		return loader;
	}
	
	private Object createControllerForType(Class<?> type) {
		return applicationContext.getBean(type);
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
		return parentView;
	}
	
	public Node getViewWithoutRootContainer(){
		ObservableList<Node> children = parentView.getChildrenUnmodifiable();
		if (children.isEmpty()) {
			return null;
		}
		return children.listIterator().next();
	}
	
	public AbstractView getControler() {
		return controler;
	}
	
}
