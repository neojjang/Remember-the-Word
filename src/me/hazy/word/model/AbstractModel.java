package me.hazy.word.model;

public abstract class AbstractModel {

    private long id;

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
