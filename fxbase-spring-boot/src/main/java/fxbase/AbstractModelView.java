package fxbase;

import fxbase.enums.ModelBeanMode;

public abstract class AbstractModelView<T extends Dto> extends AbstractView {
	private T data;
	private T dataCopy;
	private ModelBeanMode mode = ModelBeanMode.VIEW;
	
	public  void populate(T data){
		this.data = data;
	}
	
	/**
	 * Connect model properties with view fields
	 */
	public abstract void bindToView(T model); 
	
	/**
	 * Unbind model from view
	 */
	public abstract void unBindToView(); 
	
	@SuppressWarnings("unchecked")
	protected T createDataCopy() throws CloneNotSupportedException{
		return (T)clone();
	}
	
	public void prepareToAdd() {
		mode = ModelBeanMode.ADD;
	}
	
	public void prepareToEdit() {
		mode = ModelBeanMode.EDIT;
	}	
	
	protected T performInsert(T data) throws Exception {
		return data;
	}
	
	protected T performUpdate(T data) throws Exception {
		return data;
	}
	
	protected boolean performRemove(T data) throws Exception {
		return true;
	}
	
	public boolean applay() throws Exception {
		boolean resultStatus = false;
		T modifiedData = getEditableData();
		T resultData = null;
		
		if(isAddMode()){
			resultData = performInsert(modifiedData);
		} else if(isEditMode()) {
			resultData = performUpdate(modifiedData);
		} else {
			assert(false);
		}
		
		if(resultData != null) {
			data = resultData;
			dataCopy = null;
			mode = ModelBeanMode.VIEW;
			resultStatus = true;
		}
		
		return resultStatus;
	}
	
	public void cancel(){
		mode = ModelBeanMode.VIEW;
		data = dataCopy;
		dataCopy = null;
	}
	
	public boolean remove() throws Exception {
		return performRemove(data);
	}
	
	public T getData() {
		return data;
	}
	
	public T getEditableData() throws Exception {
		if(dataCopy==null) {
			dataCopy = createDataCopy();
		}
		return dataCopy;
	}
	
	public void cleanEditableData() {
		dataCopy = null;
	}
	
	public boolean isAddMode(){
		return ModelBeanMode.ADD.equals(mode);
	}
	
	public boolean isEditMode()	{
		return ModelBeanMode.EDIT.equals(mode);
	}
	
	public boolean isViewMode()	{
		return ModelBeanMode.VIEW.equals(mode);
	}
}
