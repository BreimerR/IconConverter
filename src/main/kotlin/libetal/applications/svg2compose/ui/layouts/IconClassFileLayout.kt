package libetal.applications.svg2compose.ui.layouts

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Regular
import compose.icons.fontawesomeicons.regular.Copy
import compose.icons.fontawesomeicons.regular.TimesCircle
import libetal.applications.svg2compose.data.Icon
import libetal.applications.svg2compose.models.IconsViewModel
import libetal.applications.svg2compose.ui.AppNarrations
import libetal.kotlin.compose.narrator.lifecycle.lifeCycleViewModel
import libetal.libraries.compose.layouts.icons.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.libraries.compose.layouts.text.InputLayout
import libetal.libraries.compose.layouts.text.InputModifier

@Composable
fun IconClassFileLayout(
    icon: Icon,
    iconContentWidth: Dp,
    onCloseRequest: () -> Unit
) {

    val viewModel: IconsViewModel = lifeCycleViewModel(AppNarrations.BROWSER)

    val packageName by remember { viewModel.iconPackageNameState }
    val iconReceiverName by remember { viewModel.iconReceiverNameState }

    Column(Modifier.width(iconContentWidth).fillMaxHeight()) {

        val vScrollState = rememberScrollState()
        val hScrollState = rememberScrollState()
        val clipBoardManager = LocalClipboardManager.current

        Row(
            Modifier.height(38.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            IconButton(FontAwesomeIcons.Regular.TimesCircle, 32, "Icon", onClick = onCloseRequest)

            Text(icon.path.substringAfterLast("/"), modifier = Modifier.wrapContentHeight())

            IconButton(FontAwesomeIcons.Regular.Copy, 32, "Copy Icon") {
                clipBoardManager.setText(AnnotatedString(icon.composeClassFile))
            }

        }


        BoxWithConstraints(Modifier.fillMaxSize()) {
            val inputsContainerHeight = 56.dp

            val remHeight = maxHeight - inputsContainerHeight

            val maxWidth = maxWidth

            Column {
                Row(
                    Modifier.fillMaxWidth().height(inputsContainerHeight).padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {


                    InputLayout(
                        InputModifier.Default {
                            width(100.dp)
                                .wrapContentHeight()
                                .background(MaterialTheme.colors.primaryVariant)
                                .padding(4.dp)
                        }
                    ) {

                        Input(
                            iconReceiverName,
                            singleLine = true,
                            modifier = it,
                            placeHolder = "Receiver Class"
                        ) {
                            viewModel.iconReceiverName = it
                        }
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    InputLayout(
                        InputModifier.Default {
                            width(240.dp)
                                .background(MaterialTheme.colors.primaryVariant)
                        }
                    ) {
                        Input(packageName, singleLine = true) {
                            viewModel.iconPackageName = it
                        }
                    }


                }
                BoxWithConstraints(Modifier.width(maxWidth).height(remHeight)) {

                    Column(Modifier.fillMaxSize().verticalScroll(vScrollState).horizontalScroll(hScrollState)) {

                        SlectableText(icon.composeClassFile, modifier = Modifier.wrapContentSize())

                    }

                    VerticalScrollbar(ScrollbarAdapter(vScrollState), modifier = Modifier.align(Alignment.TopEnd))
                    HorizontalScrollbar(ScrollbarAdapter(hScrollState), modifier = Modifier.align(Alignment.BottomStart))
                }

            }
        }


    }
}
