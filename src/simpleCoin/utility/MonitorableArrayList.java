package simpleCoin.utility;

import java.util.ArrayList;

import javafx.beans.property.SimpleIntegerProperty;

/**
 * Create a wrapper-class for ArrayList that provides properties that can be
 * monitored. This is a synchronized implementation.
 * 
 * Note: We cannot extend ArrayList directly, because that would make all of its
 * methods directly visible. We need to be able to intercept methods, and change
 * the properties that we make available.
 */
public class MonitorableArrayList<T> {
	private ArrayList<T> delegateList = new ArrayList<>();
	private final SimpleIntegerProperty sizeProperty = new SimpleIntegerProperty();

	public MonitorableArrayList() {
		sizeProperty.set(delegateList.size());
	}

	public SimpleIntegerProperty getSizeProperty() {
		return sizeProperty;
	}
	
	public int size() {
		return sizeProperty.get();
	}
	
	public synchronized boolean add(T item) {
		boolean result = delegateList.add(item);
		sizeProperty.set(delegateList.size());
		return result;
	}
	
	public T get(int index) {
		return delegateList.get(index);
	}
	
	public synchronized boolean remove(T item) {
		boolean result = delegateList.remove(item);
		sizeProperty.set(delegateList.size());
		return result;
	}
}
