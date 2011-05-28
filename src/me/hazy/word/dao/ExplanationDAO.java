package me.hazy.word.dao;

import static me.hazy.word.dao.WordDatabaseManager.EXPLANATION_ID;
import static me.hazy.word.dao.WordDatabaseManager.EXPLANATION_MEANING;
import static me.hazy.word.dao.WordDatabaseManager.EXPLANATION_WORD_ID;
import static me.hazy.word.dao.WordDatabaseManager.TABLE_EXPLANATION;

import java.util.List;

import me.hazy.word.model.Example;
import me.hazy.word.model.Explanation;
import me.hazy.word.model.Opposite;
import me.hazy.word.model.Synonym;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ExplanationDAO extends AbstractDAO<Explanation> {

    private SQLiteDatabase database;

    private SynonymDAO synonymDAO;
    
    private ExampleDAO exampleDAO;

    private final OppositeDAO oppositeDAO;
    
    public ExplanationDAO(SQLiteDatabase database, ExampleDAO exampleDAO, SynonymDAO synonymDAO, OppositeDAO oppositeDAO) {
        this.database = database;
        this.exampleDAO = exampleDAO;
        this.synonymDAO = synonymDAO;
        this.oppositeDAO = oppositeDAO;
    }

    public long create(Explanation explanation, long wordId) {
        ContentValues values = new ContentValues();
        values.put(EXPLANATION_MEANING, explanation.getMeaning());
        values.put(EXPLANATION_WORD_ID, wordId);
        return database.insert(TABLE_EXPLANATION, null, values);
    }
    
    public List<Explanation> getByWordId(long wordId) {
        Cursor cursor = database.query(TABLE_EXPLANATION, 
                new String[] { EXPLANATION_ID, EXPLANATION_MEANING, EXPLANATION_WORD_ID }, 
                EXPLANATION_WORD_ID + " = ?", new String[] {Long.toString(wordId)}, null, null, EXPLANATION_ID);
        return fetch(cursor);
    }
    
    protected void populate(Explanation explanation) {
        List<Synonym> synonyms = synonymDAO.getByExplanationId(explanation.getId());
        explanation.setSynonyms(synonyms);
        List<Example> examples = exampleDAO.getByExplanationId(explanation.getId());
        explanation.setExamples(examples);
        List<Opposite> opposites = oppositeDAO.getByExplanationId(explanation.getId());
        explanation.setOpposites(opposites);
    }

    @Override
    protected Explanation getEntiry(Cursor cursor) {
        Explanation explanation = new Explanation();
        explanation.setId(getLong(cursor, EXPLANATION_ID));
        explanation.setMeaning(getString(cursor, EXPLANATION_MEANING));
        explanation.setWordId(getLong(cursor, EXPLANATION_WORD_ID));
        populate(explanation);
        return explanation;
    }
    
}
