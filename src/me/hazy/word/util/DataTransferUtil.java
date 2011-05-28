package me.hazy.word.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import me.hazy.word.R;
import me.hazy.word.dao.ExampleDAO;
import me.hazy.word.dao.ExplanationDAO;
import me.hazy.word.dao.OppositeDAO;
import me.hazy.word.dao.SynonymDAO;
import me.hazy.word.dao.WordDAO;
import me.hazy.word.dao.WordDatabaseManager;
import me.hazy.word.model.Example;
import me.hazy.word.model.Explanation;
import me.hazy.word.model.Opposite;
import me.hazy.word.model.Synonym;
import me.hazy.word.model.Word;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

public class DataTransferUtil {

    
    public static void jsonToDatabase(WordDatabaseManager databaseManager, AssetManager assetManager) {
        databaseManager.openDatabaseForCreate();
        WordDAO wordDAO = databaseManager.getWordDAO();
        ExplanationDAO explanationDAO = databaseManager.getExplanationDAO();
        ExampleDAO exampleDAO = databaseManager.getExampleDAO();
        SynonymDAO synonymDAO = databaseManager.getSynonymDAO();
        OppositeDAO oppositeDAO = databaseManager.getOppositeDAO();
        Log.i("TAG", "DataTransferUtil.jsonToDatabase: start createing database");
        List<Word> words = ModelParser.parse(assetManager);
        int count = 0;
        for (Word word : words) {
            long wordId = wordDAO.create(word);
            List<Explanation> explanations = word.getExplanations();
            for (Explanation explanation : explanations) {
                long explanationId = explanationDAO.create(explanation, wordId);
                List<Synonym> synonyms = explanation.getSynonyms();
                for (Synonym synonym : synonyms) {
                    synonymDAO.create(synonym, explanationId);
                }
                List<Example> examples = explanation.getExamples();
                for (Example example : examples) {
                    exampleDAO.create(example, explanationId);
                }
                List<Opposite> opposites = explanation.getOpposites();
                for (Opposite opposite : opposites) {
                    oppositeDAO.create(opposite, explanationId);
                }
            }
            count++;
            Log.i("TAG", "DataTransferUtil.jsonToDatabase: finish word " + word.getName() + " (" + count
                        + " of " + words.size() + ")");
        }
        databaseManager.close();
    }

    public static void restoreDatabaseFileFromResource(Resources resources, String databasePath) {
        InputStream inputStream = resources.openRawResource(R.raw.words);
        FileOutputStream fos;
        try {
            File databaseFile = new File(databasePath);
            databaseFile.getParentFile().mkdirs();
            databaseFile.createNewFile();
            fos = new FileOutputStream(databaseFile);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            inputStream.close();
            Log.i("TAG", "RemeberTheWordApplication.onCreate:restrore file success");
        } catch (Exception e) {
            Log.d("TAG", "DataTransferUtil.restoreDatabaseFileFromResource: restore file failed");
        }
    }

}
