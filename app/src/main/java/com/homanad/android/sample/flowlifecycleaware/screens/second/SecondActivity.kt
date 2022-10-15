package com.homanad.android.sample.flowlifecycleaware.screens.second

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.homanad.android.sample.flowlifecycleaware.databinding.ActivitySecondBinding
import com.homanad.android.sample.flowlifecycleaware.screens.second.vm.SecondViewModel

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val viewModel: SecondViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}