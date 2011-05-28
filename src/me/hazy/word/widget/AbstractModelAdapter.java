package me.hazy.word.widget;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class AbstractModelAdapter<T> extends BaseAdapter {

	private List<T> elements;

	private Context context;

	public Context getContext() {
		return context;
	}

	public AbstractModelAdapter(Context context, List<T> elements) {
		this.context = context;
		this.elements = elements;
	}

	@Override
	public int getCount() {
		return elements.size();
	}

	@Override
	public T getItem(int position) {
		return elements.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

}
