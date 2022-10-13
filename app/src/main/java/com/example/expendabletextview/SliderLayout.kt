package com.example.expendabletextview

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout

class SideDrawerLayout : LinearLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyle: Int) : this(
        context,
        attributeSet,
        defStyle,
        0
    )

    constructor(
        context: Context,
        attributeSet: AttributeSet?,
        defStyle: Int,
        defStyleRes: Int
    ) : super(context, attributeSet, defStyle, defStyleRes) {
        init()
    }

    private var firstTime: Boolean = true

    private fun init() {

    }

    companion object {
        private const val TAG = "SideDrawerLayout"
    }

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        return super.drawChild(canvas, child, drawingTime)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (firstTime) {
            firstTime = false
            for (i in 0 until childCount) {
                val animation =
                    if (i < (childCount - 1))
                        AnimationUtils.loadAnimation(context, R.anim.enter_from_right)
                    else AnimationUtils.loadAnimation(context, R.anim.enter_from_bottom)
                animation.duration += ((i + 1) * 50).toLong()
                getChildAt(i)?.animation = animation
            }
        }
    }

}