package com.yosefmoq.firstcode.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.yosefmoq.firstcode.R

class SplashFragment : Fragment() {
    private lateinit var mNavController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initItems();
        initClicks();
        Handler().postDelayed({
            mNavController.navigate(R.id.action_splashFragment_to_mainFragment)
        }, 3000)

    }

    private fun initClicks() {

    }

    private fun initItems() {
        mNavController = findNavController()
    }
}