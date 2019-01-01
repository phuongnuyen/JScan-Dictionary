package com.jscandic.uit.jscandictionary.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import com.jscandic.uit.jscandictionary.R;

public class ThemeUtils {
    public final static int THEME_DEFAULT = 0;
    public final static int THEME_GREEN = 1;
    public final static int THEME_BLUE = 2;
    public final static int THEME_RED = 3;
    private static int themeID;

    //  set theme for activity and restart it for changing to new theme
    public static void changeToTheme(Activity activity, int theme){
        if (themeID == theme)
            return;
        themeID = theme;
        activity.recreate();
    }

    //  check and set theme
    public static void onActivityCreatedSetTheme(Activity activity){
        switch (themeID){
            case THEME_DEFAULT: //the default theme is Theme Green
            case THEME_GREEN:
                activity.setTheme(R.style.AppTheme_Green);
                break;

            case THEME_BLUE:
                activity.setTheme(R.style.AppTheme_Blue);
                break;

            case THEME_RED:
                activity.setTheme(R.style.AppTheme_Red);
                break;
        }
    }


    //  get color from current theme
    @ColorInt
    public static int getThemeColor
    (
            @NonNull final Context context,
            @AttrRes final int attributeColor
    )
    {
        final TypedValue value = new TypedValue();
        context.getTheme ().resolveAttribute (attributeColor, value, true);
        return value.data;
    }


    //  set icon (drawable) color
    public static Drawable changeColorIcon(Activity activity, int iconID, int colorID){
        Drawable icon = ContextCompat.getDrawable(activity, iconID);
        icon.setColorFilter(ContextCompat.getColor(activity, colorID), PorterDuff.Mode.MULTIPLY);
        return icon;
    }

}
