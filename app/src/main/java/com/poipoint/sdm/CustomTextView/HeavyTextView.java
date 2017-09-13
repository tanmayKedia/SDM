package com.poipoint.sdm.CustomTextView;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class HeavyTextView extends TextView {

    public HeavyTextView(Context context) {

        super(context);
        applyCustomFont(context);
    }

    public HeavyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public HeavyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(FontCache.HEAVY_AVENIR_LTStd, context);
        setTypeface(customFont);
    }
}
