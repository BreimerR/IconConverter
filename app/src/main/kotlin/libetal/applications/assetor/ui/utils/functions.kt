package libetal.applications.assetor.ui.utils

import androidx.compose.runtime.Composable

@Composable
inline fun <T> T?.compose(block: @Composable (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}