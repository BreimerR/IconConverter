package libetal.applications.svg2compose.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.regular.Folder
import compose.icons.fontawesomeicons.solid.Minus
import compose.icons.fontawesomeicons.solid.Plus
import compose.icons.fontawesomeicons.solid.Search
import compose.icons.fontawesomeicons.solid.Times
import libetal.applications.svg2compose.data.Icon
import libetal.applications.svg2compose.models.IconsViewModel
import libetal.applications.svg2compose.ui.utils.compose
import libetal.applications.svg2compose.ui.icons.Assetor
import libetal.applications.svg2compose.ui.icons.Resize24
import libetal.libraries.compose.layouts.DropdownMenuItem
import libetal.libraries.compose.layouts.SizeIn
import libetal.libraries.compose.layouts.icons.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.libraries.compose.layouts.text.InputLayout
import libetal.libraries.compose.layouts.text.InputModifier
import libetal.libraries.compose.ui.shape

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun IconsDisplayLayout(viewModel: IconsViewModel) {

    var searchIcon by remember {
        mutableStateOf<String?>(null)
    }

    val scraping by remember { viewModel.scrapingState }

    val filtered by remember { derivedStateOf { searchIcon != null } }

    val icons = remember { viewModel.iconsState }

    val presentableIcons by remember {
        derivedStateOf {
            val regexString = searchIcon?.replace("[^a-zA-Z0-9_\\s\\t\\n]".toRegex(), "")
            if (filtered) viewModel.icons.filter { icon -> icon.path.contains(".*$regexString.*".toRegex()) } else icons
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

                        InputLayout(
                            InputModifier.Default {
                                Modifier.height(iconSize.dp)
                                    .shape(RoundedCornerShape(50), MaterialTheme.colors.secondary, 2.dp)
                                    .padding(start = 2.dp, end = 2.dp)
                            }(verticalAlignment = Alignment.CenterVertically)
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
                                modifier = Modifier.wrapContentHeight().width(inputWidth),
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

                    InputLayout(
                        InputModifier.Default {
                            Modifier.fillMaxWidth()
                                .height(iconSize.dp)
                                .shape(RoundedCornerShape(50), color = MaterialTheme.colors.secondary)
                                .padding(horizontal = 4.dp)
                        }
                    ) { it ->

                        var directory by remember { viewModel.path }

                        IconButton(FontAwesomeIcons.Regular.Folder, iconSize, contentDescription = "Current Folder") {

                        }

                        Input(
                            directory,
                            placeHolder = "Search Icon",
                            modifier = it.fillMaxWidth()
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
                            var asIcon by remember { mutableStateOf(false) }

                            Row(
                                Modifier.height(32.dp).fillMaxWidth().padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {

                                var presetSizesShow by remember { mutableStateOf(false) }

                                Switch(asIcon, {
                                    asIcon = it
                                })

                                InputLayout(
                                    InputModifier.Default {
                                        height(28.dp)
                                            .width(90.dp)
                                            .shape(RoundedCornerShape(50), MaterialTheme.colors.secondary, 2.dp)
                                            .padding(horizontal = 8.dp)
                                    }(verticalAlignment = Alignment.CenterVertically),
                                    {
                                        IconButton(Assetor.Resize24) {
                                            presetSizesShow = !presetSizesShow
                                        }
                                    }
                                ) {

                                    Input(
                                        iconPreviewSize.toString(),
                                        modifier = Modifier.height(20.dp)
                                    ) {
                                        it.trim().ifEmpty { null }?.toIntOrNull()?.let { size ->
                                            iconPreviewSize = when {
                                                size > 4 -> if (size <= 240) size else 240
                                                else -> 4
                                            }
                                        }
                                    }

                                    DropdownMenu(presetSizesShow, {
                                        presetSizesShow = false
                                    }) {
                                        LazyColumn(modifier = Modifier.width(120.dp).height(120.dp).padding(2.dp)) {
                                            items(
                                                arrayOf(
                                                    4, 16, 20, 24, 28, 32, 48
                                                )
                                            ) { newSize ->
                                                Column(Modifier.wrapContentSize().padding(vertical = 2.dp)) {
                                                    DropdownMenuItem(
                                                        {
                                                            iconPreviewSize = newSize
                                                        },
                                                        sizeIn = MenuDefaults.SizeIn.copy(minHeight = 32.dp),
                                                        modifier = {
                                                            shape(RoundedCornerShape(50))
                                                        }
                                                    ) {
                                                        Text("$newSize")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                Spacer(Modifier.width(2.dp))

                                IconButton(FontAwesomeIcons.Solid.Plus, 24, contentDescription = "Increase icon Size") {
                                    val newSize = iconPreviewSize + 2
                                    iconPreviewSize = if (newSize > 240) 240 else newSize
                                }

                                Spacer(Modifier.width(2.dp))

                                IconButton(FontAwesomeIcons.Solid.Minus, 24, contentDescription = "Decrease Icon Size") {
                                    val newSize = iconPreviewSize - 2

                                    iconPreviewSize = if (newSize < 4) 4 else newSize

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
                                        IconPreviewLayout(icon, iconModifier, asIcon)
                                    } else {
                                        if (index == icons.size - 1) viewModel.updateIconsState()
                                        IconPreviewLayout(icon, iconModifier, asIcon)
                                    }

                                }

                            }

                        }

                        currentIcon.compose { icon ->
                            IconClassFileLayout(icon, iconContentWidth) {
                                currentIcon = null
                            }
                        }
                    }
                }
            }

        }
    }
}
