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

## First test

* This is my
  stateFlow: <a href="https://github.com/homanad/FlowLifecycleAware/blob/master/app/src/main/java/com/homanad/android/sample/flowlifecycleaware/screens/first/vm/FirstViewModel.kt" target="_blank">
  FirstViewModel</a>
* And these are my
  collectors: <a href="https://github.com/homanad/FlowLifecycleAware/blob/master/app/src/main/java/com/homanad/android/sample/flowlifecycleaware/screens/first/FirstActivity.kt" target="_blank">
  FirstActivity</a>
* And let's see what happens if I am trying to make the app goes to background!

<img src="/attachments/behavior.gif"/>

* Now we will start dissecting the behavior of each collector!

    - With `lifecycleScope`:

        - **_Starting point_**: it starts collecting right after calling, so we can see the 0 value
          is printed first.
        - **_Activity in background_**: and when the activity goes to `onPause` and `onStop`, it
          still collects value, so at the end we can see it shows all value from 0 to 10.
    - With `launchWhenCreated`:

        - **_Starting point_**: it starts collecting right after the activity is created (after
          `onCreate` and before `onStart`), so it can't collect value 0, instead the first value is
            1.
        - **_Activity in background_**: the same as `lifecycleScope`, it still collects value, and
          at the end we can see from 1 to 10.
    - With `launchWhenStarted`:

        - **_Starting point_**: it starts collecting right after the activity is started (after
          `onStart` and before `onResume`), so it also can't collect value 0, and 1 is the first
          value.
        - **_Activity in background_**: it will be suspended when the activity is in background, and
          when we back to foreground, it continue to collect value. As its documentation, I also
          tried to print log for each lifecycle function, I can see that it will be suspended before
          `onPause` is called.
    - With `launchWhenResumed`:

        - **_Starting point_**: it starts collecting right after the activity is resumed (and it
          only collect value when the activity is in resume state), its result the same as
          `launchWhenStarted`, but the difference is it starts collect after `onResume` is called.
        - **_Activity in background_**: the same as `launchWhenStarted`, it will be suspended before
          `onPause` is called.
    - With `repeatOnLifecycle - CREATED`:

        - **_Starting point_**: the same as `launchWhenCreated`, it starts collecting right after
          onCreate is called.
        - **_Activity in background_**:  still collecting value even the activity is in background.
    - With `repeatOnLifecycle - STARTED`:

        - **_Starting point_**: the same as `launchWhenStarted`, it starts collecting right after
          onStart is called.
        - **_Activity in background_**: don't collect value (suspended) when activity is in
          background.
    - With `repeatOnLifecycle - RESUMED`:

        - **_Starting point_**: the same as `launchWhenResumed`, it starts collecting right after
          onResume is called and only in resume state.
        - **_Activity in background_**: don't collect value (suspended) when activity is in
          background.
    - With `flowWithLifecycle - CREATED`: The same behavior as `launchWhenCreated`
      and `repeatOnLifecycle - CREATED`
    - With `flowWithLifecycle - STARTED`: The same behavior as `launchWhenStarted`
      and `repeatOnLifecycle - STARTED`
    - With `flowWithLifecycle - RESUMED`:The same behavior as `launchWhenResumed`
      and `repeatOnLifecycle - RESUMED`
* After above tests, we can separate it to x types:

    * `lifecycleScope.launch` -> start collecting right after creating and only canceling when the
      lifecycle owner is destroyed.
    * `launchWhenCreated`, `repeatOnLifecycle.CREATED`, `flowWithLifeCycle.CREATED` have the same
      behavior on above test.
    * `launchWhenCreated`, `repeatOnLifecycle.STARTED`, `flowWithLifeCycle.STARTED` have the same
      behavior on above test.
    * `launchWhenCreated`, `repeatOnLifecycle.RESUMED`, `flowWithLifeCycle.RESUMED` have the same
      behavior on above test.
* So we will not talk about `lifeCycleScope.launch` here, instead we will clarify why we have 3
  options with the same behavior, and if they are really the same, let's go to the second test!

## Second test

- Take a look at my
  stateFlow: <a href="https://github.com/homanad/FlowLifecycleAware/blob/master/app/src/main/java/com/homanad/android/sample/flowlifecycleaware/screens/second/vm/SecondViewModel.kt" target="_blank">
  SecondViewModel</a>

