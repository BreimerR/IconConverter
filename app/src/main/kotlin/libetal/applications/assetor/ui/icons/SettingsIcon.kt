package libetal.applications.assetor.ui.icons

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import libetal.kotlin.io.File
import libetal.kotlin.io.inputStream

@Composable
fun SettingsIcon(contentDescription: String, modifier: Modifier = Modifier) = Icon(
    Assetor.Settings,
    contentDescription,
    modifier = modifier
)