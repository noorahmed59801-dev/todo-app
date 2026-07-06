package com.example.todo.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.todo.R

class OnboardingContainerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private val totalPages = 4

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.onboarding, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.viewPager)
        dotsLayout = view.findViewById(R.id.dotsLayout)

        viewPager.adapter = OnboardingAdapter(this)

        setupDots(0)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupDots(position)
            }
        })
    }

    private fun setupDots(activeIndex: Int) {
        dotsLayout.removeAllViews()

        for (i in 0 until totalPages) {
            val dot = ImageView(requireContext())

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            dot.layoutParams = params

            dot.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (i == activeIndex)
                        R.drawable.lineindicatorwide
                    else
                        R.drawable.dash_inactive
                )
            )

            dotsLayout.addView(dot)
        }
    }
}