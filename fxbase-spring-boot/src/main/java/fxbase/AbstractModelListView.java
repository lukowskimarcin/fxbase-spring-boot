package fxbase;

import java.util.ArrayList;
import java.util.List;

public class AbstractModelListView<T> extends AbstractView {
	
	private List<T> dataList = new ArrayList<>();
	
	
	public  AbstractModelListView(List<T> list) {
		dataList = list;
	}
	
	
	public List<T> getData() {
		return dataList;
	}

	public void setData(List<T> list) {
		this.dataList = list;
	}
	

}
