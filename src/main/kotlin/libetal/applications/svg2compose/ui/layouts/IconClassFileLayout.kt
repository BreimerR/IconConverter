package libetal.applications.svg2compose.ui.layouts

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Copy
import compose.icons.fontawesomeicons.regular.TimesCircle
import libetal.applications.svg2compose.data.Icon
import libetal.libraries.compose.layouts.icons.IconButton

@Composable
fun IconClassFileLayout(
    iconContentWidth: Dp,
    icon: Icon,
    onCloseRequest: () -> Unit
) {

    Column(Modifier.width(iconContentWidth).fillMaxHeight()) {

        val scrollState = rememberScrollState()
        val hScrollState = rememberScrollState()
        val clipBoardManager = LocalClipboardManager.current

        Row(
            Modifier.height(38.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            IconButton(FontAwesomeIcons.Regular.TimesCircle, 32, "Icon", onClick = onCloseRequest)

            Text(icon.path.substringAfterLast("/"))

            IconButton(FontAwesomeIcons.Regular.Copy, 32, "Copy Icon") {
                clipBoardManager.setText(AnnotatedString(icon.composeClassFile))
            }

        }

        Column(Modifier.fillMaxSize().verticalScroll(scrollState).horizontalScroll(hScrollState)) {
            Column(Modifier.fillMaxHeight().padding(4.dp).wrapContentSize()) {
                SlectableText(icon.composeClassFile)
            }
        }
    }
}
