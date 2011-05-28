package me.hazy.word.model;

public enum WordType {

    UNSEEN(0), SEEN(1), REMEMBERED(2);

    private int type;

    private WordType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static WordType valueOf(long value) {
        WordType type = WordType.UNSEEN;
        switch ((int)value) {
        case 0:
            type = WordType.UNSEEN;
            break;
        case 1:
            type = WordType.SEEN;
            break;
        case 2:
            type = WordType.REMEMBERED;
            break;
        }
        return type;
    }
}
