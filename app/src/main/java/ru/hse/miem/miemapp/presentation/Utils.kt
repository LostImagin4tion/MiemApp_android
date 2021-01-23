package ru.hse.miem.miemapp.presentation

import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.text.color
import androidx.core.text.inSpans
import ru.hse.miem.miemapp.R

/**
 * Some useful TextView utils
 */
object TextViewUtils {

    /**
     * Performs concatenation of name and value, coloring and making name medium
     * @return Name (medium, colored)<sep>Value
     */
    fun View.makeNameValueString(
        @StringRes nameId: Int,
        value: String,
        @ColorRes colorId: Int = R.color.colorAccentDark,
        sep: String = " "
    ) = SpannableStringBuilder()
        .color(resources.getColor(colorId)) {
            inSpans(TypefaceSpan("sans-serif-medium")) { append(context.getString(nameId)) }
        }
        .append(sep)
        .append(value)
}