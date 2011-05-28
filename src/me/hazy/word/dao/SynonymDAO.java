package me.hazy.word.dao;

import me.hazy.word.model.Synonym;
import android.database.sqlite.SQLiteDatabase;

public class SynonymDAO extends RelatedWordDAO<Synonym> {

    public SynonymDAO(SQLiteDatabase database) {
        super(database);
    }

    @Override
    protected Synonym createRelatedWord() {
        return new Synonym();
    }

    @Override
    protected String getTableName() {
        return WordDatabaseManager.TABLE_SYNONYM;
    }
}
