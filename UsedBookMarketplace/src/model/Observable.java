package model;

import java.util.Vector;

import view.Observer;

public interface Observable {
	public void addObserver(Observer observer);
	public void deleteObserver(Observer observer);
	public <T> void notifyObserver(Vector<T> data);
}
