package com.composechef.taskmate.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composechef.taskmate.core.designsystem.theme.ListAccents
import com.composechef.taskmate.core.designsystem.theme.ListColors
import com.composechef.taskmate.core.model.Member

@Composable
fun Avatar(member: Member, modifier: Modifier = Modifier, index: Int = 0) {
    Box(
        modifier = modifier.size(32.dp).clip(CircleShape).background(ListColors[index % ListColors.size]),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = member.initials,
            color = ListAccents[index % ListAccents.size],
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
