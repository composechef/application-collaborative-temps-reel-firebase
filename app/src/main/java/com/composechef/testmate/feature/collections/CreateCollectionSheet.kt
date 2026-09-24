package com.composechef.testmate.feature.collections

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.composechef.testmate.R
import com.composechef.testmate.core.designsystem.components.WorkspaceSheet
import com.composechef.testmate.core.designsystem.theme.CollectionAccents
import com.composechef.testmate.core.designsystem.theme.CollectionColors

@Composable
fun CreateCollectionSheet(onDismiss: () -> Unit, onCreate: (String, String, Int, String) -> Unit) {
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var color by rememberSaveable { mutableIntStateOf(0) }
    val colors =
        listOf(
            stringResource(R.string.color_indigo),
            stringResource(R.string.color_green),
            stringResource(R.string.color_amber),
            stringResource(R.string.color_rose),
        )
    WorkspaceSheet(onDismiss) {
        Text(
            stringResource(R.string.new_collection),
            style = MaterialTheme.typography.headlineMedium,
        )
        OutlinedTextField(
            name,
            { name = it.take(60) },
            Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.collection_name)) },
            singleLine = true,
            placeholder = { Text(stringResource(R.string.collection_placeholder)) },
        )
        OutlinedTextField(
            description,
            { description = it.take(180) },
            Modifier.fillMaxWidth(),
            label = { Text(stringResource(R.string.description_optional)) },
            maxLines = 3,
        )
        Text(stringResource(R.string.color), style = MaterialTheme.typography.labelLarge)
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            colors.forEachIndexed { index, label ->
                Surface(
                    modifier =
                        Modifier.size(48.dp)
                            .semantics {
                                contentDescription = label
                                selected = color == index
                            }
                            .selectable(
                                selected = color == index,
                                role = Role.RadioButton,
                                onClick = { color = index },
                            ),
                    shape = CircleShape,
                    color = CollectionColors[index],
                    border =
                        if (color == index) BorderStroke(2.dp, CollectionAccents[index]) else null,
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (color == index)
                            Icon(
                                Icons.Outlined.Check,
                                contentDescription = null,
                                tint = CollectionAccents[index],
                            )
                    }
                }
            }
        }
        Button(
            onClick = { onCreate(name, description, color, "") },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(52.dp),
        ) {
            Text(stringResource(R.string.create_collection))
        }
    }
}
