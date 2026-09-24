package com.composechef.testmate.core.designsystem.components

import androidx.compose.animation.core.animate
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import kotlin.math.roundToInt

/** Left swipe reveals a button; deletion always requires a separate tap. */
@Composable
fun SwipeActions(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    onComplete: (() -> Unit)? = null,
    content: @Composable () -> Unit,
) {
    val deleteLabel = stringResource(R.string.delete)
    val completeLabel = stringResource(R.string.complete_task)
    val actionWidth = with(LocalDensity.current) { 96.dp.toPx() }
    var offset by remember { mutableFloatStateOf(0f) }
    val delete by rememberUpdatedState(onDelete)
    val complete by rememberUpdatedState(onComplete)
    Box(modifier.clip(RoundedCornerShape(18.dp))) {
        if (offset < 0f) {
            Box(Modifier.matchParentSize().background(MaterialTheme.colorScheme.errorContainer)) {
                TextButton(
                    onClick = {
                        offset = 0f
                        delete()
                    },
                    modifier = Modifier.align(Alignment.CenterEnd).width(96.dp).fillMaxHeight(),
                    colors =
                        ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Outlined.Delete, contentDescription = null)
                        Text(deleteLabel)
                    }
                }
            }
        } else if (offset > 0f) {
            Box(
                Modifier.matchParentSize().background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.CenterStart,
            ) {
                Icon(
                    Icons.Outlined.Check,
                    completeLabel,
                    Modifier.padding(start = 28.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Box(
            Modifier.offset { IntOffset(offset.roundToInt(), 0) }
                .semantics {
                    customActions = buildList {
                        add(
                            CustomAccessibilityAction(deleteLabel) {
                                offset = 0f
                                delete()
                                true
                            }
                        )
                        if (complete != null)
                            add(
                                CustomAccessibilityAction(completeLabel) {
                                    complete?.invoke()
                                    true
                                }
                            )
                    }
                }
                .draggable(
                    state =
                        rememberDraggableState { delta ->
                            offset =
                                (offset + delta).coerceIn(
                                    -actionWidth,
                                    if (complete != null) actionWidth else 0f,
                                )
                        },
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (offset >= actionWidth * .8f && complete != null) {
                            offset = 0f
                            complete?.invoke()
                        } else {
                            val target = if (offset < -actionWidth * .5f) -actionWidth else 0f
                            animate(offset, target) { value, _ -> offset = value }
                        }
                    },
                )
        ) {
            content()
        }
    }
}
