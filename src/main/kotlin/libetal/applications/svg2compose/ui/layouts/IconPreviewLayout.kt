package libetal.applications.svg2compose.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import libetal.applications.svg2compose.data.Icon
import libetal.applications.svg2compose.utils.annotated
import libetal.libraries.compose.layouts.DropdownMenuItem
import libetal.libraries.compose.layouts.SizeIn
import libetal.libraries.compose.ui.shape

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun IconPreviewLayout(icon: Icon, modifier: Modifier) {


    var dropDownState by remember { mutableStateOf(false) }

    Column(
        modifier.padding(2.dp).mouseClickable {
            when {
                buttons.isSecondaryPressed -> dropDownState = !dropDownState
            }
        },
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
                    Icon(icon.painter, "Description", modifier.fillMaxSize(0.5f))
                }
            }
        }

        DropdownMenu(
            dropDownState,
            modifier = Modifier.padding(2.dp),
            onDismissRequest = {
                dropDownState = false
            }) {

            val shape = RoundedCornerShape(50)

            val clipboardManager = LocalClipboardManager.current

            DropdownMenuItem(
                {
                    clipboardManager.setText(icon.composeClassFile.annotated)
                },
                modifier = {
                    shape(shape, MaterialTheme.colors.primaryVariant, 2.dp)
                },
                contentPadding = PaddingValues(horizontal = 8.dp),
                sizeIn = MenuDefaults.SizeIn.copy(minHeight = 32.dp)
            ) {
                Text("Copy Kt Class")
            }
        }
    }

}


