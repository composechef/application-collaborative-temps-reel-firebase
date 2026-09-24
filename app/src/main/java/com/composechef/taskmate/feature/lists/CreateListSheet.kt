package com.composechef.taskmate.feature.lists

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.components.TaskMateSheet
import com.composechef.taskmate.core.designsystem.theme.ListAccents
import com.composechef.taskmate.core.designsystem.theme.ListColors

@Composable
fun CreateListSheet(
    onDismiss: () -> Unit,
    onCreate: (name: String, description: String, color: Int) -> Unit,
) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var color by rememberSaveable { mutableIntStateOf(0) }
    val colorLabels =
        listOf(
            stringResource(R.string.color_indigo),
            stringResource(R.string.color_green),
            stringResource(R.string.color_amber),
            stringResource(R.string.color_rose),
        )

    TaskMateSheet(onDismiss = onDismiss) {
        Text(text = stringResource(R.string.new_list), style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(
            value = name,
            onValueChange = { name = it.take(60) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.list_name)) },
            placeholder = { Text(text = stringResource(R.string.list_placeholder)) },
            singleLine = true,
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it.take(180) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.description_optional)) },
            maxLines = 3,
        )
        Text(text = stringResource(R.string.color), style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            colorLabels.forEachIndexed { index, label ->
                val isSelected = color == index
                Surface(
                    modifier =
                        Modifier.size(48.dp)
                            .semantics {
                                contentDescription = label
                                selected = isSelected
                            }
                            .selectable(
                                selected = isSelected,
                                role = Role.RadioButton,
                                onClick = { color = index },
                            ),
                    shape = CircleShape,
                    color = ListColors[index],
                    border = if (isSelected) BorderStroke(2.dp, ListAccents[index]) else null,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = ListAccents[index],
                            )
                        }
                    }
                }
            }
        }
        Button(
            onClick = { onCreate(name.trim(), description.trim(), color) },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text(text = stringResource(R.string.create_list))
        }
    }
}
