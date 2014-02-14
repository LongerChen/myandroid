package myandroid.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 
 * @author Tony
 * 
 * @param <D>
 *            data
 * @param <V>
 *            view
 */
public abstract class ListAdapter<D, V> extends BaseAdapter {
	protected List<D> list = new ArrayList<D>();
	protected boolean cycle = false;
	protected Context context;
	protected Resources res;

	public ListAdapter(Context context) {
		this.context = context;
		res = context.getResources();
	}

	@Override
	public int getCount() {
		return list.size() != 0 && cycle ? Integer.MAX_VALUE : list.size();
	}

	@Override
	public D getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		V view = createRowView(position, (V) v);
		fillRowView(position, view,
				list.get(cycle ? position % list.size() : position));
		return (View) view;
	}

	/**
	 * 
	 * @param index
	 * @param v
	 * @return
	 */
	protected abstract V createRowView(int index, V v);

	/**
	 * 
	 * @param index
	 * @param v
	 * @param data
	 */
	protected abstract void fillRowView(int index, V view, D data);

	/**
	 * 
	 * @param cycle
	 */
	public void setCycle(boolean cycle) {
		this.cycle = cycle;
	}

	/**
	 * 
	 * @param list
	 */
	public void setList(List<D> list) {
		this.list = list;
		notifyDataSetChanged();
	}

	/**
	 * 
	 * @return
	 */
	public List<D> getList() {
		return list;
	}

	/**
	 * 
	 * @param addList
	 */
	public void addList(List<D> addList) {
		list.addAll(addList);
		notifyDataSetChanged();
	}

	/**
	 * 
	 * @param d
	 */
	public void add(D d) {
		list.add(d);
		notifyDataSetChanged();
	}
}
