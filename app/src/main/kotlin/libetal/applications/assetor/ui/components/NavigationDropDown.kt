package libetal.applications.assetor.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import libetal.applications.assetor.ui.AppNarrations
import libetal.applications.assetor.ui.icons.Assetor
import libetal.applications.assetor.ui.icons.Settings
import libetal.kotlin.compose.narrator.narrative
import libetal.libraries.compose.layouts.DropDownMenu
import libetal.libraries.compose.layouts.IconButton


val MutableState<Boolean>.toggle: () -> Unit
    get() = { value = !value }


fun <T> MutableState<Boolean>.toggle(then: (state: Boolean) -> T): () -> T = {
    value = !value
    then(value)
}

@Composable
fun NavigationDropDown(showSelectResource: MutableState<Boolean>) =
    DropDownMenu(showSelectResource.value, showSelectResource.toggle) {
        Column(modifier = Modifier.padding(4.dp)) {

            val browserNarrative = AppNarrations.FILE_EXPLORER.narrative
            val parser = AppNarrations.PARSER.narrative

            DropdownMenuItem(showSelectResource.toggle {
                browserNarrative.narrate()
            }) {
                Text("Explorer")
            }

            DropdownMenuItem(showSelectResource.toggle {
                parser.narrate()
            }) {
                Text("Parser")
            }

            DropdownMenuItem({
                TODO("Navigate To Free pik")
            }) {
                Text("FreePik")
            }

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                IconButton(Assetor.Settings, 32, "") {
                    TODO("Navigate to resources settings")
                }
            }

        }
    }