* Take a look at my
  activity: <a href="https://github.com/homanad/FlowLifecycleAware/blob/master/app/src/main/java/com/homanad/android/sample/flowlifecycleaware/screens/second/SecondActivity.kt" target="_blank">
  SecondActivity</a>
* As I said at the First test, on the surface, it's clear that all three methods give the same
  result, but is it really the same in practice? To put it mildly, no one would spare to create 3
  different things for the same result, right? As I mentioned, repeatOnLifeCycle was created to
  replace launchWhenX, because in fact launchWhenX will still create a waste of resources when the
  lifecycle owner is inactive, and flowWithLifecycle is also not the same as repeatOnLifeCycle, it
  is an operator. And the main difference is:

    * `launchWhenX`: it suspends only when the lifecycle is out of scope (
      depending on Created, Started or Resumed), and it simply suspends and the coroutine remains
      active, it will continue when the lifecycle's state is in scope function operation.
    * `repeatOnLifeCycle`: it doesn't suspend, but cancels the coroutine when the lifecycle goes out
      of scope. That means, each time the lifecycle comes back into scope, it creates a new
      coroutine and runs it.
    * `flowWithLifecycle`: it's an operator, and the order in which the operator is placed will also
      affect the collector's behavior, but we're not going to talk about operators for the time
      being in this article.

* For better understanding, see the execution result:

  <img src="/attachments/second_test_log.png"/>

  ```
  SecondTest.onStop        D  is job1 active: 109145133 - true
  SecondTest.onStop        D  is job1 canceled: 109145133 - false
  SecondTest.onStop        D  is job2 active: 199161186 - false
  SecondTest.onStop        D  is job2 canceled: 199161186 - true
  SecondTest.onStop        D  is job3 active: 142509811 - true
  SecondTest.onStop        D  is job3 canceled: 142509811 - false
  SecondTest.onStop        D  is job1 active: 109145133 - true
  SecondTest.onStop        D  is job1 canceled: 109145133 - false
  SecondTest.onStop        D  is job2 active: 266476678 - false
  SecondTest.onStop        D  is job2 canceled: 266476678 - true
  SecondTest.onStop        D  is job3 active: 142509811 - true
  SecondTest.onStop        D  is job3 canceled: 142509811 - false
  SecondTest.onStop        D  is job1 active: 109145133 - true
  SecondTest.onStop        D  is job1 canceled: 109145133 - false
  SecondTest.onStop        D  is job2 active: 51376120 - false
  SecondTest.onStop        D  is job2 canceled: 51376120 - true
  SecondTest.onStop        D  is job3 active: 142509811 - true
  SecondTest.onStop        D  is job3 canceled: 142509811 - false
  SecondTest.onDestroy     D  is job1 active: 109145133 - false
  SecondTest.onDestroy     D  is job1 canceled: 109145133 - true
  SecondTest.onDestroy     D  is job2 active: 51376120 - false
  SecondTest.onDestroy     D  is job2 canceled: 51376120 - true
  SecondTest.onDestroy     D  is job3 active: 142509811 - false
  SecondTest.onDestroy     D  is job3 canceled: 142509811 - false
  ```

    * The results show that:
        * _job1_ and _job3_ only **cancel** and **stay active** when `onStop` is called
        * _job2_ is **canceled** and **no longer active** when `onStop` is called
        * _job1_ will be **canceled** when `onDestroy` is called
        * _job3_ just **no longer active** but **still not canceled** when `onDestroy` is called
        * _job1_ and _job3_ are always **1 instance** even after 2 times `onPause` is called
        * _job2_ always **creates a new instance** after every `onStart` is called (
          repeatOnLifeCycle - STARTED)

    * This it can be deduced that:
        * `launchWhenX` and `flowWithLifecycle` will not automatically cancel when lifecycle goes
          out of scope
        * `repeatOnLifecycle` always cancels and starts a new coroutine.

    * And as one has already mentioned:
        * `launchWhenX` will be deprecated in the future, and actually we should use
          `repeatOnLifeCycle`.
        * `flowWithLifecycle` was introduced with `repeatOnLifeCycle`, it is not the default way
          when we start collecting data, but it is an operator, it will be used in a sequence of
          operators to perform the collection. It can be used in some cases, and ordering the
          operators will also affect the collector's behavior. So let's take a third test
          with `flowWithLifecycle` to see in which cases it will be useful!

## Third test
      