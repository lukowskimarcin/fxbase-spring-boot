package utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class DialogsUtil {
	
	private static String defaultErrorTitle = "Błąd";
	private static String defaultWarningTitle = "Uwaga";
	private static String defaultExceptionDetails = "Szczegóły wyjątku:";	
	private static boolean consoleStackTrace = true;
	private static Image defaultIcon = null;
		
	private Alert alert = null;
	
	
	private DialogsUtil() {
		alert = new Alert(AlertType.NONE);
		alert.setContentText(null);
		alert.setTitle(null);
		
		if(defaultIcon!=null) {
			Stage stage = (Stage)alert.getDialogPane().getScene().getWindow();
			stage.getIcons().add(defaultIcon);
		}
	}
	
	public static DialogsUtil create() {
		DialogsUtil instance = new DialogsUtil();
		return instance.headerText(null);
	}
	
	public DialogsUtil owner(Stage stage) {
		alert.initModality(Modality.WINDOW_MODAL);
		alert.initOwner(stage.getOwner());
		return this;
	}
	
	public  DialogsUtil headerText(String headerText) {
		alert.setHeaderText(headerText);
		return this;
	}
	
	public DialogsUtil title(String title) {
		alert.setTitle(title);
		return this;
	}
	
	public DialogsUtil message(String message) {
		alert.setContentText(message);
		return this;
	}
	
	public void showInformation() {
		alert.setAlertType(AlertType.INFORMATION);
		alert.showAndWait();
	}
	
	public void showWarning() {
		alert.setAlertType(AlertType.WARNING);
		if(alert.getTitle()==null) {
			alert.setTitle(defaultWarningTitle);
		}
		alert.showAndWait();
	}
	
	public void showError() {
		alert.setAlertType(AlertType.ERROR);
		if(alert.getTitle()==null) {
			alert.setTitle(defaultErrorTitle);
		}
		alert.showAndWait();
	}
	
	public void showException(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();
		
		if(consoleStackTrace) {
			ex.printStackTrace();
		}

		Label label = new Label(defaultExceptionDetails);

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		
		if(alert.getTitle()==null) {
			alert.setTitle(defaultErrorTitle);
		}
		
		if(alert.getContentText()==null) {
			alert.setContentText(ex.getMessage());
		}
		
		alert.getDialogPane().setExpandableContent(expContent);
		alert.setAlertType(AlertType.ERROR);
		alert.showAndWait();
	}
	
	public static void defaultIcon(Image icon) {
		defaultIcon = icon;
	}
	
	public static void defaultErrorTitle(String title) {
		defaultErrorTitle = title;
	}
	
	public static void defaultWarningTitle(String title) {
		defaultWarningTitle = title;
	}
	
	public static void defaultExceptionDetails(String details) {
		defaultWarningTitle = details;
	}
	
}
