package com.homanad.android.sample.flowlifecycleaware.screens.first.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FirstViewModel : ViewModel() {

    val stateFlow = MutableStateFlow(0)

    fun startEmitting() {
        viewModelScope.launch(Dispatchers.IO) {
            for (i in 1..10) {
                stateFlow.value = i
                delay(1000)
            }
        }
    }
}