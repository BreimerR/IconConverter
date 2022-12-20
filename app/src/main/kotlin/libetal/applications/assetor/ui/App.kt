package libetal.applications.assetor.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import libetal.applications.assetor.MainViewModel
import libetal.applications.assetor.models.IconsViewModel
import libetal.applications.assetor.ui.layouts.IconsDisplayLayout
import libetal.applications.assetor.ui.layouts.parser.ParseLayout
import libetal.applications.assetor.ui.theme.AppTheme
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.invoke
import libetal.kotlin.compose.narrator.NarrationScopeImpl
import libetal.kotlin.compose.narrator.createScopeCollector

@Composable
fun Assetor(viewModel: MainViewModel) = AppTheme(viewModel.isDarkMode) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        val scope by createScopeCollector<NarrationScopeImpl<AppNarrations>>()

        BoxWithConstraints {
            val navSize = 48.dp
            val containerSize = maxWidth - navSize

            Column(Modifier.fillMaxSize()) {

                Column(Modifier.fillMaxSize()) {

                    Narration<AppNarrations> {

                        AppNarrations.PARSER {
                            ParseLayout(viewModel)
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

