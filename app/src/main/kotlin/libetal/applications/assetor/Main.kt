package libetal.applications.assetor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import libetal.applications.assetor.ui.Assetor
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.invoke


/**
 * Compose 1.1.1 user LayoutDirection in Animations
 * thus required here
 **/
fun main() = application {

    val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)
    val showSplashState = remember { mutableStateOf(true) }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Narration(showSplashState) {
            val showApp = createPremise { it -> it }
            val showSplash = createPremise { it -> !it }

            showApp(this, { MainViewModel() }) { viewModel ->
                Window(onCloseRequest = ::exitApplication, title = "Assetor", state = windowState) {
                    Assetor(viewModel)
                }
            }

            showSplash {
                Window(
                    onCloseRequest = ::exitApplication,
                    state = rememberWindowState(width = 300.dp, height = 300.dp),
                    undecorated = true,
                    transparent = true
                ) {

                    Surface {
                        Column {
                            Row {

                            }
                            Row {
                                Button({
                                    showSplashState.value = false
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

}