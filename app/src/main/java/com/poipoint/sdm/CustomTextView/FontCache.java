package com.poipoint.sdm.CustomTextView;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Tanmay on 5/15/2016.
 */
public class FontCache {

    private static HashMap<String, Typeface> fontCache = new HashMap<>();
    public static String BOLD_AVENIR_LTStd="fonts/AvenirLTStd-Bold.otf";
    public static String BOOK_AVENIR_LTStd="fonts/AvenirLTStd-Book.otf";
    public static String HEAVY_AVENIR_LTStd="fonts/AvenirLTStd-Heavy.otf";
    public static String MEDIUM_AVENIR_LTStd="fonts/AvenirLTStd-Medium.otf";
    public static Typeface getTypeface(String fontname, Context context) {
        Typeface typeface = fontCache.get(fontname);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontname, typeface);
        }

        return typeface;
    }
}