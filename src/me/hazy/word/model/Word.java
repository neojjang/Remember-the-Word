package me.hazy.word.model;

import java.util.ArrayList;
import java.util.List;

public class Word extends AbstractModel {

    private String name;

    private String part;

    private String phonetic;

    private WordType type;

    private List<Explanation> explanations = new ArrayList<Explanation>();

    public Word(String name, String part, String phonetic, WordType type) {
        super();
        this.name = name;
        this.part = part;
        this.phonetic = phonetic;
        this.type = type;
    }

    public Word() {
    }

    public String getPart() {
        return part.length() > 4 ? part.substring(0, 3) + "." : part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public List<Explanation> getExplanations() {
        return explanations;
    }

    public void setExplanations(List<Explanation> explanations) {
        this.explanations = explanations;
    }

    public void addExplanation(Explanation explanation) {
        this.explanations.add(explanation);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WordType getType() {
        return type;
    }

    public void setType(WordType type) {
        this.type = type;
    }

    public boolean isRemembered() {
        return type == WordType.REMEMBERED ? true : false;
    }

}
