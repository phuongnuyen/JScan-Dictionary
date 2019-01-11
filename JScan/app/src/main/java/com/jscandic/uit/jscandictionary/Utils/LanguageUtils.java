package com.jscandic.uit.jscandictionary.Utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import com.jscandic.uit.jscandictionary.R;

import java.util.Locale;

public class LanguageUtils {
    public static final String DEFAULT_LANGUAGE = "";
    public static final String ENGLISH_US = "en";
    public static final String VIETNAMESE = "vn";

    public static String curLanguage = DEFAULT_LANGUAGE;
    public static int flagIconResource;


    public static void setLanguage(Activity activity, String language){
        if (curLanguage.equals(language))
            return;

        curLanguage = language;
        setFlagIconID();

        Locale locale = new Locale(language);
        Resources res = activity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = locale;
        res.updateConfiguration(conf, dm);
        activity.recreate();
    }

    private static void setFlagIconID(){
        switch (curLanguage){
            case DEFAULT_LANGUAGE:

            case VIETNAMESE:
                flagIconResource = R.drawable.ic_flag_vn;
                break;

            case ENGLISH_US:
                flagIconResource = R.drawable.ic_flag_us;
                break;
        }
    }
}
