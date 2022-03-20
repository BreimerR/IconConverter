package libetal.applications.svg2compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.ResourceLoader
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import br.com.devsrsouza.svg2compose.Size
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Folder
import compose.icons.fontawesomeicons.regular.FolderOpen
import compose.icons.fontawesomeicons.regular.Image
import compose.icons.fontawesomeicons.regular.TimesCircle
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.Times
import kotlinx.coroutines.*
import libetal.applications.svg2compose.MainViewModel
import libetal.applications.svg2compose.convert
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
            if (filtered) icons.filter { icon -> icon.path.contains(".*$searchIcon.*".toRegex()) } else icons
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

                val rowScope = this

                BoxWithConstraints(Modifier.fillMaxSize()) {
                    val iconsPreviewWidth: Dp = if (currentIcon == null) maxWidth
                    else 240.dp

                    val iconContentWidth: Dp = maxWidth - iconsPreviewWidth

                    Row(Modifier.fillMaxSize()) {
                        Column(Modifier.width(iconsPreviewWidth)) {

                            var iconPreviewSize by remember { mutableStateOf(80) }

                            Row(
                                Modifier.height(32.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                Input(iconPreviewSize.toString(), modifier = Modifier.width(40.dp).padding(horizontal = 4.dp)) {
                                    it.trim().ifEmpty { null }?.toIntOrNull()?.let { size ->
                                        iconPreviewSize = size
                                    }
                                }
                            }

                            val scrollState = rememberLazyListState()

                            LazyVerticalGrid(GridCells.Adaptive(iconPreviewSize.dp),state = scrollState, modifier = Modifier.fillMaxSize(),) {

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

                        if (currentIcon != null) {
                            Column(Modifier.width(iconContentWidth).fillMaxHeight()) {
                                Row(Modifier.height(38.dp)) {
                                    IconButton(FontAwesomeIcons.Regular.TimesCircle, 32, contentDescription = "Icon") {
                                        currentIcon = null
                                    }
                                }

                                var iconClassState by remember {
                                    mutableStateOf("")
                                }

                                viewModel.convert(currentIcon!!) {
                                    iconClassState = it
                                }

                                val scrollState = rememberScrollState()
                                val hScrollState = rememberScrollState()

                                Column(Modifier.fillMaxSize().verticalScroll(scrollState).horizontalScroll(hScrollState)) {
                                    Column(Modifier.fillMaxHeight().wrapContentSize()) {
                                        Text(iconClassState)
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
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

data class Icon(val path: String, val painter: Painter, val inputStream: InputStream, var composeClassFile: String = "")

class IconsViewModel : ViewModel() {

    private var job: Job? = null

    val path = mutableStateOf(System.getProperty("user.home") ?: "")

    var scrapingState = mutableStateOf(false)

    var scraping by scrapingState

    var currentPath: String = path.value

    val iconsState = mutableStateListOf<Icon>()

    val icons = mutableListOf<Icon>()

    override fun onCreate() {
        Log.d(TAG, "Creating...")
    }

    override fun onPause() {
        super.onPause()
        Log.d("IconsViewModel", "Pausing...")
    }

    override fun onStart() {
        super.onStart()
        Log.d("IconsViewModel", "Started...")
    }

    override fun onResume() {
        super.onResume()
        loadPath()
        Log.d(TAG, "Resumed ...")
    }

    fun updateIconsState(chunkSize: Int = 10) {

        if (scraping) coroutineScope.launch(Dispatchers.IO) {
            while (scraping && isActive) {
            }
            updateIconsState(chunkSize)
        } else {

            Log.d(TAG, "Updating icons = ${icons.size} = Icons state= ${iconsState.size}")
            var i = iconsState.size
            val max = i + chunkSize

            while (i < max && i < icons.size) {
                iconsState.add(icons[i++])
            }
        }

    }

    fun loadPath() {
        job = coroutineScope.launch(Dispatchers.IO) {
            val path = currentPath.trim().ifBlank { null }

            path?.let { activePath ->
                scraping = true
                File(activePath).getIcons()
                scraping = false
            }
        }
    }

    fun File.getIcons() {
        if (job != null) {
            if (job?.isActive == true) {
                if (isDirectory) {

                    if (path == "." || path == "..") return

                    currentPath = path

                    try {
                        toPath().forEachDirectoryEntry {
                            it.toFile().getIcons()
                        }
                    } catch (e: java.nio.file.AccessDeniedException) {
                        Log.d(TAG, "Access denied for $path")
                    }

                } else {
                    isSvg {

                        val stream = try {
                            val stream = inputStream()
                            if (stream.readAllBytes().isEmpty()) {
                                Log.d(TAG, "File is empty $path")
                                null
                            } else inputStream()
                        } catch (e: java.nio.file.AccessDeniedException) {
                            Log.d(TAG, "File access denied $path")
                            null
                        } ?: return Log.d(TAG, "Stream for $path is null")

                        val painter = try {
                            loadSvgPainter(stream, Density(80f, 1f))
                        } catch (e: Exception) {
                            Log.d(TAG, "Failed to get painter")
                            null
                        } ?: return Log.d(TAG, "Painter for $path is null")

                        val icon = Icon(path, painter, stream)

                        try {
                            icon.composeClassFile = icon.convert(size = Size(24))

                            icons.add(icon)

                            coroutineScope.launch(Dispatchers.Main) {
                                if (iconsState.size < 10) {
                                    iconsState.add(icon)
                                }
                            }

                        } catch (e: Exception) {
                            Log.w(TAG, "Unsupported icon ${icon.path}", e)
                        }


                    }

                }
            } else Log.d(TAG, "Job was canceled")
        } else Log.d(TAG, "Job is empty")

    }

    private inline fun File.isSvg(action: () -> Unit) {
        path.trim().ifBlank { null }?.split("/")?.let { sections ->
            if (sections.last().lowercase().split('.').last() == "svg") {
                action()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("IconsViewModel", "Decomposing Icons... $state")
        iconsState.clear()
        icons.clear()
        currentPath = path.value

    }

    val tempDir by lazy {
        createTempDirectory("ic_converter").toFile()
    }

    fun convert(
        currentIcon: Icon,
        receiverName: String = "Icon",
        packageName: String = "com.example",
        size: Size? = null,
        onComplete: (String) -> Unit
    ) {
        coroutineScope.launch {
            onComplete(
                currentIcon.convert(
                    receiverName,
                    packageName,
                    size
                )
            )
        }
    }

    fun Icon.convert(receiverName: String = "Icon", packageName: String = "com.example", size: Size? = null) =
        File(path).convert(tempDir, receiverName, packageName, size)

    companion object {
        const val TAG = "IconsViewModel"
    }

}
