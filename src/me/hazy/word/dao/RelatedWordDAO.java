package me.hazy.word.dao;

import static me.hazy.word.dao.WordDatabaseManager.RELATEDWORD_EXPLANATION_ID;
import static me.hazy.word.dao.WordDatabaseManager.RELATEDWORD_ID;
import static me.hazy.word.dao.WordDatabaseManager.RELATEDWORD_NAME;

import java.util.List;

import me.hazy.word.model.RelatedWord;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class RelatedWordDAO<T extends RelatedWord> extends AbstractDAO<T> {

    private final SQLiteDatabase database;

    public RelatedWordDAO(SQLiteDatabase database) {
        this.database = database;
    }

    protected abstract String getTableName();
    
    public long create(T relatedWord, long explanationId) {
        ContentValues values = new ContentValues();
        values.put(RELATEDWORD_NAME, relatedWord.getName());
        values.put(RELATEDWORD_EXPLANATION_ID, explanationId);
        return database.insert(getTableName(), null, values);
    }
    
    public List<T> getByExplanationId(long explanationId) {
        Cursor cursor = database.query(getTableName(), 
                new String[] { RELATEDWORD_ID, RELATEDWORD_NAME, RELATEDWORD_EXPLANATION_ID }, 
                RELATEDWORD_EXPLANATION_ID + " = ?", new String[] {Long.toString(explanationId)}, 
                null, null, RELATEDWORD_EXPLANATION_ID);
        return fetch(cursor);
    }
    
    @Override
    protected T getEntiry(Cursor cursor) {
        T opposite = createRelatedWord();
        opposite.setId(getLong(cursor, RELATEDWORD_ID));
        opposite.setName(getString(cursor, RELATEDWORD_NAME));
        opposite.setExplanationId(getLong(cursor, RELATEDWORD_EXPLANATION_ID));
        return opposite;
    }

    protected abstract T createRelatedWord();

}
