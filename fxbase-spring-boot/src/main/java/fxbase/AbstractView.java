package fxbase;

import static java.util.ResourceBundle.getBundle;

import java.net.URL;
import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import fxbase.enums.ControlerCreateMode;
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
	protected StringProperty title = new SimpleStringProperty();
	protected URL fxmlFilePath;
	protected FXMLLoader fxmlLoader;
	protected ResourceBundle bundle;
	
	private ApplicationContext applicationContext;
	private String fxmlRoot;
	private Object controler;
	
	protected ControlerCreateMode mode = ControlerCreateMode.SELF;
	
	public AbstractView() {
		this(FXML_PATH);
	}
	
	public AbstractView(String fxmlRoot) {
		this.fxmlRoot = fxmlRoot;
		
		FXMLView annotation = getFXMLAnnotation();
		if(annotation!= null && !annotation.fxml().equals("")){
			fxmlFilePath = getClass().getResource(annotation.fxml());
		} else {
			String fileName = fxmlRoot + "/" + getConventionalName() + ".fxml";
			fxmlFilePath = getClass().getResource(fileName);
		}
		this.bundle = getResourceBundle(getBundleName());
	
	}
	
	
	public void reload(){
		fxmlLoader = null;
		controler = null;
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
		if(mode.equals(ControlerCreateMode.SELF)) {
			return this;
		} else {
			return applicationContext.getBean(type);
		}
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
			ResourceBundle bundle = getBundle(name, AbstractJavaFxApplication.getLocale());
			return bundle;
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
			
		} else {
			URL uri = getClass().getResource(getStyleSheetName());
			if(uri!=null){
				String uriToCss = uri.toExternalForm();
				parent.getStylesheets().add(uriToCss);	
			}
		}
		
		addGlobalCSSIfAvailable(parent);
	}
	
	private void addGlobalCSSIfAvailable(Parent parent){
		List<String> cssList = AbstractJavaFxApplication.getDefaultCSS();
		if(cssList!=null){
			for (String cssFile : cssList) {
				URL uri = getClass().getResource(cssFile);
				String uriToCss = uri.toExternalForm();
				parent.getStylesheets().add(uriToCss);
			}	
		}
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
        String path = null;
        if (annotation != null && !annotation.bundle().equals("")) {
        	path = annotation.bundle();
        	path = path.replace("/", ".");
        	path = path.replace("\\", ".");
        	if(path.startsWith(".")) {
        		path = path.substring(1);
        	}
        } else {
        	path = this.getClass().getPackage().getName() + "." + getConventionalName();
        }
        return path;
	}
	
	public Parent getView() {
		initializeFXMLLoader();
		return parentView;
	}
	
	@Override	
	public <T> T loadView(Class<? extends AbstractView> newView) {
		return loadView(newView, ControlerCreateMode.SELF);
	}
	
	@Override	
	@SuppressWarnings("unchecked")
	public <T> T loadView(Class<? extends AbstractView> newView, ControlerCreateMode mode) {
		AbstractView view = applicationContext.getBean(newView);
		view.mode = mode;
		return (T)view;
	}
	 
	private void initializeFXMLLoader() {
		if (fxmlLoader == null) {
			fxmlLoader = loadFXML(fxmlFilePath, bundle);
			parentView = fxmlLoader.getRoot();
			controler = fxmlLoader.getController();
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

	public Object getControler() {
		return controler;
	}	
}
