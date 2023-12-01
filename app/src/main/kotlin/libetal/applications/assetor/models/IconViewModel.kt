package libetal.applications.assetor.models

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.io.File
import libetal.kotlin.laziest
import libetal.libraries.compose.painter.loadSvgPainter

class IconViewModel(file: File, density: Density = Density(24f)) : ViewModel() {

    val name by lazy {
        file.name
    }

    val painter by laziest {
        mutableStateOf<Painter?>(null).apply {
            ioLaunch {
                try {
                    val painter = loadSvgPainter(file, density)
                    launch {
                        value = painter
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

}
