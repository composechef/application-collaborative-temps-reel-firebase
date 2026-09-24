package com.composechef.taskmate.feature.lists.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composechef.taskmate.core.designsystem.components.Avatar
import com.composechef.taskmate.core.model.Member

/** Les avatars des premiers membres d'une liste, côte à côte. */
@Composable
fun MemberAvatars(members: List<Member>, modifier: Modifier = Modifier, maxVisible: Int = 3) {
    Row(modifier = modifier) {
        members.take(maxVisible).forEachIndexed { index, member ->
            Avatar(member = member, modifier = Modifier.padding(end = 4.dp), index = index)
        }
    }
}
