package me.hazy.word.model;

public class Example extends AbstractModel{

    private String sentense;
    
    private long explanationId;
    
    public void setSentense(String sentense) {
        this.sentense = sentense;
    }

    public String getSentense() {
        return sentense;
    }

    public Example() {
        super();
    }

    public Example(String sentense) {
        super();
        this.sentense = sentense;
    }

    public void setExplanationId(long explanationId) {
        this.explanationId = explanationId;
    }

    public long getExplanationId() {
        return explanationId;
    }
    
}
