package com.composechef.taskmate.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.composechef.taskmate.R

/** Les trois onglets principaux de l'app, affichés par [HomeScaffold]. */
enum class HomeTab(@StringRes val label: Int, val icon: ImageVector) {
    Lists(R.string.lists, Icons.Outlined.Dashboard),
    MyTasks(R.string.my_tasks, Icons.Outlined.CheckCircle),
    Profile(R.string.profile, Icons.Outlined.Person),
}
