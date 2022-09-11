package libetal.applications.assetor.ui.layouts.parser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import br.com.devsrsouza.svg2compose.VectorType
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Copy
import libetal.applications.assetor.convert
import libetal.applications.assetor.ui.layouts.SlectableText
import libetal.applications.assetor.utils.annotated
import libetal.kotlin.compose.narrator.Narration
import libetal.kotlin.compose.narrator.NarrationScopeImpl
import libetal.kotlin.compose.narrator.collectedScope
import libetal.kotlin.compose.narrator.createScopeCollector

import libetal.libraries.compose.layouts.DropDownMenu


@Composable
fun ParseLayout() {

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


    var svgString by svgState

    val svg by svgState

    val errors = remember { mutableStateListOf<String>() }


    val scope by createScopeCollector<NarrationScopeImpl<ParserTabs>>()

    BoxWithConstraints(Modifier.fillMaxSize().defaultMinSize(800.dp)) {

        var vectorType by remember { mutableStateOf(VectorType.SVG) }
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


                            Column {

                                var dropDownState by remember { mutableStateOf(false) }

                                Text(vectorType.toString(), modifier = Modifier.clickable {
                                    dropDownState = !dropDownState
                                })

                                DropDownMenu(dropDownState, { dropDownState = !dropDownState }) {
                                    Column(modifier = Modifier.padding(4.dp)) {

                                        DropdownMenuItem({
                                            vectorType = VectorType.SVG
                                        }) {
                                            Text(VectorType.SVG.toString())
                                        }

                                        DropdownMenuItem({
                                            vectorType = VectorType.DRAWABLE
                                        }) {
                                            Text(VectorType.DRAWABLE.toString())
                                        }

                                    }
                                }
                            }


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
                                with(scope) {
                                    ParserTabs.ICON.narrate()
                                }

                            }) {
                                Text("$iconName.kt")
                            }
                            Button({
                                with(scope) {
                                    ParserTabs.ICONS.narrate()
                                }
                            }) {
                                Text("$accessorName.kt")
                            }

                        }
                        Row {
                            val clipboardManager = LocalClipboardManager.current

                            IconButton({
                                clipboardManager.setText(svgState.value.annotated)
                            }) {
                                Icon(FontAwesomeIcons.Solid.Copy, "Copy to ClipBoard", modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    Column(
                        Modifier.fillMaxWidth().padding(4.dp).horizontalScroll(hScrollState).verticalScroll(vScrollState)
                    ) {

                        Narration<ParserTabs> {

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

                    convert(vectorType, inputSvg!!, iconName, accessorName, packageName) {
                        errors.clear()
                        errors.add(it.stackTraceToString())
                    }.also { icon ->
                        svgString = icon
                    }

                } else {
                    errors.clear()
                    errors.add("Please input svg")
                }

            }

        }

    }

}




