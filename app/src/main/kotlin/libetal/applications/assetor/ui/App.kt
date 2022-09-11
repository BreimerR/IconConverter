package libetal.applications.assetor.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.*
import libetal.applications.assetor.MainViewModel
import libetal.applications.assetor.models.IconsViewModel
import libetal.applications.assetor.ui.layouts.IconsDisplayLayout
import libetal.applications.assetor.ui.layouts.parser.ParseLayout
import libetal.applications.assetor.ui.theme.AppTheme
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.invoke
import libetal.kotlin.compose.narrator.NarrationScopeImpl
import libetal.kotlin.compose.narrator.createScopeCollector
import libetal.libraries.compose.layouts.IconButton

@Composable
@Preview
fun Assetor(viewModel: MainViewModel) = AppTheme(viewModel.isDarkMode) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        val scope by createScopeCollector<NarrationScopeImpl<AppNarrations>>()

        BoxWithConstraints {
            val navSize = 48.dp
            val containerSize = maxWidth - navSize

            Row(Modifier.fillMaxSize()) {

                Column(Modifier.width(navSize), horizontalAlignment = Alignment.CenterHorizontally) {

                    IconButton(FontAwesomeIcons.Regular.FolderOpen, 42, contentDescription = "Browse Narration") {
                        with(scope) {
                            AppNarrations.BROWSER.narrate()
                        }

                    }

                    IconButton(FontAwesomeIcons.Regular.Image, 42, contentDescription = "Parse File") {
                        with(scope) {
                            AppNarrations.PARSER.narrate()
                        }
                    }

                }

                Column(Modifier.width(containerSize).fillMaxHeight()) {

                    Narration<AppNarrations> {


                        AppNarrations.PARSER {
                            ParseLayout()
                        }

                        AppNarrations.BROWSER(this, { IconsViewModel() }) { viewModel ->
                            IconsDisplayLayout(viewModel)
                        }

                    }

                }

            }

        }

    }

}

