package me.hazy.word.model;

public class RelatedWord extends AbstractModel {
    
    private String name;

    private long explanationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RelatedWord(String name) {
        super();
        this.name = name;
    }

    public RelatedWord() {
    }

    public void setExplanationId(long explanationId) {
        this.explanationId = explanationId;
    }

    public long getExplanationId() {
        return explanationId;
    }
}
