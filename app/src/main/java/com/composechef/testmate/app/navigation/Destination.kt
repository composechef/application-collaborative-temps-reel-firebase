package com.composechef.testmate.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.composechef.testmate.R

internal enum class Destination(val label: Int, val icon: ImageVector) {
    Collections(R.string.collections, Icons.Outlined.Dashboard),
    Tasks(R.string.my_tasks, Icons.Outlined.CheckCircle),
    Profile(R.string.profile, Icons.Outlined.Person),
}
