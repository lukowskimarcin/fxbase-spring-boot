package fxbase;

import fxbase.enums.ModelBeanMode;

public class AbstractModelView<T> extends AbstractView {
	private T data;
	
	private ModelBeanMode mode = ModelBeanMode.VIEW;
	
	public  void populate(T data){
		this.data = data;
	}
	
	
	protected boolean performAplay() {
		return true;
	}
	
	protected void performEdit() {
		mode = ModelBeanMode.EDIT;
	}
	
	
	public boolean applay(){
		boolean result = performAplay();
		if(result) {
			mode = ModelBeanMode.VIEW;
		}
		return result;
	}
	
	public void cancel(){
		
	}
	
	
	public T getData() {
		return data;
	}
	
	public T getEditableData() {
		return data;
	}
	
	public ModelBeanMode getMode() {
		return mode;
	}
}
