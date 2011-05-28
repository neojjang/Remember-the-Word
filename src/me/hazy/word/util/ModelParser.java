package me.hazy.word.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import me.hazy.word.model.Explanation;
import me.hazy.word.model.Word;
import me.hazy.word.model.WordType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.AssetManager;
import android.util.Log;

public class ModelParser {

    public static final String JSON_FILE = "json.txt";

    private static String loadJsonDataFromFile(AssetManager assetManager) {
        try {
            InputStream is = assetManager.open(JSON_FILE);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Word> parse(AssetManager assetManager) {
        Log.i("TAG", "ModelParser.parse: loading json file");
        String jsonData = loadJsonDataFromFile(assetManager); 
        List<Word> words = new ArrayList<Word>();
        try {
            JSONArray wordsJson = new JSONArray(jsonData);
            Log.i("TAG", "ModelParser.parse: JSONArray created");
            for (int i = 0; i < wordsJson.length(); i++) {
                Word word = parseWord((JSONObject) wordsJson.get(i));
                words.add(word);
            }
            Log.i("TAG", "ModelParser.parse: parsing json to word list finished");
            return words;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String WORD_NAME = "word";

    public static final String WORD_PART = "part_of_speech";

    public static final String WORD_PHONETIC = "phonetic";

    public static final String WORD_EXPLANATIONS = "explanations";

    private static Word parseWord(JSONObject wordJson) throws JSONException {
        String name = wordJson.getString(WORD_NAME);
        String part = wordJson.getString(WORD_PART);
        String phonetic = wordJson.getString(WORD_PHONETIC);
        Word word = new Word(name, part, phonetic, WordType.UNSEEN);

        JSONArray explanationsJsonArray = wordJson.getJSONArray(WORD_EXPLANATIONS);
        for (int i = 0; i < explanationsJsonArray.length(); i++) {
            Explanation explanation = parseExplanations((JSONObject) explanationsJsonArray.get(i));
            word.addExplanation(explanation);
        }
        return word;
    }

    public static final String EXPLANATION_MEANING = "meaning";

    public static final String EXPLANATION_SYNONYMS = "synonyms";

    public static final String EXPLANATION_EXAMPLES = "examples";

    private static final String EXPLANATION_OPPOSITES = "opposites";

    private static Explanation parseExplanations(JSONObject explanationsJson) throws JSONException {
        String meaning = explanationsJson.getString(EXPLANATION_MEANING);
        Explanation explanation = new Explanation(meaning);

        JSONArray synonymsJsonArray = explanationsJson.getJSONArray(EXPLANATION_SYNONYMS);
        for (int i = 0; i < synonymsJsonArray.length(); i++) {
            String synonym = synonymsJsonArray.getString(i);
            explanation.addSynonym(synonym);
        }
        JSONArray oppositesJsonArray = explanationsJson.getJSONArray(EXPLANATION_OPPOSITES);
        for (int i = 0; i < oppositesJsonArray.length(); i++) {
            String opposite = oppositesJsonArray.getString(i);
            explanation.addOpposite(opposite);
        }
        JSONArray examplesJsonArray = explanationsJson.getJSONArray(EXPLANATION_EXAMPLES);
        for (int i = 0; i < examplesJsonArray.length(); i++) {
            String example = examplesJsonArray.getString(i);
            explanation.addExample(example);
        }
        return explanation;
    }
}
