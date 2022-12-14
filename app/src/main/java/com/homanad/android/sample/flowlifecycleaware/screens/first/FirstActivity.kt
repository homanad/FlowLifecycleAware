package com.homanad.android.sample.flowlifecycleaware.screens.first

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.homanad.android.sample.flowlifecycleaware.MainActivity
import com.homanad.android.sample.flowlifecycleaware.databinding.ActivityFirstBinding
import com.homanad.android.sample.flowlifecycleaware.screens.first.vm.FirstViewModel
import kotlinx.coroutines.launch

class FirstActivity : AppCompatActivity() {

    companion object {
        private val TAG = FirstActivity::class.java.simpleName
    }

    private lateinit var binding: ActivityFirstBinding
    private val viewModel: FirstViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        lifecycleScope.launch {
//            viewModel.stateFlow.collect {
//                Log.d(TAG, "lifecycleScope, value: $it")
//                binding.run {
//                    text1.text = "${text1.text} $it"
//                }
//            }
//        }
//
//        lifecycleScope.launchWhenCreated {
//            viewModel.stateFlow.collect {
//                Log.d(TAG, "launchWhenCreated, value: $it")
//                binding.run {
//                    text2.text = "${text2.text} $it"
//                }
//            }
//        }

//        lifecycleScope.launchWhenStarted {
//            viewModel.stateFlow.collect {
//                Log.d(TAG, "launchWhenStarted, value: $it")
//                binding.run {
//                    text3.text = "${text3.text} $it"
//                }
//            }
//        }

        lifecycleScope.launchWhenResumed {
            viewModel.stateFlow.collect {
                Log.d(TAG, "launchWhenResumed, value: $it")
                binding.run {
                    text4.text = "${text4.text} $it"
                }
            }
        }
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.CREATED) {
//                viewModel.stateFlow.collect {
//                    Log.d(TAG, "repeatOnLifecycle - CREATED, value: $it")
//                    binding.run {
//                        text5.text = "${text5.text} $it"
//                    }
//                }
//            }
//        }
//
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.stateFlow.collect {
//                    Log.d(TAG, "repeatOnLifecycle - STARTED, value: $it")
//                    binding.run {
//                        text6.text = "${text6.text} $it"
//                    }
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                viewModel.stateFlow.collect {
//                    Log.d(TAG, "repeatOnLifecycle - RESUMED, value: $it")
//                    binding.run {
//                        text7.text = "${text7.text} $it"
//                    }
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.stateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.CREATED).collect {
//                Log.d(TAG, "flowWithLifecycle - CREATED, value: $it")
//                binding.run {
//                    text8.text = "${text8.text} $it"
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.stateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
//                Log.d(TAG, "flowWithLifecycle - STARTED, value: $it")
//                binding.run {
//                    text9.text = "${text9.text} $it"
//                }
//            }
//        }
//
//        lifecycleScope.launch {
//            viewModel.stateFlow.flowWithLifecycle(lifecycle, Lifecycle.State.RESUMED).collect {
//                Log.d(TAG, "flowWithLifecycle - RESUMED, value: $it")
//                binding.run {
//                    text10.text = "${text10.text} $it"
//                }
//            }
//        }

        viewModel.startEmitting()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}