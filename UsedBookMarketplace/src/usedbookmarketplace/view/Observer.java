package usedbookmarketplace.view;

import java.util.Vector;

public interface Observer {
	public <T> void update(Vector<T> data);
//	public <T> void update_general(Vector<T> data, Object o);
}
