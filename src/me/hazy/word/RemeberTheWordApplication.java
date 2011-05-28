package me.hazy.word;

import me.hazy.word.dao.WordDAO;
import me.hazy.word.dao.WordDatabaseManager;
import me.hazy.word.dao.WordTravller;
import me.hazy.word.model.ApplicationMode;
import android.app.Application;
import android.database.Cursor;

public class RemeberTheWordApplication extends Application {

    private WordDatabaseManager databaseManager;
    
    @Override
    public void onCreate() {
        super.onCreate();
        databaseManager = new WordDatabaseManager(this);
//         recreate the database from json file
//        DataTransferUtil.jsonToDatabase(databaseManager, getAssets());
        databaseManager.restoreDatabaseIfNotExist();
        databaseManager.open();
    }

    @Override
    public void onTerminate() {
        databaseManager.close();
        super.onTerminate();
    }

    public WordTravller getWordTraveller(ApplicationMode mode) {
        return new WordTravller(databaseManager.getWordDAO(), mode);
    }
    
    public int getRememberedCount() {
        WordDAO wordDAO = databaseManager.getWordDAO();
        Cursor allWords = wordDAO.getAllWords();
        Cursor unrememberedWords = wordDAO.getUnrememberedWords();
        int rememberedCount = allWords.getCount() - unrememberedWords.getCount();
        allWords.close();
        unrememberedWords.close();
        return rememberedCount;
    } 
}
