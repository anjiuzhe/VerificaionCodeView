package com.zx.library

import android.R
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

/**
 * @作者： liqiang
 * @日期： 2022/1/5 14:40
 * @描述：
 **/
internal class CodeEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (selStart == selEnd) {
            setSelection(text?.length ?: 0)
        }
    }

    override fun onTextContextMenuItem(id: Int): Boolean {
        if (id == R.id.paste) {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (clipboardManager.text != null) {
                val text = clipboardManager.text.toString()
                setText(text)
            }
        }
        return super.onTextContextMenuItem(id)
    }

    override fun getTextSelectHandleLeft(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    override fun getTextSelectHandleRight(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }
}