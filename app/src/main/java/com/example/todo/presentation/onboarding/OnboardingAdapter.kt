package com.example.todo.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.todo.R

class OnboardingAdapter(fragment: OnboardingContainerFragment) : FragmentStateAdapter(fragment) {

    private val pages = listOf(
        OnboardingPage(R.drawable.service1book, R.string.service_page1),
        OnboardingPage(R.drawable.servicebook2, R.string.service_page2),
        OnboardingPage(R.drawable.service3book, R.string.service_page3)
    )

    override fun getItemCount() = pages.size + 1

    override fun createFragment(position: Int): Fragment {
        return if (position < pages.size) {
            OnboardingPageFragment.newInstance(pages[position])
        } else {
            Onboarding4Fragment()
        }
    }
}