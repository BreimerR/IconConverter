package libetal.applications.svg2compose.utils

import androidx.compose.ui.text.AnnotatedString

val String.annotated
    get() = AnnotatedString(this)