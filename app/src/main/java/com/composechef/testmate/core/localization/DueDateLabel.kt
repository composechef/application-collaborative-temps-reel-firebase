package com.composechef.testmate.core.localization

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.composechef.testmate.R
import com.composechef.testmate.core.model.DueDate

@Composable
fun DueDate.label(): String =
    stringResource(
        when (this) {
            DueDate.None -> R.string.no_date
            DueDate.Today -> R.string.today
            DueDate.Tomorrow -> R.string.tomorrow
            DueDate.ThisWeek -> R.string.this_week
        }
    )
