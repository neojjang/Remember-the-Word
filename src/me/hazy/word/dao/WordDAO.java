package me.hazy.word.dao;

import static me.hazy.word.dao.WordDatabaseManager.TABLE_WORD;
import static me.hazy.word.dao.WordDatabaseManager.WORD_ID;
import static me.hazy.word.dao.WordDatabaseManager.WORD_NAME;
import static me.hazy.word.dao.WordDatabaseManager.WORD_PART;
import static me.hazy.word.dao.WordDatabaseManager.WORD_PHONETIC;
import static me.hazy.word.dao.WordDatabaseManager.WORD_TYPE;

import java.util.List;

import me.hazy.word.model.Explanation;
import me.hazy.word.model.Word;
import me.hazy.word.model.WordType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WordDAO extends AbstractDAO<Word> {

    public static final String[] ALL_FIELDS = new String[] { WORD_ID, WORD_NAME, WORD_PART, WORD_PHONETIC,
            WORD_TYPE };

    private SQLiteDatabase database;

    private final ExplanationDAO explanationDAO;

    public WordDAO(SQLiteDatabase database, ExplanationDAO explanationDAO) {
        this.database = database;
        this.explanationDAO = explanationDAO;
    }

    public long create(Word word) {
        ContentValues values = createContentValue(word);
        return database.insert(TABLE_WORD, null, values);
    }

    private ContentValues createContentValue(Word word) {
        ContentValues values = new ContentValues();
        values.put(WORD_NAME, word.getName());
        values.put(WORD_PART, word.getPart());
        values.put(WORD_PHONETIC, word.getPhonetic());
        values.put(WORD_TYPE, word.getType().getType());
        return values;
    }

    protected void populate(Word word) {
        List<Explanation> explanations = explanationDAO.getByWordId(word.getId());
        word.setExplanations(explanations);
    }

    @Override
    protected Word getEntiry(Cursor cursor) {
        Word word = new Word();
        word.setId(getLong(cursor, WORD_ID));
        word.setName(getString(cursor, WORD_NAME));
        word.setPart(getString(cursor, WORD_PART));
        word.setPhonetic(getString(cursor, WORD_PHONETIC));
        word.setType(WordType.valueOf(getLong(cursor, WORD_TYPE)));
        populate(word);
        return word;
    }

    public Cursor getUnrememberedWords() {
        Cursor cursor = database.query(TABLE_WORD, ALL_FIELDS, 
                WORD_TYPE + "=?", new String[] { Long.toString(WordType.UNSEEN.getType()) }, 
                null, null, WORD_ID);
        return cursor;
    }
    
    public Cursor getAllWords() {
        Cursor cursor = database.query(TABLE_WORD, ALL_FIELDS, null, null, null, null, WORD_ID);
        return cursor;
    }

    public void update(Word word, WordType changedType) {
        word.setType(changedType);
        ContentValues values = createContentValue(word);
        database.update(TABLE_WORD, values, WORD_ID + " = ?", new String[] { Long.toString(word.getId()) });
    }
}
