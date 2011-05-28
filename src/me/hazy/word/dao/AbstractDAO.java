package me.hazy.word.dao;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

public abstract class AbstractDAO<T> {

    protected abstract T getEntiry(Cursor cursor);

    protected List<T> fetch(Cursor cursor) {
        List<T> entities = new ArrayList<T>(cursor.getCount());
        try {
            if (cursor.moveToFirst()) {
                do {
                    T entity = getEntiry(cursor);
                    entities.add(entity);
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }
        return entities;
    }

    protected Long getLong(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex == -1 ? 0L : cursor.getLong(columnIndex);
    }

    protected String getString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        return columnIndex == -1 ? "" : cursor.getString(columnIndex);
    }
}
