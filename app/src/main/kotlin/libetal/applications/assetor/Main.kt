package libetal.applications.assetor

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import libetal.applications.assetor.ui.Assetor


/**
 * Compose 1.1.1 user LayoutDirection in Animations
 * thus required here
 **/
fun main() = application {

    val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)

    Window(onCloseRequest = ::exitApplication, title = "Assetor", state = windowState) {
        Assetor(MainViewModel())
    }

}