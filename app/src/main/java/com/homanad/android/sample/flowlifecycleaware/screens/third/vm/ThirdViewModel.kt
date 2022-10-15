package com.homanad.android.sample.flowlifecycleaware.screens.third.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ThirdViewModel : ViewModel() {

    private val values1 = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    private val values2 = listOf(10, 9, 8, 7, 6, 5, 4, 3, 2, 1)

    val stateFlow1 = MutableStateFlow(0)
    val stateFlow2 = MutableStateFlow(0)

    fun startEmitting() {
        viewModelScope.launch(Dispatchers.IO) {
            values1.forEach {
                stateFlow1.value = it
                delay(1000)
            }
            values2.forEach {
                stateFlow2.value = it
                delay(1000)
            }
        }
    }
}