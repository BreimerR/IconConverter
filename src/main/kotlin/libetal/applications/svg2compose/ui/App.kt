package libetal.applications.svg2compose.ui

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
import libetal.applications.svg2compose.data.Icon
import libetal.applications.svg2compose.models.IconsViewModel
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

@Composable
@Preview
fun App(viewModel: MainViewModel) = AppTheme(viewModel.isDarkMode) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        val iconModifier = Modifier.size(24.dp)

        Narrator<AppNarrations> {

            BoxWithConstraints {

                val navSize = 48.dp
                val containerSize = maxWidth - navSize

                val browseNarration: Narration<AppNarrations> = AppNarrations.BROWSER.narration
                val parseNarration: Narration<AppNarrations> = AppNarrations.PARSER.narration

                Row(Modifier.fillMaxSize()) {

                    Column(Modifier.width(navSize), horizontalAlignment = Alignment.CenterHorizontally) {

                        IconButton(FontAwesomeIcons.Regular.FolderOpen, 42, contentDescription = "Browse Narration") {
                            browseNarration.begin()
                        }

                        IconButton(FontAwesomeIcons.Regular.Image, 42, contentDescription = "Parse File") {
                            parseNarration.begin()
                        }

                    }

                    Column(Modifier.width(containerSize).fillMaxHeight()) {

                        Narrate<AppNarrations>({ true }) {

                            AppNarrations.PARSER {
                                ParseLayout()
                            }

                            AppNarrations.BROWSER({ IconsViewModel() }) {
                                IconsDisplayLayout(lifeCycleViewModel())
                            }

                        }

                    }

                }
            }
        }


    }
}


