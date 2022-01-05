package com.zx.library

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.core.widget.doAfterTextChanged

/**
 * @作者： liqiang
 * @日期： 2022/1/5 14:04
 * @描述：
 **/
class VerificationCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var codeSize = 0
    private var codeWidth = 0
    private var codeHeight = 0
    private var codeMargin = 0
    private var codeColor = 0
    private var codeCorner = 0
    private var codeStrokeWidth = 0
    private var codeStrokeColor = 0

    private var cursorWidth = 0
    private var cursorHeight = 0
    private var cursorColor = 0
    private var cursorCorner = 0
    private val cursorInterval = 500

    private var minHeight = 0

    private lateinit var linearLayout: LinearLayout
    private lateinit var cursorView: View

    companion object {
        const val MSG_HIDE_CURSOR = 1
        const val MSG_SHOW_CURSOR = 2
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(@NonNull msg: Message) {
            if (msg.what == MSG_HIDE_CURSOR) {
                cursorView.visibility = GONE
                sendEmptyMessageDelayed(MSG_SHOW_CURSOR, cursorInterval.toLong())
            } else if (msg.what == MSG_SHOW_CURSOR) {
                cursorView.visibility = VISIBLE
                sendEmptyMessageDelayed(MSG_HIDE_CURSOR, cursorInterval.toLong())
            }
        }
    }

    init {
        initEditText()
        initTextView()
        initCursorView()
    }

    private fun initEditText() {
        val editText = CodeEditText(context)
        editText.filters = arrayOf<InputFilter>(LengthFilter(6))
        editText.imeOptions = EditorInfo.IME_FLAG_NO_EXTRACT_UI
        editText.inputType = EditorInfo.TYPE_CLASS_NUMBER
        editText.alpha = 0f
        editText.gravity = Gravity.CENTER
        editText.isCursorVisible = true
        editText.setBackgroundColor(Color.TRANSPARENT)
        editText.minHeight = minHeight
        val editTextParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(editText, editTextParams)
        editText.doAfterTextChanged {
            val text = it.toString().trim()
            val count: Int = linearLayout.childCount
            for (i in 0 until count) {
                val child = linearLayout.getChildAt(i) as TextView
                if (i < text.length) {
                    child.text = text[i].toString()
                } else {
                    child.text = null
                }
                if (text.isEmpty()) {
                    setMargin(0)
                } else if (i == text.length - 1) {
                    setMargin(i + 1)
                }
            }
        }
    }

    private fun initTextView() {
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.minimumHeight = minHeight
        val linearLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(linearLayout, linearLayoutParams)
        for (i in 0..codeSize) {
            val view = TextView(context)
            view.setTextColor(codeColor)
            view.textSize = 16f
            view.gravity = Gravity.CENTER
            val params = LinearLayout.LayoutParams(0, height)
            params.weight = 1f
            if (i != 0) {
                params.leftMargin = codeMargin
            }
            val drawable = GradientDrawable()
            drawable.setStroke(codeStrokeWidth, codeStrokeColor)
            drawable.cornerRadius = codeCorner.toFloat()
            view.background = drawable
            linearLayout.addView(view, params)
        }
    }

    private fun initCursorView() {
        cursorView = View(context)
        val drawable = GradientDrawable()
        drawable.setColor(cursorColor)
        drawable.cornerRadius = cursorCorner.toFloat()
        cursorView.background = drawable
        val cursorParams = LayoutParams(cursorWidth, cursorHeight)
        cursorParams.gravity = Gravity.CENTER_VERTICAL
        addView(cursorView, cursorParams)
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (linearLayout.childCount > 0) {
                    codeWidth = linearLayout.getChildAt(0).width
                    setMargin(0)
                    handler.sendEmptyMessageDelayed(MSG_HIDE_CURSOR, cursorInterval.toLong())
                }
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setMargin(i: Int) {
        val margin: Int = if (i < linearLayout.childCount) {
            val textView = linearLayout.getChildAt(i) as TextView
            (textView.x + codeWidth / 2 - cursorWidth / 2).toInt()
        } else {
            width
        }
        val params = cursorView.layoutParams as LayoutParams
        params.leftMargin = margin
        cursorView.layoutParams = params
    }

    fun setText(text: String?) {
        if (text == null || text.trim().isEmpty()) return
        if (linearLayout.childCount != 6) return
        val length = text.length.coerceAtMost(6)
        for (i in 0 until length) {
            val textView = linearLayout.getChildAt(i) as TextView
            textView.text = text[i].toString()
        }
    }

    override fun onDetachedFromWindow() {
        handler.removeMessages(MSG_HIDE_CURSOR)
        handler.removeMessages(MSG_SHOW_CURSOR)
        super.onDetachedFromWindow()
    }

    private fun dp2px(value: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, resources.displayMetrics).toInt()
    }
}



