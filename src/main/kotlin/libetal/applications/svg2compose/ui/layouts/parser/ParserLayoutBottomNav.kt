package libetal.applications.svg2compose.ui.layouts.parser

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp


@Composable
fun ParserLayoutBottomNav(
    navigationHeight: Dp,
    onGenerateClick: () -> Unit
) {
    Row(Modifier.fillMaxWidth().height(navigationHeight), verticalAlignment = Alignment.CenterVertically) {
        Button(onGenerateClick) {
            Text("Generate")
        }
    }
}
