package com.example.expendabletextview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class ExpendableTextView : ViewGroup {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : this(
        context, attributeSet, defStyle, 0
    )

    constructor(
        context: Context, attributeSet: AttributeSet?, defStyle: Int, defStyleRes: Int
    ) : super(context, attributeSet, defStyle, defStyleRes) {

        context.obtainStyledAttributes(attributeSet, R.styleable.ExpendableTextView).apply {

            truncatedLength = getInteger(R.styleable.ExpendableTextView_truncationLength, 60)
            actionExpendedText =
                getString(R.styleable.ExpendableTextView_actionExpendedText) ?: "View Less"
            actionCollapsedText =
                getString(R.styleable.ExpendableTextView_actionCollapsedText) ?: "View More"
            completeText = getString(R.styleable.ExpendableTextView_completeText) ?: ""

            actionTextColor = getColor(R.styleable.ExpendableTextView_actionTextColor, Color.RED)

            recycle()
        }

        textView = TextView(context, attributeSet, defStyle, defStyleRes)

        init()
    }

    private var completeTextHeight: Int = 0

    private fun init() {
        Log.d(TAG, "init: ")

        addView(textView)
        textView.text = completeText

        textView.measure(
            MeasureSpec.makeMeasureSpec(textView.width, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(textView.height, MeasureSpec.EXACTLY)
        )

        completeTextHeight = textView.layout.height + textView.paddingBottom + textView.paddingTop

        if (completeText.length > truncatedLength) {
            val spannedText =
                getSpannableString(
                    completeText.substring(0, truncatedLength),
                    actionCollapsedText,
                    actionTextColor,
                    ::actionCollapseClicked)
        }
    }


    private var textView: TextView

    private var _completeText: String = ""
    var completeText
        get() = _completeText
        set(value) {
            _completeText = value
        }


    private var _truncatedLength: Int = 0
    var truncatedLength
        get() = _truncatedLength
        set(value) {
            _truncatedLength = value
        }

    private var _actionCollapsedText: String = "View More"
    var actionCollapsedText
        get() = _actionCollapsedText
        set(value) {
            _actionCollapsedText = value
        }

    private var _actionExpendedText: String = "View Less"
    var actionExpendedText
        get() = _actionExpendedText
        set(value) {
            _actionExpendedText = value
        }

    private var _actionTextColor: Int = Color.RED
    var actionTextColor
        get() = _actionTextColor
        set(value) {
            _actionTextColor = value
        }


    protected fun getSpannableString(
        string: String, spanString: String, color: Int, clickAction: () -> Unit
    ): SpannableString {
        SpannableString(string).let {

            val clickSpan = object : ClickableSpan() {

                override fun onClick(widget: View) {
                    clickAction()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = color
                    ds.isUnderlineText = true
                }

            }

            it.setSpan(
                clickSpan,
                (string.length - spanString.length),
                string.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )

            return it
        }
    }

    fun actionCollapseClicked() {

    }

    fun actionExpendClicked() {

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        Log.d(TAG, "onLayout: ")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        Log.d(TAG, "onMeasure: ")
        val mode = MeasureSpec.getMode(widthMeasureSpec) // mode == View.MesaureSpec.EXACTLY
        val size = MeasureSpec.getSize(widthMeasureSpec) // 400
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow: ")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow: ")
    }

    override fun measureChild(
        child: View?, parentWidthMeasureSpec: Int, parentHeightMeasureSpec: Int
    ) {
        super.measureChild(child, parentWidthMeasureSpec, parentHeightMeasureSpec)
        Log.d(TAG, "measureChild: ")
    }

    override fun measureChildWithMargins(
        child: View?,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ) {
        super.measureChildWithMargins(
            child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed
        )
        Log.d(TAG, "measureChildWithMargins: ")
    }

    override fun measureChildren(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.measureChildren(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "measureChildren: ")
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        Log.d(TAG, "draw: ")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw: ")
    }

    companion object {
        private const val TAG = "ExpendableTextView"
    }
}