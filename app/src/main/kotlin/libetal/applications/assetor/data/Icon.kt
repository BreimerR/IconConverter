package libetal.applications.assetor.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import java.io.InputStream

data class Icon(val path: String, val painter: Painter, val inputStream: InputStream) {

    val composeClassFileState by lazy {
        mutableStateOf("")
    }

    var composeClassFile: String
        get() = composeClassFileState.value
        set(value) {
            composeClassFileState.value = value
        }

}


