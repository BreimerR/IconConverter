package libetal.applications.assetor.ui.layouts.parser

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import br.com.devsrsouza.svg2compose.Size
import br.com.devsrsouza.svg2compose.VectorType
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Copy
import libetal.applications.assetor.MainViewModel
import libetal.applications.assetor.convert
import libetal.applications.assetor.ui.icons.*
import libetal.applications.assetor.ui.layouts.SelectableText
import libetal.applications.assetor.utils.annotated
import libetal.kotlin.compose.narrator.NarrationScopeImpl
import libetal.kotlin.compose.narrator.createScopeCollector
import libetal.libraries.compose.layouts.Caption

import libetal.libraries.compose.layouts.DropDownMenu
import libetal.libraries.compose.layouts.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.libraries.compose.layouts.text.InputLayout
import libetal.libraries.compose.layouts.text.InputModifier
import libetal.libraries.compose.ui.shape


@Composable
fun ParseLayout(mainViewModel: MainViewModel) {

    val vScrollState = rememberScrollState()
    val hScrollState = rememberScrollState()
    var inputSvg by remember { mutableStateOf<String?>(null) }
    val allIconsState = remember { mutableStateOf("") }
    val allIcons by allIconsState

    var resourcePath by remember { mutableStateOf<String?>(null) }
    var accessorName by remember { mutableStateOf<String?>(null) }
    var iconName by remember { mutableStateOf<String?>(null) }
    var packageName by remember { mutableStateOf<String?>(null) }

    var svgString by remember {
        mutableStateOf(
            """|import ${packageName ?: "com.example"}.${accessorName ?: "Icons"}.${iconName ?: "Example"}
                |
                |object $accessorName
                |
                |val $accessorName.$allIcons = /*OTHER CODE GENERATED HERE*/
                |""".trimMargin()
        )
    }
    val svgDerivedState = derivedStateOf {
        """|${packageName?.let { "package $it" } ?: ""}
            |
            |$svgString
            |""".trimMargin()
    }

    val svgFileString by svgDerivedState


    val errors = remember { mutableStateListOf<String>() }


    val scope by createScopeCollector<NarrationScopeImpl<ParserTabs>>()

    BoxWithConstraints(Modifier.fillMaxSize()) {

        var vectorType by remember { mutableStateOf(VectorType.SVG) }
        var iconSize by remember { mutableStateOf<Int?>(null) }
        val navigationHeight = 128.dp
        val topHeight = maxHeight - navigationHeight

        Column(Modifier.fillMaxSize().padding(4.dp), verticalArrangement = Arrangement.SpaceBetween) {

            Row(
                Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val resourceInputShape = RoundedCornerShape(50)
                val resourceModifier =
                    Modifier.fillMaxWidth(.4f)
                        .height(48.dp).border(1.dp, Color.Black, resourceInputShape)
                        .shape(resourceInputShape, Color.Black.copy(0.1f))
                        .padding(end = 8.dp)

                val inputModifier =
                    Modifier.fillMaxWidth(.2f)
                        .applySharedHeaderInputStyle()
                        .padding(horizontal = 16.dp, vertical = 4.dp)

                var showSelectResource by remember { mutableStateOf(false) }

                InputLayout(resourceModifier) {
                    Row(
                        Modifier.fillMaxHeight().shape(RoundedCornerShape(topStart = 25f, bottomStart = 25f)).clickable {
                            showSelectResource = !showSelectResource
                        }.padding(start = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Resources")
                        Spacer(Modifier.width(4.dp))
                        Icon(Assetor.ArrowDown, "Show Resources")

                        DropDownMenu(showSelectResource, { showSelectResource = !showSelectResource }) {
                            Column(modifier = Modifier.padding(4.dp)) {

                                DropdownMenuItem({
                                    TODO("Navigate To Resources")
                                }) {
                                    Text("Remote")
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
                    }
                    Spacer(Modifier.width(4.dp))
                    BoxWithConstraints(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterStart) {
                        val inputWidth = maxWidth - 32.dp
                        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                            Input(
                                value = resourcePath ?: "",
                                placeHolder = "/home/brymher/Pictures/Icon.svg",
                                modifier = Modifier.width(inputWidth),
                                singleLine = true,
                                placeHolderModifier = Modifier.width(inputWidth)
                            ) { newResourcePath ->
                                resourcePath = newResourcePath
                            }
                            IconButton(Assetor.IcFolder, 32, "Open File Selection") {
                                TODO("Seelect A File")
                            }
                        }
                    }
                }

                Spacer(Modifier.width(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    InputLayout(
                        InputModifier(inputModifier)
                    ) { modifier ->
                        Input(iconName ?: "", "Example", modifier, true) { newIconName ->
                            iconName = newIconName
                        }
                    }
                    Spacer(Modifier.width(4.dp))
                    InputLayout(
                        InputModifier(inputModifier)
                    ) { modifier ->
                        Input(accessorName ?: "", "Assetor", modifier, true) { newIconName ->
                            accessorName = newIconName
                        }
                    }
                    IconButton(Assetor.ThemeMode, iconSize = 32) {
                        mainViewModel.isDarkMode = !mainViewModel.isDarkMode
                    }
                    val clipboardManager = LocalClipboardManager.current

                    IconButton(FontAwesomeIcons.Solid.Copy, 32, "Copy To Clipboard") {
                        clipboardManager.setText(svgFileString.annotated)
                    }
                    Spacer(Modifier.width(4.dp))
                    IconButton(Assetor.Settings, iconSize = 32) {
                        TODO("Navigate to Settings")
                    }
                }

            }

            Row(Modifier.fillMaxWidth().height(topHeight)) {

                Column(Modifier.fillMaxWidth(0.4f)) {
                    Row(
                        Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(Modifier.fillMaxWidth(.5f)) {
                            Caption("package name")
                            Spacer(Modifier.height(4.dp))
                            Input(packageName ?: "", "com.libetal.examples", singleLine = true) { newPackageName ->
                                packageName = newPackageName.trim().ifEmpty { null }
                            }
                        }

                        var dropDownState by remember { mutableStateOf(false) }

                        Row(
                            Modifier.wrapContentSize()
                                .shape(
                                    RoundedCornerShape(25)
                                )
                                .clickable {
                                    dropDownState = !dropDownState
                                }
                                .background(Color.Black.copy(.2f))
                                .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Text(vectorType.toString())

                            Icon(Assetor.ArrowDown, "Some Text")


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

                        Spacer(Modifier.width(4.dp))

                        Box(
                            Modifier.wrapContentHeight().fillMaxWidth(.7f)
                                .shape(
                                    RoundedCornerShape(25)
                                )
                                .clickable {
                                    dropDownState = !dropDownState
                                }
                                .background(Color.Black.copy(.2f))
                                .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
                        ) {
                            Text(".dp", modifier = Modifier.align(Alignment.CenterEnd))
                            Input(
                                iconSize?.toString() ?: "",
                                "24",
                                modifier = Modifier.fillMaxWidth(.6f).align(Alignment.CenterStart),
                                singleLine = true
                            ) { newIconSize ->
                                iconSize = newIconSize.toIntOrNull()
                            }
                        }
                    }
                    Column {
                        Text("SVG | XML")
                        Column(Modifier.fillMaxHeight(0.7f).background(Color.Black.copy(.2f)).padding(8.dp)) {
                            Input(
                                inputSvg ?: "",
                                placeHolder = "<svg><!--YOUR MARKUP CODE HERE--></svg>",
                                modifier = Modifier.fillMaxSize().background(Color.Transparent),
                                containerContentAlignment = Alignment.TopStart
                            ) {
                                inputSvg = it
                            }
                        }

                        val hScroll = rememberScrollState()
                        val vScroll = rememberScrollState()

                        AnimatedVisibility(errors.size > 0) {
                            Column(
                                Modifier.fillMaxWidth().padding(8.dp).horizontalScroll(hScroll).verticalScroll(vScroll)
                            ) {

                                Text("Errors")

                                Spacer(modifier = Modifier.height(4.dp))

                                for (error in errors) {
                                    SelectableText(
                                        error,
                                        style = MaterialTheme.typography.caption.copy(MaterialTheme.colors.onError),
                                        textModifier = Modifier.wrapContentSize()
                                    )
                                }

                            }

                        }


                    }
                }
                Column(
                    Modifier.fillMaxSize().padding(4.dp).verticalScroll(vScrollState),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SelectableText(svgFileString, textModifier = Modifier.wrapContentHeight().fillMaxWidth())
                }

            }


            Button({
                errors.clear()

                when {
                    inputSvg == null -> errors.add("Please Input SVG")
                    accessorName == null -> errors.add("Add Accessor Name")
                    iconName == null -> errors.add("Icon Name Not Set")

                    else -> convert(
                        vectorType,
                        inputSvg!!,
                        iconName!!,
                        accessorName!!,
                        "",
                        size = iconSize?.let { Size(it) }) {
                        errors.clear()
                        errors.add(it.stackTraceToString())
                    }.also { icon ->
                        svgString = icon
                    }
                }

            }) {
                Text("Generate")
            }

        }
    }


}

@Composable
fun Modifier.applySharedHeaderInputStyle() =
    height(48.dp).shape(RoundedCornerShape(50), Color.Black.copy(0.2f))
