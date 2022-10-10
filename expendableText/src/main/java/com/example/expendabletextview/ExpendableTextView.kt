package com.example.expendabletextview

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class ExpendableTextView : TextView {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : this(
        context, attributeSet, defStyle, 0
    )

    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        defStyle: Int,
        defStyleRes: Int
    ) : super(context, attributeSet, defStyle, defStyleRes) {
        context.obtainStyledAttributes(attributeSet, R.styleable.ExpendableTextView).apply {

            truncatedLength = getInteger(R.styleable.ExpendableTextView_truncationLength, 60)
            actionExpendedText =
                getString(R.styleable.ExpendableTextView_actionExpendedText) ?: "View Less"
            actionCollapsedText =
                getString(R.styleable.ExpendableTextView_actionCollapsedText) ?: "View More"
            completeText = getString(R.styleable.ExpendableTextView_completeText) ?: ""

            actionTextColor = getColor(R.styleable.ExpendableTextView_actionTextColor, Color.RED)

            expandDuration = getInteger(R.styleable.ExpendableTextView_collapseDuration, 600)
            collapseDuration = getInteger(R.styleable.ExpendableTextView_collapseDuration, 600)

            recycle()
        }
        init()
    }


    private var expandedHeight: Int = 0
    private var collapsedHeight: Int = 0


    private fun init() {

        Log.d(TAG, "init: Expended $expandedHeight, Collapsed $collapsedHeight")
    }


    private fun onCompleteText(){
        text = "$completeText $actionCollapsedText"
        measureLayout()

        expandedHeight = layout.height + paddingBottom + paddingTop

        setText(if (completeText.length > truncatedLength) {

            getSpan(
                completeText.substring(0, truncatedLength) + actionCollapsedText,
                actionCollapsedText
            ) {
                startAnimation(collapsedHeight, expandedHeight, true)
            }

        } else completeText
        )

        measureLayout()

        collapsedHeight = layout.height + paddingBottom + paddingTop
    }
    private var expendAnimator: ValueAnimator? = null
    private var collapseAnimator: ValueAnimator? = null


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

    private var _collapseDuration: Int = 0
    var collapseDuration
        get() = _collapseDuration
        set(value) {
            _collapseDuration = value
        }

    private var _expandDuration: Int = 0
    var expandDuration
        get() = _expandDuration
        set(value) {
            _expandDuration = value
        }

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
        if (toExpand) {

            if (expendAnimator == null) expendAnimator = ValueAnimator.ofInt(from, to).apply {
                interpolator = LinearInterpolator()
                duration = expandDuration.toLong()
                addUpdateListener {
                    layoutParams.height = animatedValue as Int
                }
                addListener(expendAnimationListener)
            }

            val spannedText = getSpan(
                completeText.substring(0, truncatedLength) + actionCollapsedText,
                actionCollapsedText
            ) {
                startAnimation(expandedHeight, collapsedHeight, false)
            }

            text = spannedText

            if ((expendAnimator?.isRunning != true)) expendAnimator?.start()

        } else {

            if (collapseAnimator == null) collapseAnimator = ValueAnimator.ofInt(from, to).apply {
                duration = expandDuration.toLong()
                interpolator = LinearInterpolator()
                addUpdateListener {
                    layoutParams.height = animatedValue as Int
                }
                addListener(collapseAnimationListener)
            }

            if (collapseAnimator?.isRunning != true) collapseAnimator?.start()

        }
    }

    private fun View.measureLayout() {
        val widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        measure(widthSpec, heightSpec)
    }

    private val expendAnimationListener = object : Animator.AnimatorListener {

        override fun onAnimationStart(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    }

    private val collapseAnimationListener = object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {}

        override fun onAnimationEnd(animation: Animator?) {
            val spannedString = getSpan(
                completeText + actionExpendedText, actionExpendedText
            ) {
                startAnimation(collapsedHeight, expandedHeight, true)
            }
            text = spannedString
        }

        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    }

    companion object {
        private const val TAG = "ExpendableTextView"
    }

}