package myandroid.database;

import myandroid.tools.Develop;
import android.database.DataSetObservable;

public class DataSetObservableC extends DataSetObservable {

	public void notifyChanged(int position) {
		Develop.e(this, mObservers.size());
		synchronized (mObservers) {
			mObservers.get(0).onChanged();
		}
	}
}
