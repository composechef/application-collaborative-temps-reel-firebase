package com.composechef.testmate.feature.collections

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.composechef.testmate.core.designsystem.theme.TaskMateTheme
import com.composechef.testmate.core.model.Member
import com.composechef.testmate.data.demo.DemoWorkspace

@Preview(name = "Phone", widthDp = 393, heightDp = 852, showBackground = true)
@Preview(name = "Tablet", widthDp = 1000, heightDp = 800, showBackground = true)
@Preview(
    name = "Dark",
    widthDp = 393,
    heightDp = 852,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun CollectionsPreview() {
    val member = Member("me", "Alex", "AL")
    TaskMateTheme {
        Surface {
            CollectionsScreen(
                DemoWorkspace.collections(member),
                DemoWorkspace.tasks(),
                {},
                {},
            )
        }
    }
}
