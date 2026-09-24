package com.composechef.testmate.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.composechef.testmate.core.designsystem.theme.CollectionAccents
import com.composechef.testmate.core.designsystem.theme.CollectionColors
import com.composechef.testmate.core.model.Member

@Composable
fun Avatar(member: Member, modifier: Modifier = Modifier, index: Int = 0) {
    Box(
        modifier.size(32.dp).clip(CircleShape).background(CollectionColors[index % 4]),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            member.initials,
            color = CollectionAccents[index % 4],
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
