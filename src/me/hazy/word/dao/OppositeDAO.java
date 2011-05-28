package me.hazy.word.dao;

import me.hazy.word.model.Opposite;
import android.database.sqlite.SQLiteDatabase;

public class OppositeDAO extends RelatedWordDAO<Opposite> {

    public OppositeDAO(SQLiteDatabase database) {
        super(database);
    }

    @Override
    protected Opposite createRelatedWord() {
        return new Opposite();
    }

    @Override
    protected String getTableName() {
        return WordDatabaseManager.TABLE_OPPOSITE;
    }

}
