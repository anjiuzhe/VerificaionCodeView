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
import androidx.core.app.ActivityCompat
import androidx.core.widget.doAfterTextChanged

/**
 * @作者： liqiang
 * @日期： 2022/1/5 14:04
 * @描述：
 **/
class VerificationCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var codeNumber = 0
    private var textSize = 0f
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

    private lateinit var linearLayout: LinearLayout
    private lateinit var cursorView: View
    private var listener: OnCodeChangeListener? = null

    companion object {
        const val cursorInterval = 500L
        const val MSG_HIDE_CURSOR = 1
        const val MSG_SHOW_CURSOR = 2
    }

    private val handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(@NonNull msg: Message) {
            if (msg.what == MSG_HIDE_CURSOR) {
                cursorView.visibility = GONE
                sendEmptyMessageDelayed(MSG_SHOW_CURSOR, cursorInterval)
            } else if (msg.what == MSG_SHOW_CURSOR) {
                cursorView.visibility = VISIBLE
                sendEmptyMessageDelayed(MSG_HIDE_CURSOR, cursorInterval)
            }
        }
    }

    init {
        initAttr(context, attrs)
        initEditText()
        initTextView()
        initCursorView()
    }

    private fun initAttr(context: Context, attrs: AttributeSet? = null) {
        if (attrs == null) return
        val ta = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeView)
        codeNumber = ta.getInteger(
            R.styleable.VerificationCodeView_verify_code_number,
            resources.getInteger(R.integer.verify_code_number)
        )
        textSize = ta.getDimension(
            R.styleable.VerificationCodeView_verify_text_size,
            resources.getDimension(R.dimen.verify_text_size)
        )
        codeWidth = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_code_width,
            resources.getDimensionPixelSize(R.dimen.verify_code_width)
        )
        codeHeight = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_code_height,
            resources.getDimensionPixelSize(R.dimen.verify_code_height)
        )
        codeMargin = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_code_margin,
            resources.getDimensionPixelSize(R.dimen.verify_code_margin)
        )
        codeColor = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_code_color,
            ActivityCompat.getColor(context, R.color.verify_code_color)
        )
        codeCorner = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_code_corner,
            resources.getDimensionPixelSize(R.dimen.verify_code_corner)
        )
        codeStrokeWidth = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_code_stroke_width,
            resources.getDimensionPixelSize(R.dimen.verify_code_stroke_width)
        )
        codeStrokeColor = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_code_stroke_color,
            ActivityCompat.getColor(context, R.color.verify_code_stroke_color)
        )
        cursorWidth = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_cursor_width,
            resources.getDimensionPixelSize(R.dimen.verify_cursor_width)
        )
        cursorHeight = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_cursor_height,
            resources.getDimensionPixelSize(R.dimen.verify_cursor_height)
        )
        cursorColor = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_cursor_color,
            ActivityCompat.getColor(context, R.color.verify_cursor_color)
        )
        cursorCorner = ta.getDimensionPixelSize(
            R.styleable.VerificationCodeView_verify_cursor_corner,
            resources.getDimensionPixelSize(R.dimen.verify_cursor_corner)
        )
        ta.recycle()
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
                    setCursorMargin(0)
                } else if (i == text.length - 1) {
                    setCursorMargin(i + 1)
                }
            }
        }
    }

    private fun initTextView() {
        linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        val linearLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(linearLayout, linearLayoutParams)
        for (i in 0 until codeNumber) {
            val view = TextView(context)
            view.setTextColor(codeColor)
            view.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize)
            view.gravity = Gravity.CENTER
            val params = LinearLayout.LayoutParams(codeWidth, codeHeight)
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
        cursorParams.leftMargin = -cursorWidth
        addView(cursorView, cursorParams)
        viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (linearLayout.childCount > 0) {
                    codeWidth = linearLayout.getChildAt(0).width
                    setCursorMargin(0)
                    handler.sendEmptyMessageDelayed(MSG_HIDE_CURSOR, cursorInterval)
                }
                viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun setCursorMargin(i: Int) {
        val margin: Int = if (i < linearLayout.childCount) {
            val textView = linearLayout.getChildAt(i) as TextView
            (textView.x + codeWidth / 2 - cursorWidth / 2).toInt()
        } else {
            -cursorWidth
        }
        val params = cursorView.layoutParams as LayoutParams
        params.leftMargin = margin
        cursorView.layoutParams = params
    }

    override fun onDetachedFromWindow() {
        handler.removeMessages(MSG_HIDE_CURSOR)
        handler.removeMessages(MSG_SHOW_CURSOR)
        super.onDetachedFromWindow()
    }

    fun setText(text: String?) {
        if (text == null || text.trim().isEmpty()) return
        if (linearLayout.childCount != codeNumber) return
        val length = text.length.coerceAtMost(codeNumber)
        for (i in 0 until length) {
            val textView = linearLayout.getChildAt(i) as TextView
            textView.text = text[i].toString()
        }
    }

    fun setOnCodeChangeListener(listener: OnCodeChangeListener) {
        this.listener = listener
    }

}



