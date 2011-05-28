package me.hazy.word.util;

import me.hazy.word.model.ApplicationMode;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtil {

    private static final String PREFERENCE_MODE = "mode";
    
    private static final String PREFERENCE_LAST_LOCATION = "lastLocation";

    private SharedPreferences prefs;

    public PreferenceUtil(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public ApplicationMode getApplicationMode() {
        String mode = prefs.getString(PREFERENCE_MODE, ApplicationMode.HIDE_STAR.toString());
        return ApplicationMode.valueOf(mode);
    }
    
    public void setApplicationMode(ApplicationMode mode) {
        Editor editor = prefs.edit();
        editor.putString(PREFERENCE_MODE, mode.toString());
        editor.commit();
    }
    
    public int getLastLocation() {
        int location = prefs.getInt(PREFERENCE_LAST_LOCATION, 0);
        return location;
    }
    
    public void setLastLocation(int loc) {
        Editor editor = prefs.edit();
        editor.putInt(PREFERENCE_LAST_LOCATION, loc);
        editor.commit();
    }
   
}
