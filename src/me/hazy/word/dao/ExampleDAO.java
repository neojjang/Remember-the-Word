package me.hazy.word.dao;

import static me.hazy.word.dao.WordDatabaseManager.EXAMPLE_EXPLANATION_ID;
import static me.hazy.word.dao.WordDatabaseManager.EXAMPLE_ID;
import static me.hazy.word.dao.WordDatabaseManager.EXAMPLE_SENTENSE;
import static me.hazy.word.dao.WordDatabaseManager.TABLE_EXAMPLE;

import java.util.List;

import me.hazy.word.model.Example;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExampleDAO extends AbstractDAO<Example> {

    private SQLiteDatabase database;

    public ExampleDAO(SQLiteDatabase database) {
        this.database = database;
    }

    public long create(Example example, long explanationId) {
        ContentValues values = new ContentValues();
        values.put(EXAMPLE_SENTENSE, example.getSentense());
        values.put(EXAMPLE_EXPLANATION_ID, explanationId);
        return database.insert(TABLE_EXAMPLE, null, values);
    }

    public List<Example> getByExplanationId(long explanationId) {
        Cursor cursor = database.query(TABLE_EXAMPLE, 
                new String[] { EXAMPLE_ID, EXAMPLE_SENTENSE, EXAMPLE_EXPLANATION_ID }, 
                EXAMPLE_EXPLANATION_ID + " = ?", new String[] {Long.toString(explanationId)}, null, null, EXAMPLE_ID);
        return fetch(cursor);
    }
    
    @Override
    protected Example getEntiry(Cursor cursor) {
        Example example = new Example();
        example.setId(getLong(cursor, EXAMPLE_ID));
        example.setSentense(getString(cursor, EXAMPLE_SENTENSE));
        example.setExplanationId(getLong(cursor, EXAMPLE_EXPLANATION_ID));
        return example;
    }

}
