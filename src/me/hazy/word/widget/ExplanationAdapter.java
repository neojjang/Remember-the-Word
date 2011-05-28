package me.hazy.word.widget;

import java.util.List;

import me.hazy.word.R;
import me.hazy.word.model.Example;
import me.hazy.word.model.Explanation;
import me.hazy.word.model.RelatedWord;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExplanationAdapter extends AbstractModelAdapter<Explanation> {

    public ExplanationAdapter(Context context, List<Explanation> Tweets) {
        super(context, Tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout rowLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(
                R.layout.list_item_explanation, parent, false);
        Explanation explanation = getItem(position);

        TextView positionTextView = (TextView) rowLayout.findViewById(R.id.position);
        positionTextView.setText("" + (position + 1));

        String meaning = explanation.getMeaning();
        TextView meaningTextView = (TextView) rowLayout.findViewById(R.id.meaning);
        meaningTextView.setText(meaning);

        if (!explanation.getSynonyms().isEmpty()) {
            String synonyms = getRelatedWordText(explanation.getSynonyms());
            TextView synonymsTextView = (TextView) rowLayout.findViewById(R.id.synonyms);
            synonymsTextView.setText(synonyms);
        } else {
            View synonymsLayout = rowLayout.findViewById(R.id.synonymsLayout);
            synonymsLayout.setVisibility(View.GONE);
        }
        if (!explanation.getOpposites().isEmpty()) {
            String opposite = getRelatedWordText(explanation.getOpposites());
            TextView oppositesTextView = (TextView) rowLayout.findViewById(R.id.opposites);
            oppositesTextView.setText(opposite);
        } else {
            View oppositesLayout = rowLayout.findViewById(R.id.oppositesLayout);
            oppositesLayout.setVisibility(View.GONE);
        }

        String examples = getExamplesText(explanation);
        TextView examplesTextView = (TextView) rowLayout.findViewById(R.id.examples);
        examplesTextView.setText(examples);

        return rowLayout;
    }

    private String getExamplesText(Explanation explanation) {
        StringBuilder builder = new StringBuilder();
        List<Example> examples = explanation.getExamples();
        for (Example example : examples) {
            builder.append("• ");
            builder.append(example.getSentense());
            builder.append("\n");
        }
        return builder.toString();
    }

    private <T extends RelatedWord> String getRelatedWordText(List<T> relatedWords) {
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (RelatedWord word : relatedWords) {
            if (!isFirst) {
                builder.append(", ");
            }
            isFirst = false;
            builder.append(word.getName());
        }
        return builder.toString();
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
