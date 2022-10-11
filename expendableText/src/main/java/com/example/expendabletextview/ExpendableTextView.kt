package com.example.expendabletextview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.animation.doOnEnd

@SuppressLint("AppCompatCustomView")
class ExpendableTextView : TextView {

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

            expandDuration = getInteger(R.styleable.ExpendableTextView_expendDuration, 1200)

            collapseDuration = getInteger(R.styleable.ExpendableTextView_collapseDuration, 1200)

            recycle()
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (firstTime) {
            firstTime = false
            init()
        }
    }

    private var expandedTextSpanned: SpannableString? = null
    private var collapsedTextSpanned: SpannableString? = null

    private var expandedHeight: Int = 0
    private var collapsedHeight: Int = 0

    private var firstTime: Boolean = true

    private fun init() {

        text = "$completeText $actionExpendedText"

        measureLayout()
        expandedHeight = layout.height + paddingBottom + paddingTop

        if (completeText.length > truncatedLength) {

            text = getSpan(
                completeText.substring(0, truncatedLength) + actionCollapsedText,
                actionCollapsedText
            ) {
                text = getExpendedString()
                startAnimation(collapsedHeight, expandedHeight, true)
            }

            measureLayout()
            collapsedHeight = layout.height + paddingBottom + paddingTop

        } else text = completeText

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        movementMethod = LinkMovementMethod.getInstance()
    }

    private fun getCollapseSpan(): SpannableString {
        if (collapsedTextSpanned == null) collapsedTextSpanned = getSpan(
            completeText.substring(0, truncatedLength) + actionCollapsedText, actionCollapsedText
        ) {
            text = getExpendedString()
            startAnimation(collapsedHeight, expandedHeight, true)
        }
        return collapsedTextSpanned!!
    }

    private fun getExpendedString(): SpannableString {
        if (expandedTextSpanned == null) expandedTextSpanned = getSpan(
            completeText + actionExpendedText, actionExpendedText
        ) {
            startAnimation(expandedHeight, collapsedHeight, false)
        }
        return expandedTextSpanned!!
    }

    private var completeText: String = ""
    private var truncatedLength: Int = 0
    private var actionCollapsedText: String = "View More"
    private var actionExpendedText: String = "View Less"
    private var actionTextColor: Int = Color.RED
    private var collapseDuration: Int = 0
    private var expandDuration: Int = 0

    private fun getSpan(
        string: String, spanString: String, clickAction: () -> Unit
    ): SpannableString {
        return SpannableString(string).apply {

            val clickSpan = object : ClickableSpan() {

                override fun onClick(widget: View) {
                    clickAction()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = actionTextColor
                    ds.isUnderlineText = true
                }

            }

            setSpan(
                clickSpan,
                (string.length - spanString.length),
                string.length,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }


    private fun startAnimation(from: Int, to: Int, toExpand: Boolean) {

        ValueAnimator.ofInt(from, to).apply {
            duration = expandDuration.toLong()
            addUpdateListener {
                val lp = layoutParams
                lp.height = animatedValue as Int
                layoutParams = lp
            }
            start()
            if (!toExpand)
                doOnEnd {
                    text = getCollapseSpan()
                }
        }
    }

    private fun View.measureLayout() {
        val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        measure(widthSpec, heightSpec)
    }

    companion object {
        private const val TAG = "ExpendableTextView"
    }

}