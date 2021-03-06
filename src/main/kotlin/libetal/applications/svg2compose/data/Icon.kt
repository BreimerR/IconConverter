package libetal.applications.svg2compose.data

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalInputModeManager
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.devsrsouza.svg2compose.Size
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.*
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.Times
import kotlinx.coroutines.*
import libetal.applications.svg2compose.MainViewModel
import libetal.applications.svg2compose.convert
import libetal.applications.svg2compose.ui.layouts.SlectableText
import libetal.applications.svg2compose.ui.layouts.parser.ParseLayout
import libetal.applications.svg2compose.ui.theme.AppTheme
import libetal.kotlin.compose.narrator.Narrate
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.Narrator
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.compose.narrator.narration
import libetal.libraries.compose.layouts.icons.AsyncIcon
import libetal.libraries.compose.layouts.icons.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.multiplatform.log.Log
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.io.path.createTempDirectory
import kotlin.io.path.forEachDirectoryEntry

data class Icon(val path: String, val painter: Painter, val inputStream: InputStream) {

    val composeClassFileState = mutableStateOf("")

    var composeClassFile: String
        get() = composeClassFileState.value
        set(value) {
            composeClassFileState.value = value
        }

}


