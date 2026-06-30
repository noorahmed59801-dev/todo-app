package com.example.todo.presentation.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todo.R
import com.example.todo.util.PrefsConstants

class SplashFragment : Fragment() {

    companion object {
        private const val SPLASH_DELAY_MS = 2000L
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = requireContext()
            .getSharedPreferences(PrefsConstants.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(PrefsConstants.KEY_USER_NAME, "")

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isAdded) return@postDelayed
            if (name.isNullOrEmpty()) {
                findNavController().navigate(R.id.action_splash_to_onboarding)
            } else {
                findNavController().navigate(R.id.action_splash_to_home)
            }
        }, SPLASH_DELAY_MS)
    }
}