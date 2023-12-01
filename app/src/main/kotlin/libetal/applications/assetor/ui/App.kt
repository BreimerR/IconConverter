package libetal.applications.assetor.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import libetal.applications.assetor.MainViewModel
import libetal.applications.assetor.models.IconsViewModel
import libetal.applications.assetor.ui.layouts.IconExplorerLayout
import libetal.applications.assetor.ui.layouts.parser.ParseLayout
import libetal.applications.assetor.ui.theme.AppTheme
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.invoke

@Composable
fun Assetor(mainViewModel: MainViewModel) = AppTheme(mainViewModel.isDarkMode) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        Narration {

            AppNarrations.PARSER {
                ParseLayout(mainViewModel)
            }

            AppNarrations.FILE_EXPLORER(this, { IconsViewModel() }) { viewModel ->
                IconExplorerLayout(mainViewModel.isDarkModeState, viewModel)
            }

        }

    }

}
