package com.yosefmoq.firstcode.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.yosefmoq.firstcode.R

class MainActivity : AppCompatActivity() {

    private lateinit var mNavController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initItems();
        initClicks();
    }
    private fun initItems(){
        mNavController = findNavController(R.id.navHost)
    };
    private fun initClicks(){

    }

}