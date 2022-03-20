package libetal.applications.svg2compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import libetal.applications.svg2compose.ui.App
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.compose.narrator.narration

fun main() = application {

    val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)

    Narration<MainNarration> {

        MainNarration.APP({ MainViewModel() }) {
            Window(onCloseRequest = ::exitApplication, title = "SVG 2 Compose", state = windowState) {

                val viewModel = lifeCycleViewModel<MainViewModel>()

                App(viewModel)

            }
        }

        MainNarration.SPLASH {
            Window(
                onCloseRequest = ::exitApplication,
                state = rememberWindowState(width = 300.dp, height = 300.dp),
                undecorated = true,
                transparent = true
            ) {
                val appNarration = MainNarration.APP.narration

                Surface {
                    Column {
                        Row {

                        }
                        Row {
                            Button({
                                appNarration.begin()
                            }) {
                                Text("Start App")
                            }
                        }
                    }
                }
            }
        }

    }

}

class MainViewModel : ViewModel() {
    private val isDarkModeState = mutableStateOf(true)
    var isDarkMode by isDarkModeState
}

enum class MainNarration {
    APP,
    SPLASH
}