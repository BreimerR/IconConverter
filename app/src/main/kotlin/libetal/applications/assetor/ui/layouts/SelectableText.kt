package libetal.applications.assetor.ui.layouts

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle


@Composable
fun SelectableText(
    text: String,
    style: TextStyle = MaterialTheme.typography.body1,
    textModifier: Modifier = Modifier,
    selectContainerModifier: Modifier = Modifier.wrapContentHeight().fillMaxWidth()
) {

    val customTextSelectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colors.onSecondary,
        backgroundColor = MaterialTheme.colors.secondary,
    )

    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {

        SelectionContainer(selectContainerModifier) {
            Text(
                text,
                style = style,
                modifier = textModifier
            )
        }

    }


}