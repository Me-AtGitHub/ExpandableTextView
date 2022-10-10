package com.example.expendabletextview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.expendabletextview.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewPager.adapter = PagerAdapter(this)

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab, position->
            when(position){
                0-> tab.setIcon(R.drawable.home_selector)
                else -> tab.setIcon(R.drawable.favorite_selector)
            }
        }.attach()

    }

    class PagerAdapter(fragmentActivity: FragmentActivity) :
        FragmentStateAdapter(fragmentActivity) {
        override fun getItemCount() = 2
        override fun createFragment(position: Int) = PlaceHolderFragment.get(position)
    }

}