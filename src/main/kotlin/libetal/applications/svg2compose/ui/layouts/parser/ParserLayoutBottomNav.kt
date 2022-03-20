package libetal.applications.svg2compose.ui.layouts.parser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Copy
import libetal.applications.svg2compose.convert
import libetal.kotlin.compose.narrator.Narrate
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.narration


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
