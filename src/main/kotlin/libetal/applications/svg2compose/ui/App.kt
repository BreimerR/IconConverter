package libetal.applications.svg2compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.*
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.Times
import libetal.applications.svg2compose.MainViewModel
import libetal.applications.svg2compose.data.Icon
import libetal.applications.svg2compose.models.IconsViewModel
import libetal.applications.svg2compose.ui.icons.Assetor
import libetal.applications.svg2compose.ui.icons.Resize24
import libetal.applications.svg2compose.ui.layouts.IconsDisplayLayout
import libetal.applications.svg2compose.ui.layouts.SlectableText
import libetal.applications.svg2compose.ui.layouts.parser.ParseLayout
import libetal.applications.svg2compose.ui.theme.AppTheme
import libetal.kotlin.compose.narrator.Narrate
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.Narrator
import libetal.kotlin.compose.narrator.narration
import libetal.libraries.compose.layouts.*
import libetal.libraries.compose.layouts.icons.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.libraries.compose.layouts.text.InputLayout
import libetal.libraries.compose.ui.shape
import libetal.libraries.compose.layouts.DropdownMenuItem
import libetal.libraries.compose.layouts.text.InputModifier

@Composable
@Preview
fun App(viewModel: MainViewModel) = AppTheme(viewModel.isDarkMode) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        Narrator<AppNarrations> {

            BoxWithConstraints {

                val navSize = 48.dp
                val containerSize = maxWidth - navSize

                val browseNarration: Narration<AppNarrations> = AppNarrations.BROWSER.narration
                val parseNarration: Narration<AppNarrations> = AppNarrations.PARSER.narration

                Row(Modifier.fillMaxSize()) {

                    Column(Modifier.width(navSize), horizontalAlignment = Alignment.CenterHorizontally) {

                        IconButton(FontAwesomeIcons.Regular.FolderOpen, 42, contentDescription = "Browse Narration") {
                            browseNarration.begin()
                        }

                        IconButton(FontAwesomeIcons.Regular.Image, 42, contentDescription = "Parse File") {
                            parseNarration.begin()
                        }

                    }

                    Column(Modifier.width(containerSize).fillMaxHeight()) {

                        Narrate<AppNarrations>({ true }) {

                            AppNarrations.BROWSER({ IconsViewModel() }) {
                                IconsDisplayLayout(lifeCycleViewModel())
                            }

                            AppNarrations.PARSER {
                                ParseLayout()
                            }

                        }

                    }

                }

            }

        }

    }

}