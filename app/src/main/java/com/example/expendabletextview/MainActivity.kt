package com.example.expendabletextview

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.appcompat.app.AppCompatActivity
import com.example.expendabletextview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.tvText.animation = AnimationUtils.loadAnimation(
            this,
            R.anim.graph_point_animation
        )
//        animate()


    }


    private fun animationStart() {
        ScaleAnimation(0.5F, 1F, 0.5F, 1F).apply {
            duration = 500
            fillAfter = true
            binding.tvText.animation = this
        }
    }

    private fun animate() {
        ValueAnimator.ofInt(40, 40 * 3).apply {

            interpolator = AccelerateInterpolator()
            duration = 1500
//            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE

            addUpdateListener {
                val value = animatedValue as Int

                val params = binding.tvText.layoutParams
                params.width = value
                params.height = value
                binding.tvText.layoutParams = params


//                Log.d("animate", "animate: ${value}")
//                binding.tvText.alpha = ((value - 40)/80).toFloat()
            }

            start()
        }
    }

}