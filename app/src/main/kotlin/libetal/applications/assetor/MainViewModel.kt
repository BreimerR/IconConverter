package libetal.applications.assetor

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import libetal.kotlin.compose.narrator.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    val isDarkModeState = mutableStateOf(true)
    var isDarkMode by isDarkModeState
}