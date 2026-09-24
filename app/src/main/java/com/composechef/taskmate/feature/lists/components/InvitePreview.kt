package com.composechef.taskmate.feature.lists.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.core.designsystem.theme.TaskMateTheme
import com.composechef.taskmate.core.designsystem.theme.listColor
import com.composechef.taskmate.core.model.ListInvite
import com.composechef.taskmate.core.preview.PreviewData

/** L'aperçu d'une liste trouvée grâce à un code d'invitation. */
@Composable
fun InvitePreview(invite: ListInvite, modifier: Modifier = Modifier) {
    val colors = listColor(invite.color)
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = colors.container,
        shape = MaterialTheme.shapes.medium,
    ) {
        Text(
            text = invite.name,
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.titleMedium,
            color = colors.onContainer,
        )
    }
}

@Preview
@Composable
private fun InvitePreviewPreview() {
    TaskMateTheme {
        InvitePreview(invite = PreviewData.invite)
    }
}
