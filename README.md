# Start collector as an lifecycle-aware

This is the original article from [Manuel Vivo](https://medium.com/@manuelvicnt)
from **_
Medium_**: https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda

He mentioned `Lifecycle.repeatOnLifecycle` and `Flow.flowWithLifecycle`, and it should be used to
replace old collect flow ways like `CoroutineScope.launch`, `Flow.launchIn` or
even `LifeCycleCoroutineScope.launchWhenX`.

As we all know, Flow is not like LiveData, it does not automatically stop emitting data when the app
is in the background. Until the appearance of `LifeCycleCoroutineScope.launchWhenX`, the problem is
somewhat solved, but it's still not perfect, even though the collect is suspended while the app is
in the background, the flow producer still active and continues to emit the data even though no one
is collecting. Even using `lifecycleScope.launch` or `launchIn` is dangerous than when it continues
to collect data even though the app is in the background.

You may have thought about manually canceling the coroutine when the app goes into the background,
it would work fine, but in reality it will create quite a lot of boilerplate code, you will have to
write the same thing in many different places.

And that's why `Lifecycle.repeatOnLifecycle` and `flowWithLifecycle` were introduced
from `androidx.lifecycle:lifecycle-runtime-ktx:2.4.0` library or later.

For better understanding, read through the article by [Manuel Vivo](https://medium.com/@manuelvicnt)
, and here we will try above things to see what is the difference. ;)

* This is my
  stateFlow: <a href="https://github.com/homanad/FlowLifecycleAware/blob/master/app/src/main/java/com/homanad/android/sample/flowlifecycleaware/MainViewModel.kt" target="_blank">
  MainViewModel</a>
* And these are my
  collectors: <a href="https://github.com/homanad/FlowLifecycleAware/blob/master/app/src/main/java/com/homanad/android/sample/flowlifecycleaware/MainActivity.kt" target="_blank">
  MainActivity</a>

* And let's see what happens if I am trying to make the app goes to background!

<img src="/attachments/behavior.gif"/>

* Now we will start dissecting the behavior of each collector!

  