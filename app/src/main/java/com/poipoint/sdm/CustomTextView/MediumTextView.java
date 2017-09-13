package com.poipoint.sdm.CustomTextView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Tanmay on 5/15/2016.
 */
public class MediumTextView extends TextView {

    public MediumTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public MediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public MediumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(FontCache.MEDIUM_AVENIR_LTStd, context);
        setTypeface(customFont);
    }
}
