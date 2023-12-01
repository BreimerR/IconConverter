package libetal.applications.assetor.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
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
import libetal.applications.assetor.models.IconsViewModel
import libetal.applications.assetor.ui.components.NavigationDropDown
import libetal.applications.assetor.ui.icons.*
import libetal.kotlin.io.File
import libetal.kotlin.log.info
import libetal.libraries.compose.layouts.DropDownMenu
import libetal.libraries.compose.layouts.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.libraries.compose.ui.shape


const val TAG = "IconsDisplayLayout"

@Composable
fun IconExplorerLayout(themeMode: MutableState<Boolean>, viewModel: IconsViewModel) = Column(
    modifier = Modifier.fillMaxSize()
) {

    var path by remember { viewModel.pathState }
    var deepSearch by remember { viewModel.deepSearchState }

    val currentRootFolderState = remember { mutableStateOf(0) }
    val icons = remember { viewModel.painters }
    val folders = remember { viewModel.folders }
    val currentFolder by remember { mutableStateOf("") }

    val subFolders = remember { viewModel.subFolders }

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
                    Modifier.fillMaxSize()
                        .shape(RoundedCornerShape(50))
                        .background(
                            Brush.radialGradient(
                                radius = 300f,
                                colors = listOf(
                                    Color(0xFF970069),
                                    Color(0xFF7B00A7)
                                )
                            )
                        )
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        Modifier.fillMaxHeight()
                            .clickable(onClick = ::toggleShowResources)
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
        val folderContainerSize by remember { mutableStateOf(40) }

        Column(Modifier.fillMaxSize().padding(12.dp, 4.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {

                CircleIcon(
                    Assetor.ClearFilters,
                    {
                    // TODO Clear filters
                    },
                    size = 32.dp
                )

                Row(
                    Modifier.clickable {
                        deepSearch = !deepSearch
                    },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Switch(
                        deepSearch, {
                            deepSearch = !deepSearch
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green
                        )
                    )
                    Text("Deep Search")
                }
                AnimatedVisibility(lastItem.value != null) {

                    Row {
                        var iconSizeState by remember { mutableStateOf(false) }
                        Row(
                            Modifier
                                .shape(RoundedCornerShape(4.dp), MaterialTheme.colors.primary, elevation = 2.dp)
                                .clickable { iconSizeState = !iconSizeState }
                                .padding(12.dp, 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("ICON SIZE")
                            Divider(
                                Modifier.width(1.dp).height(LocalTextStyle.current.fontSize.value.dp)
                                    .background(Color.White)
                            )
                            Text("$iconSize")
                        }

                        DropDownMenu(expanded = iconSizeState, { iconSizeState = false }) {
                            val padding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            val sizes = listOf(48, 52, 80, 100, 120)

                            for (size in sizes) {
                                DropdownMenuItem(
                                    {
                                        iconSizeState = !iconSizeState
                                        iconSize = size
                                    },
                                    contentPadding = padding,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("$size")
                                }
                            }

                        }
                    }

                }
            }

            Row(
                Modifier.fillMaxWidth()
                    .height((folderContainerSize * 2 + 8).dp)
                    .shape(RoundedCornerShape(8.dp), Color.Black.copy(.6f)),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.fillMaxHeight()) {
                    viewModel.previewFolderViewModel(120.dp)
                }
                Divider(Modifier.fillMaxHeight(.8f).width(1.dp).background(Color.White))

                LazyVerticalGrid(
                    GridCells.Adaptive(folderContainerSize.dp),
                    Modifier.fillMaxWidth().fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(subFolders) { fVm ->
                        fVm.previewFolderViewModel(folderContainerSize.dp) {
                        }
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.FixedSize(iconSize.dp),
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(.2f))
            ) {

                itemsIndexed(icons) { i, iconViewModel ->
                    if (i == icons.size - 1) {
                        viewModel.scan()
                    }
                    val painter by remember { iconViewModel.painter }
                    Column(Modifier.padding(2.dp)) {
                        AnimatedVisibility(painter != null) {
                            when (val iconPainter = painter) {
                                null -> TAG info "Painter is still empty"
                                else -> Icon(iconPainter, iconViewModel.name, modifier = Modifier.size(iconSize.dp))
                            }
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

    LaunchedEffect(currentFolder) {
        viewModel.updateSubFolders()
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
fun IconsViewModel.previewFolderViewModel(maxWidth: Dp, onClick: () -> Unit = {}) =
    Row(Modifier.widthIn(min = maxWidth)
        .clickable {
            onClick()
        }) {
        Icon(
            Assetor.IcFolder,
            path,
            Modifier.size(max(maxWidth * (1 / 3), 24.dp))
        )
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


class WithinCells (private val minSize: Dp) : GridCells {
    init {
        require(minSize > 0.dp) { "Provided min size $minSize should be larger than zero." }
    }

    override fun Density.calculateCrossAxisCellSizes(
        availableSize: Int,
        spacing: Int
    ): List<Int> {
        val count = maxOf((availableSize + spacing) / (minSize.roundToPx() + spacing), 1)
        return calculateCellsCrossAxisSizeImpl(availableSize, count, spacing)
    }

    override fun hashCode(): Int {
        return minSize.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is WithinCells && minSize == other.minSize
    }

    fun calculateCellsCrossAxisSizeImpl(
        gridSize: Int,
        slotCount: Int,
        spacing: Int
    ): List<Int> {
        val gridSizeWithoutSpacing = gridSize - spacing * (slotCount - 1)
        val slotSize = gridSizeWithoutSpacing / slotCount
        val remainingPixels = gridSizeWithoutSpacing % slotCount
        return List(slotCount) {
            slotSize + if (it < remainingPixels) 1 else 0
        }
    }

}
