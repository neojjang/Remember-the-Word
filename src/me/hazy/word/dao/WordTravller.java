package me.hazy.word.dao;

import me.hazy.word.model.ApplicationMode;
import me.hazy.word.model.Word;
import me.hazy.word.model.WordType;
import android.database.Cursor;

/**
 * 
 * <p>
 * A layer off the Cursor, and make life easy by using WordDAO
 * 
 * didn't use WordDAO, beacuse I don't want it to have the state for a specific
 * query
 * </p>
 * 
 * @author Flynn
 * 
 * @version 1.0
 */
public class WordTravller {

    private WordDAO wordDAO;

    private Cursor cursor;
    
    private Word[] wordArray;

    private ApplicationMode mode;

    public WordTravller(WordDAO wordDAO, ApplicationMode mode) {
        this.wordDAO = wordDAO;
        setMode(mode);
    }
    
    private void changeCursor(ApplicationMode mode) {
        if (cursor != null)
            cursor.close();
        this.cursor = (ApplicationMode.ALL == mode) ? 
                wordDAO.getAllWords() : wordDAO.getUnrememberedWords();
        this.wordArray = new Word[cursor.getCount()];
    }

    private Word get() {
        int position = cursor.getPosition();
        if (position < 0)
            return null;
        if (wordArray[position] != null)
            return wordArray[position];
        Word entiry = wordDAO.getEntiry(cursor);
        wordArray[position] = entiry;
        return entiry;
    }
    
    public int getPosition() {
        return cursor.getPosition();
    }

    public Word getNextWord() {
        if (!cursor.moveToNext())
            cursor.moveToFirst();
        return get();
    }

    public Word getPreviousWord() {
        if (!cursor.moveToPrevious())
            cursor.moveToLast();
        return get();
    }
    
    public int getCount() {
        return cursor.getCount();
    }

    public Word getFirstWord() {
        cursor.moveToFirst();
        return get();
    }
    
    public Word getWordAtPosition(int position) {
        if (!cursor.moveToPosition(position))
            cursor.moveToFirst();
        return get();
    }
    
    public void close() {
        cursor.close();
    }

    public void rememberTheWord(boolean isChecked) {
        WordType changedType = isChecked ? WordType.REMEMBERED : WordType.UNSEEN;
        Word word = wordDAO.getEntiry(cursor);
        wordDAO.update(word, changedType);
        wordArray[cursor.getPosition()].setType(changedType);
    }



    public void setMode(ApplicationMode mode) {
        this.mode = mode;
        changeCursor(mode);
    }

    public ApplicationMode getMode() {
        return mode;
    }

    public int countNoneStarWord(int position) {
        int cnt = 0;
        for (int i = 0; i < position; i++) {
            if (wordArray[i] == null || wordArray[i].getType() == WordType.UNSEEN) {
                cnt++;
            }
        }
        return cnt;
    }
}
