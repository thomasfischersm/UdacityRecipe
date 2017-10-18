package com.playposse.udacityrecipe.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

/**
 * A utility for loading fonts from the resources.
 */
public final class FontUtil {

    public static final String CORMORANT_REGULAR_FONT = "fonts/Cormorant-Regular.ttf";
    public static final String CORMORANT_BOLD_FONT = "fonts/Cormorant-Bold.ttf";

    private FontUtil() {}

    public static void apply(TextView textView, String fontFile) {
        Context context = textView.getContext();

        Typeface font = Typeface.createFromAsset(context.getAssets(), fontFile);
        textView.setTypeface(font);
    }
}
