package me.hazy.word;

import me.hazy.word.dao.WordTravller;
import me.hazy.word.model.ApplicationMode;
import me.hazy.word.model.Word;
import me.hazy.word.model.WordType;
import me.hazy.word.util.PreferenceUtil;
import me.hazy.word.widget.ExplanationAdapter;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class MainActivity extends Activity {

    private WordTravller wordTravller;

    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_bar);

        ((TextView) findViewById(R.id.left_text)).setText("Remember the Word");

        // Gesture detection
        gestureDetector = new GestureDetector(new SimpleGestureDetector());
        findViewById(R.id.explanation_list).setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });
        ((CheckBox) findViewById(R.id.star_check_box))
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        wordTravller.rememberTheWord(isChecked);
                        Log.i("TAG", "MainActivity onCheckedChanged: " + isChecked);
                    }
                });

        pref = new PreferenceUtil(this);
        ApplicationMode mode = pref.getApplicationMode();
        RemeberTheWordApplication application = (RemeberTheWordApplication) getApplication();
        wordTravller = application.getWordTraveller(mode);
        int lastLocation = pref.getLastLocation();
        updateView(wordTravller.getWordAtPosition(lastLocation));

        if (savedInstanceState == null) {
            int count = application.getRememberedCount();
            Toast.makeText(this, "you have remembered " + count + " words", Toast.LENGTH_SHORT).show();
        }
        Log.i("TAG", "MainActivity.onCreate");
    }

    private void updateView(Word word) {
        if (word == null) {
            word = new Word("Congratulations!~", "","", WordType.UNSEEN);
        }
        
        ((TextView) findViewById(R.id.right_text)).setText("" + (wordTravller.getPosition() + 1) + "/"
                + wordTravller.getCount());

        TextView wordNameTextView = (TextView) findViewById(R.id.word_name);
        wordNameTextView.setText(word.getName());

        TextView wordPartTextView = (TextView) findViewById(R.id.word_part);
        wordPartTextView.setText(word.getPart());

        CheckBox starCheckbox = (CheckBox) findViewById(R.id.star_check_box);
        starCheckbox.setChecked(word.isRemembered());

        TextView wordPhoneticTextView = (TextView) findViewById(R.id.word_phonetic);
        Typeface egoeuiFont = Typeface.createFromAsset(getAssets(), "font/segoeui.ttf");
        wordPhoneticTextView.setTypeface(egoeuiFont);
        wordPhoneticTextView.setText("/ " + word.getPhonetic() + " /");

        ListView explanationListView = (ListView) findViewById(R.id.explanation_list);
        explanationListView.setAdapter(new ExplanationAdapter(this, word.getExplanations()));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event))
            return true;
        else
            return false;
    }

    private void showPrevious() {
        Word previousWord = wordTravller.getPreviousWord();
        updateView(previousWord);
    }

    private void showNext() {
        Word nextWord = wordTravller.getNextWord();
        updateView(nextWord);
    }

    class SimpleGestureDetector extends SimpleOnGestureListener {

        private static final int SWIPE_MIN_DISTANCE = 80;

        private static final int SWIPE_MAX_OFF_PATH = 250;

        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                showNext();
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                showPrevious();
            }
            return false;
        }
    }

    public static final String WORD_INDEX = "wordIndex";

    private PreferenceUtil pref;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(WORD_INDEX, wordTravller.getPosition() + 1);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        int savedWordPosition = savedInstanceState.getInt(WORD_INDEX) - 1;
        if (savedWordPosition != -1) {
            Word currentWord = wordTravller.getWordAtPosition(savedWordPosition);
            updateView(currentWord);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        wordTravller.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_show_all:
            changeApplicationMode(ApplicationMode.ALL);
            return true;
        case R.id.menu_hide_star:
            changeApplicationMode(ApplicationMode.HIDE_STAR);
            return true;
        case R.id.menu_move_to_first:
            Word firstWord = wordTravller.getFirstWord();
            updateView(firstWord);
            return true;            
        }
        return false;
    }

    private void changeApplicationMode(ApplicationMode mode) {
        pref.setApplicationMode(mode);
        wordTravller.setMode(mode);
        updateView(wordTravller.getFirstWord());
    }

    @Override
    protected void onStop() {
        int lastIndex = wordTravller.getPosition();
        if (pref.getApplicationMode() == ApplicationMode.HIDE_STAR) {
            lastIndex = wordTravller.countNoneStarWord(wordTravller.getPosition());
        }
        pref.setLastLocation(lastIndex);
        super.onStop();
    }
    
    

}
