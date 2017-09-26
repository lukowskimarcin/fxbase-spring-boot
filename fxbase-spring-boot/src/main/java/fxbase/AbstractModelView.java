package fxbase;

import fxbase.enums.ViewMode;

public abstract class AbstractModelView<T extends Dto> extends AbstractView {
	private T data;
	private T dataCopy;
	private ViewMode mode = ViewMode.VIEW;

	
	/**
	 * Connect model properties with view fields
	 * Connect will be performed for data, not dataCopy
	 */
	public abstract void bindToView(); 
	
	/**
	 * Unbind model from view
	 */
	public abstract void unBindToView();
	
	
	@SuppressWarnings("unchecked")
	protected T createDataCopy() throws CloneNotSupportedException{
		return (T)data.clone();
	}
	
	public void prepareToAdd() {
		mode = ViewMode.ADD;
		dataCopy = null;	//nie ma do czego wracac przy anulowaniu
	}
	
	public void prepareToEdit() throws Exception {
		mode = ViewMode.EDIT;
		dataCopy = getDataCopy();
	}	
	
	
	/**
	 * Operacje zwiazane z zapisem nowego rekordu na bazie danych
	 * @param data
	 * @return
	 * @throws Exception
	 */
	protected boolean performInsert(T data) throws Exception {
		return true;
	}
	
	
	/**
	 * Operacje zwiazane z zapisem nowej wersji rekordu na bazie danych
	 * @param data
	 * @return
	 * @throws Exception
	 */
	protected boolean performUpdate(T data) throws Exception {
		return true;
	}
	
	
	/** Operacje zwiazane z usuwaniem danego rekordu na bazie danych
	 * @param data
	 * @return
	 * @throws Exception
	 */
	protected boolean performRemove(T data) throws Exception {
		return true;
	}
	
	public boolean save() throws Exception {
		boolean resultStatus = false;
		
		if(isAddMode()){
			resultStatus = performInsert(data);
		} else if(isEditMode()) {
			resultStatus = performUpdate(data);
		} else {
			assert(false);
		}
		
		if(resultStatus) {
			dataCopy = null;
			mode = ViewMode.VIEW;
		}
		
		return resultStatus;
	}
	
	public void cancel(){
		mode = ViewMode.VIEW;
		unBindToView();
		data = dataCopy;
		bindToView();
		dataCopy = null;
	}
	
	public boolean remove() throws Exception {
		return performRemove(data);
	}
	
	public T getData() {
		return data;
	}
	
	public void setData(T data) {
		this.data = data;
	}
	
	public T getDataCopy() throws Exception {
		if(dataCopy==null) {
			dataCopy = createDataCopy();
		}
		return dataCopy;
	}
	
	public boolean isAddMode(){
		return ViewMode.ADD.equals(mode);
	}
	
	public boolean isEditMode()	{
		return ViewMode.EDIT.equals(mode);
	}
	
	public boolean isViewMode()	{
		return ViewMode.VIEW.equals(mode);
	}
}
