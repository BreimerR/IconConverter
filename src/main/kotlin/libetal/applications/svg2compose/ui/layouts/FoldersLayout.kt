package libetal.applications.svg2compose.ui.layouts

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import libetal.applications.svg2compose.models.LocalFilesProvider


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FoldersLayout() {

    val provider = LocalFilesProvider.current
    val rootPath = provider.rootPath
    val icons = provider.icons

    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colors.onBackground) {
        Column {
            TextField(rootPath.value, onValueChange = {
                rootPath.value = it
            })
            LazyVerticalGrid(GridCells.Adaptive(40.dp), state = rememberLazyListState()) {
                items(icons) { painter ->
                    Card(Modifier.padding(2.dp).fillMaxSize()) {
                        Column(
                            modifier = Modifier.wrapContentSize().clickable {

                            },
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(painter, "File name maybe", modifier = Modifier.size(32.dp))
                        }
                    }
                }
            }
        }
    }


    LaunchedEffect(provider) {
        provider.load()
    }

    DisposableEffect(provider) {
        onDispose {
            //  provider.cancel()
        }
    }

}