package com.example.todo.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todo.databinding.ItemOnboardingPageBinding

class OnboardingPageFragment : Fragment() {

    private var _binding: ItemOnboardingPageBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_IMAGE = "arg_image"
        private const val ARG_TEXT = "arg_text"

        fun newInstance(page: OnboardingPage): OnboardingPageFragment {
            return OnboardingPageFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_IMAGE, page.imageRes)
                    putInt(ARG_TEXT, page.textRes)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ItemOnboardingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            binding.pageImage.setImageResource(it.getInt(ARG_IMAGE))
            binding.pageText.setText(it.getInt(ARG_TEXT))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}