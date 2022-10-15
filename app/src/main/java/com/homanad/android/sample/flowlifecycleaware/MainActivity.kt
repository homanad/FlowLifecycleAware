package com.homanad.android.sample.flowlifecycleaware

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.homanad.android.sample.flowlifecycleaware.databinding.ActivityMainBinding
import com.homanad.android.sample.flowlifecycleaware.screens.first.FirstActivity
import com.homanad.android.sample.flowlifecycleaware.screens.second.SecondActivity
import com.homanad.android.sample.flowlifecycleaware.screens.third.ThirdActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            buttonFirst.setOnClickListener {
                startActivity(Intent(this@MainActivity, FirstActivity::class.java))
            }

            buttonSecond.setOnClickListener {
                startActivity(Intent(this@MainActivity, SecondActivity::class.java))
            }

            buttonThird.setOnClickListener {
                startActivity(Intent(this@MainActivity, ThirdActivity::class.java))
            }
        }
    }
}