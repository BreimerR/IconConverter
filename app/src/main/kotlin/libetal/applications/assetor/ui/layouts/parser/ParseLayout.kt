package libetal.applications.assetor.ui.layouts.parser

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import br.com.devsrsouza.svg2compose.Size
import br.com.devsrsouza.svg2compose.VectorType
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Copy
import libetal.applications.assetor.*
import libetal.applications.assetor.ui.AppNarrations
import libetal.applications.assetor.ui.components.NavigationDropDown
import libetal.applications.assetor.ui.icons.*
import libetal.applications.assetor.ui.layouts.SelectableText
import libetal.applications.assetor.utils.annotated
import libetal.kotlin.compose.narrator.NarrationScopeImpl
import libetal.kotlin.compose.narrator.createScopeCollector
import libetal.kotlin.compose.narrator.narrative
import libetal.kotlin.io.File
import libetal.libraries.compose.layouts.Caption
import libetal.libraries.compose.layouts.DropDownMenu
import libetal.libraries.compose.layouts.IconButton
import libetal.libraries.compose.layouts.text.Input
import libetal.libraries.compose.layouts.text.InputLayout
import libetal.libraries.compose.layouts.text.InputModifier
import libetal.libraries.compose.ui.shape
import java.awt.FileDialog
import java.io.FilenameFilter


@Composable
fun ParseLayout(mainViewModel: MainViewModel) {

    val vScrollState = rememberScrollState()
    var inputSvg by remember { mutableStateOf<String?>(null) }
    var resourcePath by remember { mutableStateOf<String?>(null) }
    var accessorName by remember { mutableStateOf<String?>(null) }
    var iconName by remember { mutableStateOf<String?>(null) }
    var packageName by remember { mutableStateOf<String?>(null) }
    val safeIconName by derivedStateOf {
        iconName ?: "Icons"
    }
    val safeAccessorName by derivedStateOf {
        accessorName ?: "Example"
    }
    val safePackageName by derivedStateOf {
        packageName ?: "com.example"
    }
    val packageLine by derivedStateOf {
        packageName?.let { "package $packageName" } ?: ""
    }
    var svgString by remember {
        mutableStateOf(
            """|import $safePackageName.$safeAccessorName.$safeIconName
                |
                |object $safeAccessorName
                |
                |val $safeAccessorName.$safeIconName = /*OTHER CODE GENERATED HERE*/
                |""".trimMargin()
        )
    }
    val svgDerivedState = derivedStateOf {
        """|$packageLine
            |
            |$svgString
            |""".trimMargin()
    }
    val svgFileString by svgDerivedState
    val errors = remember { mutableStateListOf<String>() }


    val scope by createScopeCollector<NarrationScopeImpl<ParserTabs>>()

    BoxWithConstraints(Modifier.fillMaxSize().padding(4.dp)) {

        var vectorType by remember { mutableStateOf(VectorType.SVG) }
        var iconSize by remember { mutableStateOf<Int?>(null) }
        val navigationHeight = 64.dp
        val centerHeight = maxHeight - navigationHeight

        Row(
            Modifier.fillMaxWidth().height(64.dp).padding(horizontal = 8.dp).align(Alignment.TopCenter),
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

            val showSelectResource = remember { mutableStateOf(false) }

            InputLayout(resourceModifier) {
                Row(
                    Modifier.fillMaxHeight().shape(RoundedCornerShape(topStart = 25f, bottomStart = 25f)).clickable {
                        showSelectResource.value = !showSelectResource.value
                    }.padding(start = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Resources")
                    Spacer(Modifier.width(4.dp))
                    Icon(Assetor.ArrowDown, "Show Resources")

                    NavigationDropDown(showSelectResource)
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
                            val fileDialog = FileDialog(
                                ComposeWindow()
                            )

                            var path = "/"
                            fileDialog.filenameFilter = FilenameFilter { _, name ->
                                name.substringAfterLast(".") == "svg"
                            }
                            fileDialog.isMultipleMode = false
                            fileDialog.isVisible = true

                            when (val fileName = fileDialog.file) {
                                null -> {}
                                else -> {
                                    resourcePath = "${fileDialog.directory}$fileName"

                                    val file = File(resourcePath!!)
                                    iconName = fileName.substringBeforeLast(".").camelCase.titleCase
                                    inputSvg = file.readText()
                                }
                            }


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

        Row(Modifier.fillMaxWidth().height(centerHeight).align(Alignment.BottomEnd)) {
            Column(Modifier.fillMaxWidth(0.4f).fillMaxHeight()) {
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

            BoxWithConstraints(Modifier.fillMaxSize()) {
                val topHeight = maxHeight - 64.dp
                Column(
                    Modifier.fillMaxWidth().height(topHeight).padding(4.dp).verticalScroll(vScrollState)
                        .align(Alignment.TopStart),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SelectableText(svgFileString, textModifier = Modifier.wrapContentHeight().fillMaxWidth())
                }
                BoxWithConstraints(
                    Modifier.fillMaxWidth().height(52.dp).shape(RoundedCornerShape(50)).background(Color.Black.copy(0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp).align(Alignment.BottomStart)
                ) {
                    val footerIconSize = 48
                    var savePath by remember { mutableStateOf("") }
                    val centerWidth = maxWidth - (footerIconSize.dp * 2)

                    val dialogState = remember { mutableStateOf(false) }
                    IconButton(Assetor.IcFolder, footerIconSize, "", modifier = Modifier.align(Alignment.CenterStart)) {
                        dialogState.value = true
                    }

                    Dialog(visible = dialogState.value, onCloseRequest = { dialogState.value = false }) {

                    }

                    Input(
                        value = savePath,
                        placeHolder = "/home/brymher",
                        containerModifier = Modifier.width(centerWidth).align(Alignment.Center)
                    ) {
                        savePath = it
                    }
                    IconButton(Assetor.IcSave24, footerIconSize, "", modifier = Modifier.align(Alignment.CenterEnd)) {
                        try {
                            val file = File(savePath, "$iconName.kt")
                            if (file.exists()) file.delete()
                            file.createNewFile()

                            // Doing this on main thread isn't the best path to go with
                            file.writer().use {
                                it.write(svgFileString)
                            }
                        } catch (e: Exception) {
                            errors.add(e.localizedMessage)
                        }

                    }

                }
            }

        }

        Dialog({ errors.clear() }, title = "Check On Errors", visible = errors.size > 0) {

            val vScroll = rememberScrollState()

            Column(
                Modifier.fillMaxSize().background(MaterialTheme.colors.background).padding(8.dp).verticalScroll(vScroll)
            ) {

                for (error in errors) {
                    SelectableText(
                        error,
                        style = MaterialTheme.typography.caption.copy(MaterialTheme.colors.onError),
                        textModifier = Modifier.fillMaxWidth().wrapContentHeight()
                    )
                }

            }

        }
    }


}

@Composable
fun Modifier.applySharedHeaderInputStyle() =
    height(48.dp).shape(RoundedCornerShape(50), Color.Black.copy(0.2f))
