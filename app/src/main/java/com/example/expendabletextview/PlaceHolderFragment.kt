package com.example.expendabletextview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.expendabletextview.databinding.FragmentPlaceholderBinding

class PlaceHolderFragment : Fragment() {

    private var position: Int = 0

    private val binding: FragmentPlaceholderBinding by lazy {
        FragmentPlaceholderBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt("place", 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvText.text = "$position"
    }

    companion object {
        fun get(position: Int): PlaceHolderFragment {
            return PlaceHolderFragment().apply {
                arguments = Bundle().apply {
                    putInt("place", position)
                }
            }
        }
    }
}