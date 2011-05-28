package me.hazy.word.dao;

import java.io.File;

import me.hazy.word.util.DataTransferUtil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class WordDatabaseManager {

    private SQLiteDatabase database;

    private final Context context;

    private WordDAO wordDAO;

    private ExplanationDAO explanationDAO;

    private ExampleDAO exampleDAO;

    private SynonymDAO synonymDAO;

    private OppositeDAO oppositeDAO;

    public static final String DATABASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DATABASE_FILENAME = "words.db";
    public static final String DATABASE_CREATE_FILE_PATH = DATABASE_PATH + "/" + DATABASE_FILENAME;
    public static final int DATABASE_VERSION = 1;

    // Tables
    public static final String TABLE_WORD = "word";
    public static final String TABLE_EXPLANATION = "explanation";
    public static final String TABLE_SYNONYM = "synonym";
    public static final String TABLE_OPPOSITE = "opposite";
    public static final String TABLE_EXAMPLE = "example";

    // Table Word
    public static final String WORD_ID = "_id";
    public static final String WORD_NAME = "name";
    public static final String WORD_PART = "part";
    public static final String WORD_PHONETIC = "phonetic";
    public static final String WORD_TYPE = "type";

    // Table Explanation
    public static final String EXPLANATION_ID = "_id";
    public static final String EXPLANATION_MEANING = "meaning";
    public static final String EXPLANATION_WORD_ID = "word_id";

    // Table Opposite & Synonum
    public static final String RELATEDWORD_ID = "_id";
    public static final String RELATEDWORD_NAME = "name";
    public static final String RELATEDWORD_EXPLANATION_ID = "explanation_id";

    // Table Explanation
    public static final String EXAMPLE_ID = "_id";
    public static final String EXAMPLE_SENTENSE = "sentense";
    public static final String EXAMPLE_EXPLANATION_ID = "explanation_id";

    public WordDatabaseManager(Context context) {
        super();
        this.context = context;
    }

    public void open() {
        openOrCreateDatabase();
        createDAOs();
    }

    private void createDAOs() {
        this.exampleDAO = new ExampleDAO(database);
        this.synonymDAO = new SynonymDAO(database);
        this.oppositeDAO = new OppositeDAO(database);
        this.explanationDAO = new ExplanationDAO(database, exampleDAO, synonymDAO, oppositeDAO);
        this.wordDAO = new WordDAO(database, explanationDAO);
    }

    public int getVersion() {
        return database.getVersion();
    }

    private void openOrCreateDatabase() {
        database = context.openOrCreateDatabase(DATABASE_FILENAME, Context.MODE_PRIVATE, null);
        context.openOrCreateDatabase(DATABASE_FILENAME, Context.MODE_PRIVATE, null);
    }

    public String getPathDatabase() {
        return database.getPath();
    }

    public void openDatabaseForCreate() {
        new File(DATABASE_CREATE_FILE_PATH).delete();
        database = SQLiteDatabase.openDatabase(WordDatabaseManager.DATABASE_CREATE_FILE_PATH, null,
                SQLiteDatabase.OPEN_READWRITE | SQLiteDatabase.CREATE_IF_NECESSARY);
        database.setVersion(DATABASE_VERSION);
        createTables();
        createDAOs();
    }

    private void createTables() {
        final String CREATE_WORD_TABLE = "create table " + TABLE_WORD + " ( " + WORD_ID
                + " integer primary key autoincrement, " + WORD_NAME + " text, " + WORD_PART + " text, "
                + WORD_PHONETIC + " text, " + WORD_TYPE + " integer);";
        final String CREATE_EXPLANATION_TABLE = "create table " + TABLE_EXPLANATION + " ( " + EXPLANATION_ID
                + " integer primary key autoincrement, " + EXPLANATION_MEANING + " text, "
                + EXPLANATION_WORD_ID + " integer);";
        final String CREATE_SYNONUM_TABLE = "create table " + TABLE_SYNONYM + " ( " + RELATEDWORD_ID
                + " integer primary key autoincrement, " + RELATEDWORD_NAME + " text, "
                + RELATEDWORD_EXPLANATION_ID + " integer);";
        final String CREATE_OPPOSITE_TABLE = "create table " + TABLE_OPPOSITE + " ( " + RELATEDWORD_ID
                + " integer primary key autoincrement, " + RELATEDWORD_NAME + " text, "
                + RELATEDWORD_EXPLANATION_ID + " integer);";
        final String CREATE_EXAMPLE_TABLE = "create table " + TABLE_EXAMPLE + " ( " + EXAMPLE_ID
                + " integer primary key autoincrement, " + EXAMPLE_SENTENSE + " text, "
                + EXAMPLE_EXPLANATION_ID + " integer);";
        try {
            database.execSQL(CREATE_WORD_TABLE);
            database.execSQL(CREATE_EXPLANATION_TABLE);
            database.execSQL(CREATE_SYNONUM_TABLE);
            database.execSQL(CREATE_OPPOSITE_TABLE);
            database.execSQL(CREATE_EXAMPLE_TABLE);
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
        }
    }

    public void close() {
        if (database != null && database.isOpen())
            database.close();
    }

    public WordDAO getWordDAO() {
        return wordDAO;
    }

    public ExplanationDAO getExplanationDAO() {
        return explanationDAO;
    }

    public ExampleDAO getExampleDAO() {
        return exampleDAO;
    }

    public SynonymDAO getSynonymDAO() {
        return synonymDAO;
    }

    public OppositeDAO getOppositeDAO() {
        return oppositeDAO;
    }

    public void restoreDatabaseIfNotExist() {
        File databaseFile = context.getDatabasePath(DATABASE_FILENAME);
        if (!databaseFile.exists()) {
            close();
            DataTransferUtil.restoreDatabaseFileFromResource(context.getResources(), databaseFile.getAbsolutePath());
        }        
    }

}