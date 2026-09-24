package com.composechef.taskmate.feature.lists.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.R
import com.composechef.taskmate.core.designsystem.theme.Ink
import com.composechef.taskmate.core.designsystem.theme.ListAccents
import com.composechef.taskmate.core.designsystem.theme.ListColors
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.core.model.SharedList
import com.composechef.taskmate.core.preview.PreviewData

@Composable
fun ListCard(list: SharedList, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colorIndex = list.color.mod(ListColors.size)
    Card(
        onClick = onClick,
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = ListColors[colorIndex]),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = list.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = Ink,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (list.description.isNotBlank()) {
                    Text(
                        text = list.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Ink.copy(alpha = .65f),
                    )
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                MemberAvatars(members = list.members)
                Text(
                    text =
                        pluralStringResource(
                            R.plurals.member_count,
                            list.members.size,
                            list.members.size,
                        ),
                    modifier = Modifier.padding(start = 8.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = ListAccents[colorIndex],
                )
            }
        }
    }
}

@Preview
@Composable
private fun ListCardPreview() {
    TaskMateTheme {
        ListCard(list = PreviewData.launch, onClick = {})
    }
}
