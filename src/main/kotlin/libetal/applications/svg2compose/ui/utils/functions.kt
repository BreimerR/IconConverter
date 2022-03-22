package libetal.applications.svg2compose.ui.utils

import androidx.compose.runtime.Composable

@Composable
inline fun <T> T?.compose(block: @Composable (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}