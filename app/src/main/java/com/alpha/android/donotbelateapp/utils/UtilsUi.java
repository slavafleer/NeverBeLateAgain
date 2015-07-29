package com.alpha.android.donotbelateapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Slava on 28/07/2015.
 */
public class UtilsUi {

    public static TextView createTextButton(Context context,String label) {
        TextView button = new TextView(context);
        button.setText(label);
        button.setGravity(Gravity.CENTER);
        button.setBackgroundColor(0x11000000 + Color.BLACK);
        button.setPadding(dpToPixel(3), dpToPixel(2), dpToPixel(3), dpToPixel(2));

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(dpToPixel(3), dpToPixel(2), dpToPixel(3), dpToPixel(2));
        button.setLayoutParams(params);

        return button;
    }

    // Convert dp units to pixels.
    public static int dpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }
}
