package com.homanad.android.sample.flowlifecycleaware.screens.second

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.homanad.android.sample.flowlifecycleaware.databinding.ActivitySecondBinding
import com.homanad.android.sample.flowlifecycleaware.screens.second.vm.SecondViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SecondActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySecondBinding
    private val viewModel: SecondViewModel by viewModels()

    private lateinit var job1: Job
    private lateinit var job2: Job
    private lateinit var job3: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            job1 = launch {
                viewModel.stateFlow.collect {
                    binding.run {
                        text1.text = "${text1.text} $it"
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                job2 = launch {
                    viewModel.stateFlow.collect {
                        binding.run {
                            text2.text = "${text2.text} $it"
                        }
                    }
                }
            }
        }

        job3 = lifecycleScope.launch {
            viewModel.stateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                binding.run {
                    text3.text = "${text3.text} $it"
                }
            }
        }

        viewModel.startEmitting()
    }

    override fun onStop() {
        super.onStop()
        Log.d("SecondTest.onStop", "is job1 active: ${job1.hashCode()} - ${job1.isActive}")
        Log.d("SecondTest.onStop", "is job1 canceled: ${job1.hashCode()} - ${job1.isCancelled}")

        Log.d("SecondTest.onStop", "is job2 active: ${job2.hashCode()} - ${job2.isActive}")
        Log.d("SecondTest.onStop", "is job2 canceled: ${job2.hashCode()} - ${job2.isCancelled}")

        Log.d("SecondTest.onStop", "is job3 active: ${job3.hashCode()} - ${job3.isActive}")
        Log.d("SecondTest.onStop", "is job3 canceled: ${job3.hashCode()} - ${job3.isCancelled}")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("SecondTest.onDestroy", "is job1 active: ${job1.hashCode()} - ${job1.isActive}")
        Log.d("SecondTest.onDestroy", "is job1 canceled: ${job1.hashCode()} - ${job1.isCancelled}")

        Log.d("SecondTest.onDestroy", "is job2 active: ${job2.hashCode()} - ${job2.isActive}")
        Log.d("SecondTest.onDestroy", "is job2 canceled: ${job2.hashCode()} - ${job2.isCancelled}")

        Log.d("SecondTest.onDestroy", "is job3 active: ${job3.hashCode()} - ${job3.isActive}")
        Log.d("SecondTest.onDestroy", "is job3 canceled: ${job3.hashCode()} - ${job3.isCancelled}")
    }
}