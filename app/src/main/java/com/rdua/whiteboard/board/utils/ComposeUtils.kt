package com.rdua.whiteboard.board.utils

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.platform.ViewConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.rdua.whiteboard.board.model.ShapeType

/**
 * Replaces current state of this SnapshotStateList with elements from [newList] effectively triggering recomposition.
 *
 * @param newList provides values to replace currently present in this SnapshotStateList.
 */
fun <T> SnapshotStateList<T>.swapList(newList: List<T>) {
    clear()
    addAll(newList)
}

/**
 * Consumes all pointer events. You can use it to "prevent" event propagation from child to parent Composable.
 */
fun Modifier.consumeAllPointerEvents(): Modifier =
    pointerInput(Unit) {
        awaitEachGesture {
            while (true) {
                val event = awaitPointerEvent()
                event.changes.forEach { pointerInputChange: PointerInputChange ->
                    pointerInputChange.consume()
                }
            }
        }
    }

/**
 * Calls action lambda when user closes EMI keyboard. Uses WindowInsets to check for closed EMI keyboard, which requires disabled decorFitsSystemWindows in WindowCompat for the application.
 */
fun Modifier.onKeyboardDismiss(action: () -> Unit): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                action()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}

/**
 * Allows to provide different modifiers based on the result of the given [condition].
 *
 * @param condition a boolean value, that determines the resulting modifier.
 * @param ifTrue provides modifier that will be returned if the result of the [condition] is true.
 * @param ifFalse provides modifier that will be returned if the result of the [condition] is false.
 */
fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null,
): Modifier {
    return if (condition) {
        then(ifTrue(Modifier))
    } else if (ifFalse != null) {
        then(ifFalse(Modifier))
    } else {
        this
    }
}

/**
 * Alternative version to Modifier.onSizeChanged, that calls [onSizeChanged] lambda only when the size of the Composable has actually changed. The original Modifier.onSizeChanged would call its lambda during every recomposition.
 */
fun Modifier.onSizeActuallyChanged(onSizeChanged: (IntSize) -> Unit): Modifier = composed {
    var size by remember { mutableStateOf<IntSize?>(null) }

    onSizeChanged {
        if (it != size) {
            if (size != null) onSizeChanged(it)
            size = it
        }
    }
}

fun getShapeContentAlignment(type: ShapeType): Alignment =
    when (type) {
        ShapeType.RECTANGLE, ShapeType.OVAL, ShapeType.STAR, ShapeType.ROUNDED_RECTANGLE,
        ShapeType.RHOMBUS, ShapeType.PARALLELOGRAM, ShapeType.HEXAGON,
        ShapeType.LINE, ShapeType.RIGHT_ARROW, ShapeType.LEFT_ARROW,
        -> Alignment.Center

        ShapeType.TRIANGLE, ShapeType.PENTAGON -> Alignment.BottomCenter
    }

/**
 * Provides a mean to modify minimum touch target size for Composables.
 *
 * By default MaterialDesign 3 specifies a 48.dp minimum touch size for any Composable for increasing usability. This function will override this touch size for all the Composables in a given [content].
 */
@Composable
fun UpdateViewConfiguration(
    minimumTouchTargetSize: Dp = 48.dp,
    content: @Composable () -> Unit,
) {
    fun ViewConfiguration.updateViewConfiguration() = object : ViewConfiguration {
        override val doubleTapMinTimeMillis: Long
            get() = this@updateViewConfiguration.doubleTapMinTimeMillis

        override val doubleTapTimeoutMillis: Long
            get() = this@updateViewConfiguration.doubleTapTimeoutMillis

        override val longPressTimeoutMillis: Long
            get() = this@updateViewConfiguration.longPressTimeoutMillis

        override val touchSlop: Float
            get() = this@updateViewConfiguration.touchSlop

        override val minimumTouchTargetSize: DpSize
            get() = DpSize(minimumTouchTargetSize, minimumTouchTargetSize)
    }

    CompositionLocalProvider(
        LocalViewConfiguration provides LocalViewConfiguration.current.updateViewConfiguration()
    ) {
        content()
    }
}