package com.leisherz.shoppinglist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import com.leisherz.shoppinglist.databinding.ActivitySplashBinding
import kotlin.concurrent.thread

class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        val splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_anim)
        binding.icon.startAnimation(splashAnim)
        thread {
            Thread.sleep(3000)
            runOnUiThread {
                val intent = Intent()
                intent.setClass(this, ScrollingActivity::class.java)
                startActivity(intent)
            }
        }
    }
}