@Composable
fun Modifier.toolbarInputDecorator(): Modifier = this.background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(50))

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun IconsDisplayLayout(viewModel: IconsViewModel) {

    var searchIcon by remember {
        mutableStateOf<String?>(null)
    }

    val scraping by remember { viewModel.scrapingState }

    val filtered by remember { derivedStateOf { searchIcon != null } }

    val icons = remember { viewModel.iconsState }

    val presentableIcons by remember {
        derivedStateOf {
            if (filtered) viewModel.icons.filter { icon -> icon.path.contains(".*$searchIcon.*".toRegex()) } else icons
        }
    }

    var currentIcon by remember { mutableStateOf<Icon?>(null) }

    BoxWithConstraints(modifier = Modifier.fillMaxSize().padding(4.dp)) {
        val iconSize = 36
        val toolBarHeight = 56.dp
        val contentHeight = maxHeight - toolBarHeight

        Column(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.height(toolBarHeight).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.onBackground) {

                    BoxWithConstraints(Modifier.height(iconSize.dp).fillMaxWidth(0.4f)) {

                        val showClear by remember { derivedStateOf { searchIcon != null } }

                        val inputWidth = maxWidth - (iconSize.dp * if (showClear) 2 else 1)

                        Row(
                            modifier = Modifier.fillMaxSize().padding(vertical = 2.dp).toolbarInputDecorator(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                FontAwesomeIcons.Solid.Search,
                                iconSize,
                                contentDescription = "Search Icon"
                            ) {

                            }
                            Input(
                                searchIcon ?: "",
                                placeHolder = "Search Icon",
                                modifier = Modifier.height(32.dp).width(inputWidth),
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = MaterialTheme.colors.primaryVariant,
                                    cursorColor = MaterialTheme.colors.onPrimary,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(0)
                            ) {
                                if (!scraping) searchIcon = it.trim().ifBlank { null }
                            }
                            AnimatedVisibility(showClear) {
                                IconButton(
                                    FontAwesomeIcons.Solid.Times,
                                    iconSize,
                                    contentDescription = "Clear Text"
                                ) {
                                    if (!scraping) searchIcon = null
                                }
                            }
                        }
                    }
                    Spacer(Modifier.width(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp).toolbarInputDecorator(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        var directory by remember { viewModel.path }

                        IconButton(FontAwesomeIcons.Regular.Folder, iconSize, contentDescription = "Current Folder") {

                        }

                        Input(
                            directory,
                            placeHolder = "Search Icon",
                            modifier = Modifier.height(32.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = MaterialTheme.colors.primaryVariant,
                                cursorColor = MaterialTheme.colors.onPrimary,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        ) {
                            directory = it.trim().ifBlank { "" }
                        }

                    }

                }
            }

            Row(modifier = Modifier.height(contentHeight).fillMaxWidth()) {

                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val iconsPreviewWidth: Dp = if (currentIcon == null) maxWidth
                    else 240.dp

                    val iconContentWidth: Dp = maxWidth - iconsPreviewWidth

                    Row(Modifier.fillMaxSize()) {
                        Column(Modifier.width(iconsPreviewWidth)) {

                            var iconPreviewSize by remember { mutableStateOf(80) }

                            Row(
                                Modifier.height(32.dp).fillMaxWidth().padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {


                                Row {

                                    Input(iconPreviewSize.toString(), modifier = Modifier.width(80.dp).padding(horizontal = 16.dp)) {
                                        it.trim().ifEmpty { null }?.toIntOrNull()?.let { size ->
                                            iconPreviewSize = size
                                        }
                                    }

                                    Spacer(Modifier.width(2.dp))

                                    IconButton(FontAwesomeIcons.Solid.Plus, 24, contentDescription = "Increase icon Size") {
                                        iconPreviewSize += 2
                                    }

                                    Spacer(Modifier.width(2.dp))

                                    IconButton(FontAwesomeIcons.Solid.Minus, 24, contentDescription = "Decrease Icon Size") {
                                        iconPreviewSize -= 2
                                    }

                                }
                            }

                            val scrollState = rememberLazyListState()

                            LazyVerticalGrid(
                                GridCells.Adaptive(iconPreviewSize.dp),
                                state = scrollState,
                                modifier = Modifier.fillMaxSize(),
                            ) {

                                itemsIndexed(presentableIcons) { index, icon ->
                                    fun onPreviewClick() {
                                        currentIcon = icon
                                    }

                                    val iconModifier = Modifier.size(iconPreviewSize.dp).clickable(onClick = ::onPreviewClick)

                                    if (filtered) {
                                        IconPreviewLayout(icon, iconModifier)
                                    } else {
                                        if (index == icons.size - 1) viewModel.updateIconsState()
                                        IconPreviewLayout(icon, iconModifier)
                                    }

                                }

                            }

                        }

                        currentIcon.compose { icon ->
                            IconClassFileLayout(iconContentWidth, icon) {
                                currentIcon = null
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun IconClassFileLayout(
    iconContentWidth: Dp,
    icon: Icon,
    onCloseRequest: () -> Unit
) {

    Column(Modifier.width(iconContentWidth).fillMaxHeight()) {

        val scrollState = rememberScrollState()
        val hScrollState = rememberScrollState()
        val clipBoardManager = LocalClipboardManager.current

        Row(
            Modifier.height(38.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {


            IconButton(FontAwesomeIcons.Regular.TimesCircle, 32, "Icon", onClick = onCloseRequest)

            Text(icon.path.substringAfterLast("/"))

            IconButton(FontAwesomeIcons.Regular.Copy, 32, "Copy Icon") {
                clipBoardManager.setText(AnnotatedString(icon.composeClassFile))
            }

        }

        Column(Modifier.fillMaxSize().verticalScroll(scrollState).horizontalScroll(hScrollState)) {
            Column(Modifier.fillMaxHeight().padding(4.dp).wrapContentSize()) {
                SlectableText(icon.composeClassFile)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun IconPreviewLayout(icon: Icon, modifier: Modifier) {

    Column(
        modifier.padding(2.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(true) {
            Card(Modifier.fillMaxSize()) {
                Column(
                    Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(icon.painter, "Description", modifier.fillMaxSize(0.7f))

                }
            }
        }
    }

}


@Composable
inline fun <T> T?.compose(block: @Composable (T) -> Unit) {
    if (this != null) {
        block(this)
    }
}