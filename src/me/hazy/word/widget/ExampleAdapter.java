package me.hazy.word.widget;

import java.util.List;

import me.hazy.word.R;
import me.hazy.word.model.Example;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExampleAdapter extends AbstractModelAdapter<Example> {

    public ExampleAdapter(Context context, List<Example> Tweets) {
        super(context, Tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout rowLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.list_item_example, parent, false);
        Example example = getItem(position);
        TextView exampleTextView = (TextView) rowLayout.findViewById(R.id.example);
        exampleTextView .setText(example.getSentense());
        return rowLayout;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
