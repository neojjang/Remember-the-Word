package me.hazy.word.model;

import java.util.ArrayList;
import java.util.List;

public class Explanation extends AbstractModel {

    private List<Synonym> synonyms = new ArrayList<Synonym>();

    private List<Opposite> opposites = new ArrayList<Opposite>();

    private String meaning;

    private long wordId;

    private List<Example> examples = new ArrayList<Example>();

    public Explanation(String meaning) {
        super();
        this.setMeaning(meaning);
    }

    public Explanation() {
    }

    public void addSynonym(String synonym) {
        this.synonyms.add(new Synonym(synonym));
    }

    public List<Synonym> getSynonyms() {
        return synonyms;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning() {
        return meaning;
    }

    public void addExample(String sentense) {
        this.examples.add(new Example(sentense));
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setWordId(long wordId) {
        this.wordId = wordId;
    }

    public long getWordId() {
        return wordId;
    }

    public void setSynonyms(List<Synonym> synonyms) {
        this.synonyms = synonyms;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }

    public void setOpposites(List<Opposite> opposites) {
        this.opposites = opposites;
    }

    public List<Opposite> getOpposites() {
        return opposites;
    }
    
    public void addOpposite(String opposite) {
        this.opposites.add(new Opposite(opposite));
    }

}
