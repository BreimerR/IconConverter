package libetal.applications.assetor.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.mouseClickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import libetal.applications.assetor.data.Icon
import libetal.applications.assetor.utils.annotated
import libetal.libraries.compose.layouts.DropdownMenuItem
import libetal.libraries.compose.layouts.SizeIn
import libetal.libraries.compose.ui.shape

@Composable
@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
fun IconPreviewLayout(icon: Icon, modifier: Modifier, asIcon: Boolean = true) {


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
                    if (asIcon) {
                        Icon(icon.painter, "Description", modifier.fillMaxSize(0.5f))
                    } else Image(icon.painter, "Description", modifier.fillMaxSize(0.5f))

                }
            }
        }

        var dropDownFocus by remember { mutableStateOf(false) }
        val focusRequester = FocusRequester()

        DropdownMenu(
            dropDownState,
            modifier = Modifier.padding(2.dp).focusRequester(focusRequester).focusable(true).onFocusChanged {
                dropDownFocus = !it.isFocused
            },
            onDismissRequest = {
                dropDownState = false
            }
        ) {


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

            SideEffect {
                if (dropDownFocus) {
                    focusRequester.requestFocus()
                }
            }
        }
    }

}


