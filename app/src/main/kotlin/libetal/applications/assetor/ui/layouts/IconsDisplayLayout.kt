package libetal.applications.assetor.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.unit.*
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowRight
import compose.icons.fontawesomeicons.solid.CaretDown
import compose.icons.fontawesomeicons.solid.Search
import libetal.applications.assetor.models.FolderViewModel
import libetal.applications.assetor.models.IconViewModel
import libetal.applications.assetor.models.IconsViewModel
import libetal.applications.assetor.ui.components.NavigationDropDown
import libetal.applications.assetor.ui.icons.Assetor
import libetal.applications.assetor.ui.icons.IcFolder
import libetal.applications.assetor.ui.icons.Settings
import libetal.applications.assetor.ui.icons.ThemeMode
import androidx.compose.foundation.lazy.itemsIndexed
import libetal.kotlin.io.File
import libetal.kotlin.log.info
import libetal.libraries.compose.layouts.DropDownMenu
import libetal.libraries.compose.layouts.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.libraries.compose.ui.shape


val TAG = "IconsDisplayLayout"

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun IconExplorerLayout(themeMode: MutableState<Boolean>, viewModel: IconsViewModel) = Column(
    modifier = Modifier.fillMaxSize()
) {

    var path by remember { viewModel.pathState }

    val currentRootFolderState = remember { mutableStateOf(0) }
    val icons = remember { viewModel.painters }
    val folders = remember { viewModel.folders }
    val currentFolder by remember {
        derivedStateOf {
            folders.lastOrNull()
        }
    }

    val subFolders by remember {
        derivedStateOf {
            folders.map { IconsViewModel(it.path) }
        }
    }

    val lastItem = remember {
        derivedStateOf {
            folders.lastOrNull()
        }
    }

    var showSearchField by remember { mutableStateOf(false) }
    var iconSearch by remember { mutableStateOf<String?>(null) }
    val showResources = remember { mutableStateOf(false) }

    fun toggleShowResources() {
        showResources.value = !showResources.value
    }

    BoxWithConstraints(Modifier.fillMaxWidth().height(54.dp)) {
        val inputAreaWidth = maxWidth - 64.dp
        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            val inputContainerModifier = Modifier.fillMaxHeight().width(inputAreaWidth)


            Row(
                inputContainerModifier.padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    Modifier.fillMaxSize().shape(RoundedCornerShape(50)).padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier.fillMaxHeight()
                            .clickable(onClick = ::toggleShowResources)
                            .background(
                                MaterialTheme.colors.onBackground.copy(.2f),
                                RoundedCornerShape(
                                    percent = 50, RoundedCornerShapeDirections.START
                                )
                            )
                            .roundedCornerHorizontalPadding(end = false).padding(end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Resources")
                        Icon(FontAwesomeIcons.Solid.CaretDown, "Search Directories", Modifier.size(24.dp))
                        NavigationDropDown(showResources)
                    }
                    Divider(Modifier.fillMaxHeight(.8f).width(1.dp).background(MaterialTheme.colors.onBackground))
                    BoxWithConstraints(Modifier.fillMaxWidth().padding(start = 4.dp)) {
                        if (!showSearchField) {
                            Input(
                                path,
                                "/Example/Dir/Pictures",
                                singleLine = true,
                                containerModifier = Modifier.width(maxWidth - 64.dp).align(Alignment.CenterStart)
                            ) { newPath ->
                                path = newPath
                            }
                        } else {
                            Input(
                                iconSearch ?: "",
                                "ic_icon_name",
                                singleLine = true,
                                containerModifier = Modifier.width(maxWidth - 64.dp).align(Alignment.CenterStart)
                            ) { newIconSearch ->
                                iconSearch = newIconSearch
                            }
                        }
                        Row(
                            modifier = Modifier.align(Alignment.CenterEnd)
                        ) {
                            AnimatedVisibility(!showSearchField) {
                                IconButton(
                                    FontAwesomeIcons.Solid.ArrowRight, iconSize = 32, modifier = Modifier
                                ) {
                                    TAG info "Clearing folders"
                                    folders.clear()
                                    icons.clear()
                                    viewModel.scan()
                                }
                            }

                            IconButton(
                                FontAwesomeIcons.Solid.Search, iconSize = 32, modifier = Modifier
                            ) {
                                showSearchField = !showSearchField
                            }
                        }
                    }
                }
            }

            IconButton(Assetor.ThemeMode, iconSize = 32) {
                themeMode.value = !themeMode.value
            }

            IconButton(Assetor.Settings, iconSize = 32) {

            }


        }
    }

    BoxWithConstraints(Modifier.fillMaxSize()) {
        val drawerWidth = 240.dp
        var iconSize by remember { mutableStateOf(80) }
        val maxExplorerWidth = max(min(folders.size * iconSize.dp, maxWidth * 0.5f), iconSize.dp)

        Column(Modifier.fillMaxSize()) {
            Row(Modifier.fillMaxWidth().fillMaxHeight(0.4f)) {

                Column(Modifier.width(maxExplorerWidth).fillMaxHeight()) {
                    Row(Modifier.fillMaxWidth().height(56.dp)) {

                    }
                    LazyVerticalGrid(
                        GridCells.Adaptive(iconSize.dp),
                        Modifier.fillMaxSize().background(Color.Black.copy(.6f))
                    ) {
                        items(subFolders) { fVm ->
                            fVm.previewFolderViewModel(iconSize.dp) {
                                // TODO: Set current folder to this folder
                            }
                        }
                    }
                }

                AnimatedVisibility(lastItem.value != null) {
                    Column(Modifier.fillMaxSize()) {
                        Row(Modifier.fillMaxWidth().height(56.dp)) {
                            var iconSizeState by remember { mutableStateOf(false) }
                            Button({ iconSizeState = !iconSizeState }) {
                                Text("Icon size: $iconSize")
                            }
                            Text("Current Folder = ${currentRootFolderState.value}")
                            DropDownMenu(expanded = iconSizeState, { iconSizeState = false }) {
                                libetal.libraries.compose.layouts.DropdownMenuItem({
                                    iconSizeState = !iconSizeState
                                    iconSize = 80
                                }) {
                                    Text("80")
                                }
                                libetal.libraries.compose.layouts.DropdownMenuItem({
                                    iconSizeState = !iconSizeState
                                    iconSize = 100
                                }) {
                                    Text("100")
                                }
                                libetal.libraries.compose.layouts.DropdownMenuItem({
                                    iconSizeState = !iconSizeState
                                    iconSize = 120
                                }) {
                                    Text("120")
                                }
                            }
                        }


                    }
                }
                // TODO: Show Current Icon preview here

            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 62.dp),
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(.4f))
            ) {

                itemsIndexed(icons) { i, iconViewModel ->
                    if (i == icons.size - 1) {
                        viewModel.scan()
                    }
                    val painter by remember { iconViewModel.painter }
                    Column(Modifier.padding(2.dp)) {
                        AnimatedVisibility(painter != null) {
                            Icon(painter!!, iconViewModel.name, modifier = Modifier.size(56.dp))
                        }
                    }
                }
            }

        }
    }

    DisposableEffect(icons, folders) {
        onDispose {
            icons.clear()
            folders.clear()
        }
    }

}


