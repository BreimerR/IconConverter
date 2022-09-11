package libetal.applications.assetor

import androidx.compose.material.Text
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {

    val windowState = rememberWindowState()

    Window(onCloseRequest = ::exitApplication, title = "Assetor", state = windowState) {
        //  App(this@showApp)
        Text("Hello World")
    }

}