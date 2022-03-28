package libetal.applications.svg2compose.ui.layouts.parser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Copy
import libetal.applications.svg2compose.convert
import libetal.applications.svg2compose.ui.layouts.SlectableText
import libetal.kotlin.compose.narrator.Narrate
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.Narrator
import libetal.kotlin.compose.narrator.narration


@Composable
fun ParseLayout() {
    Narrator<ParserTabs> {

        val iconNarration: Narration<ParserTabs> = ParserTabs.ICON.narration
        val iconsNarration: Narration<ParserTabs> = ParserTabs.ICONS.narration


        val vScrollState = rememberScrollState()
        val hScrollState = rememberScrollState()
        var inputSvg by remember { mutableStateOf<String?>(null) }
        val allIconsState = remember { mutableStateOf("") }
        val allIcons by allIconsState

        var iconName by remember { mutableStateOf("Example") }
        var accessorName by remember { mutableStateOf("Icons") }
        var packageName by remember { mutableStateOf("com.example") }

        val svgState = remember {
            mutableStateOf(
                """|$packageName
                   |
                   |import $packageName.$accessorName.$iconName
                   |
                   |object $accessorName
                   |
                   |val $accessorName.$allIcons = /*OTHER CODE GENERATED HERE*/ 
                   |
                """.trimMargin()
            )
        }

        val svg by svgState

        val errors = remember { mutableStateListOf<String>() }

        BoxWithConstraints(Modifier.fillMaxSize().defaultMinSize(800.dp)) {

            val navigationHeight = 60.dp
            val topHeight = maxHeight - navigationHeight

            Column(Modifier.fillMaxSize().padding(4.dp), verticalArrangement = Arrangement.SpaceBetween) {

                Row(Modifier.fillMaxWidth().height(topHeight)) {

                    Column(Modifier.fillMaxWidth(0.4f)) {
                        Column {
                            Row {

                                TextField(packageName, label = { Text("Package Name") }, onValueChange = {
                                    packageName = it
                                })


                            }
                            Row {
                                TextField(iconName, label = { Text("IconName") }, onValueChange = {
                                    iconName = it
                                })
                                TextField(accessorName, label = { Text("Accessor") }, onValueChange = {
                                    accessorName = it
                                })

                            }
                        }
                        Column {
                            TextField(
                                inputSvg ?: "",
                                label = {
                                    Text("SVG | XML")
                                },
                                onValueChange = {
                                    inputSvg = it
                                },
                                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.7f),
                                placeholder = {
                                    Text("<svg><!--YOUR MARKUP CODE HERE-></svg>")
                                }
                            )

                            val hScroll = rememberScrollState()
                            val vScroll = rememberScrollState()

                            AnimatedVisibility(errors.size > 0) {
                                Column(
                                    Modifier.fillMaxWidth().padding(8.dp).horizontalScroll(hScroll).verticalScroll(vScroll)
                                ) {

                                    Text("Errors")

                                    Spacer(modifier = Modifier.height(4.dp))

                                    for (error in errors) {
                                        SlectableText(
                                            error,
                                            style = MaterialTheme.typography.caption.copy(MaterialTheme.colors.onError),
                                            modifier = Modifier.wrapContentSize()
                                        )
                                    }

                                }

                            }


                        }
                    }
                    Column(Modifier.fillMaxWidth()) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row {
                                Button({
                                    iconNarration?.begin()
                                }) {
                                    Text("$iconName.kt")
                                }
                                Button({
                                    iconsNarration?.begin()
                                }) {
                                    Text("$accessorName.kt")
                                }

                            }
                            Row {
                                IconButton({}) {
                                    Icon(FontAwesomeIcons.Solid.Copy, "Copy to ClipBoard", modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Column(
                            Modifier.fillMaxWidth().padding(4.dp).horizontalScroll(hScrollState).verticalScroll(vScrollState)
                        ) {


                            Narrate<ParserTabs>({ true }) {

                                ParserTabs.ICON {
                                    SlectableText(svg, modifier = Modifier.wrapContentSize())
                                }

                                ParserTabs.ICONS {
                                    SlectableText(allIcons, modifier = Modifier.wrapContentSize())
                                }

                            }

                        }

                    }

                }

                ParserLayoutBottomNav(navigationHeight) {

                    if (inputSvg != null) {
                        val icon = convert(inputSvg!!, iconName, accessorName, packageName) {
                            errors.clear()
                            errors.add(it.stackTraceToString())
                        }

                        svgState.value = icon

                        // allIconsState.value = icons
                    } else {
                        errors.clear()
                        errors.add("Please input svg")
                    }

                }

            }

        }

    }

}


