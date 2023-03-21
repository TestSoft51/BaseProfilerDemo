package com.example.benchmark

import androidx.benchmark.macro.*
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startupCompilationModeNone() = startup(CompilationMode.None())

    @Test
    fun startupCompilationModePartial() = startup(CompilationMode.Partial())

    @Test
    fun scrollListCompilationModeNone() = scrollListPerformance(CompilationMode.None())

    @Test
    fun scrollListCompilationModePartial() = scrollListPerformance(CompilationMode.Partial())

    private fun startup(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.example.benchmarkdemo",
        metrics = listOf(StartupTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = mode
    ) {
        pressHome()
        startActivityAndWait()
    }

    private fun scrollListPerformance(mode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.example.benchmarkdemo",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.COLD,
        compilationMode = mode
    ){
        pressHome()
        startActivityAndWait()

        addItemAndScrollList()
    }
}

fun MacrobenchmarkScope.addItemAndScrollList() {
    val button = device.findObject(By.text("Add"))
    val list = device.findObject(By.res("list"))

    repeat(30) {
        button.click()
    }

    device.waitForIdle()

    list.setGestureMargin(device.displayWidth / 5)
    list.fling(Direction.DOWN)

    device.waitForIdle()
}
