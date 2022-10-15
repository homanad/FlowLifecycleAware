package com.homanad.android.sample.flowlifecycleaware.screens.third

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.homanad.android.sample.flowlifecycleaware.databinding.ActivityThirdBinding
import com.homanad.android.sample.flowlifecycleaware.screens.third.vm.ThirdViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ThirdActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThirdBinding
    private val viewModel: ThirdViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThirdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            viewModel.stateFlow1
                .combine(viewModel.stateFlow2) { d1, d2 ->
                    d1 + d2
                }
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    Log.d("ThirdTest1", "value: $it")
                    binding.run {
                        text1.text = "${text1.text} $it"
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.stateFlow1
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .combine(viewModel.stateFlow2) { d1, d2 ->
                    d1 + d2
                }
                .collect {
                    Log.d("ThirdTest2", "value: $it")
                    binding.run {
                        text2.text = "${text2.text} $it"
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.stateFlow1
                .map { it + 1 }
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    Log.d("ThirdTest3", "value: $it")
                    binding.run {
                        text3.text = "${text3.text} $it"
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.stateFlow1
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .map { it + 1 }
                .collect {
                    Log.d("ThirdTest4", "value: $it")
                    binding.run {
                        text4.text = "${text4.text} $it"
                    }
                }
        }

        viewModel.startEmitting()
    }
}