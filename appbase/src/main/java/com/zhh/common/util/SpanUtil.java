package com.zhh.common.util;

import android.text.*;
import android.text.style.ForegroundColorSpan;

/**
 * Created by MHL on 2016/9/2.
 */
public class SpanUtil {

    public static SpannableString applyFountgroundColorSpan(String total, String span, int color) {

        SpannableString spanString = new SpannableString(total);
        int start = total.indexOf(span);
        int end = start + span.length();
        if (start >= 0 && start < end) {
            spanString.setSpan(new ForegroundColorSpan(color), start, end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanString;
    }

    public static SpannableString applyFountgroundColorSpan(String total, String[] spans, int[] colors) {

        SpannableString spanString = new SpannableString(total);
        for (int i = 0; i < spans.length; i++) {
            int start = total.indexOf(spans[i]);
            int end = start + spans[i].length();
            spanString.setSpan(new ForegroundColorSpan(colors[i]),
                    start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spanString;
    }
}