enum class RoundedCornerShapeDirections {
    START, END
}

fun RoundedCornerShape(percent: Int, direction: RoundedCornerShapeDirections) = when (direction) {
    RoundedCornerShapeDirections.START -> RoundedCornerShape(
        topStartPercent = percent, bottomStartPercent = percent
    )

    RoundedCornerShapeDirections.END -> RoundedCornerShape(
        topEndPercent = percent, bottomEndPercent = percent
    )
}

@Composable
fun Modifier.roundedCornerHorizontalPadding(start: Boolean = true, end: Boolean = true) = this.then(
    RoundedCornerHorizontalModifier(start, end)
)

class RoundedCornerHorizontalModifier(val start: Boolean, val end: Boolean) : LayoutModifier {

    override fun MeasureScope.measure(
        measurable: Measurable, constraints: Constraints
    ): MeasureResult {

        val initialPlaceable = measurable.measure(constraints)

        val initialHeight = initialPlaceable.height
        val paddingSpace = initialHeight / 2
        val horizontal = when {
            start && end -> initialHeight
            start || end -> paddingSpace
            else -> initialHeight
        }


        val placeable = measurable.measure(constraints.offset(-horizontal, 0))

        val width = constraints.constrainWidth(placeable.width + horizontal)
        val height = constraints.constrainHeight(placeable.height)
        return layout(width, height) {
            when {
                start && end -> placeable.placeRelative(paddingSpace, 0)
                start -> placeable.placeRelative(paddingSpace, 0)
                end -> placeable.placeRelative(0, 0)
            }
        }
    }
}


@Composable
fun IconsViewModel.previewFolderViewModel(iconSize: Dp, onClick: () -> Unit) =
    Column(Modifier.sizeIn(maxWidth = iconSize), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Assetor.IcFolder, path, Modifier.clickable {
            onClick()
        }.fillMaxWidth(0.6f))
        Spacer(Modifier.height(2.dp))
        Text(folderName)
    }


@Composable
fun File.previewFolderViewModel(iconSize: Dp, onClick: () -> Unit) =
    Column(Modifier.sizeIn(maxWidth = iconSize), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Assetor.IcFolder, path, Modifier.clickable {
            onClick()
        }.fillMaxWidth(0.6f))
        Spacer(Modifier.height(2.dp))
        Text(name)
    }
