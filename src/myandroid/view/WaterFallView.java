package myandroid.view;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import myandroid.adapter.ListAdapter;
import myandroid.adapter.ListAdapter.OnDataSetChangeListener;
import myandroid.tools.Develop;

public class WaterFallView<D> extends ListView implements
		OnDataSetChangeListener<D> {
	protected OnFallingListener listener;
	protected int startPage = 0;
	protected int loadOffset = 3;
	protected boolean fall = false;
	boolean falling = false;

	public WaterFallView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		onFalling();
	}

	public void stopFall() {
		fall = false;
	}

	public void startFall() {
		fall = true;
	}

	public void setFall(boolean fall) {
		this.fall = fall;
	}

	public int getStartPage() {
		return startPage;
	}

	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}

	public int getLoadOffset() {
		return loadOffset;
	}

	public void setLoadOffset(int loadOffset) {
		this.loadOffset = loadOffset;
	}

	public OnFallingListener getListener() {
		return listener;
	}

	public void setListener(OnFallingListener listener) {
		this.listener = listener;
	}

	void onFalling() {
		if (!fall)
			return;
		if (getAdapter() == null)
			return;
		if (falling)
			return;
		if ((getAdapter().getCount() - getLastVisiblePosition()) <= loadOffset)
			if (listener != null) {
				listener.onFailing(startPage++);
				falling = true;
			}
	}

	public void setAdapter(ListAdapter<D, ? extends View> adapter) {
		super.setAdapter(adapter);
		adapter.setOnDataSetChangeListener(this);
	}

	@Override
	@SuppressWarnings("unchecked")
	public ListAdapter<D, ? extends View> getAdapter() {
		// TODO Auto-generated method stub
		return (ListAdapter<D, ? extends View>) super.getAdapter();
	}

	public void setOnFallingListener(OnFallingListener l) {
		this.listener = l;
	}

	public interface OnFallingListener {
		public void onFailing(int page);
	}

	@Override
	public void onChange(List<D> data) {
		falling = false;
	}
}
