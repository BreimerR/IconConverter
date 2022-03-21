package libetal.applications.svg2compose.models

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import br.com.devsrsouza.svg2compose.Size
import compose.icons.fontawesomeicons.regular.*
import kotlinx.coroutines.*
import libetal.applications.svg2compose.convert
import libetal.applications.svg2compose.data.Icon
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.multiplatform.log.Log
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.io.path.forEachDirectoryEntry

class IconsViewModel : ViewModel() {

    private var job: Job? = null

    val path = mutableStateOf(System.getProperty("user.home") ?: "")

    var scrapingState = mutableStateOf(false)

    var scraping by scrapingState

    var currentPath: String = path.value

    val iconsState = mutableStateListOf<Icon>()

    val icons = mutableListOf<Icon>()

    override fun onStart() {
        super.onStart()
        Log.d("IconsViewModel", "Started...")
    }

    override fun onResume() {
        loadPath()
    }

    var onScrapingFinish: () -> Unit = {
        Log.d(TAG, "Done scraping")
    }

    fun updateIconsState(chunkSize: Int = 10) {

        if (scraping) onScrapingFinish = {
            updateIconsState(chunkSize)
        } else {

            Log.d(TAG, "Updating icons = ${icons.size} = Icons state= ${iconsState.size}")
            var i = iconsState.size
            val max = i + chunkSize

            while (i < max && i < icons.size) {
                iconsState.add(icons[i++])
            }
        }

    }

    fun loadPath() {
        job = coroutineScope.launch(Dispatchers.IO) {
            val path = currentPath.trim().ifBlank { null }

            path?.let { activePath ->
                scraping = true
                File(activePath).getIcons()
                onScrapingFinish()
                scraping = false
            }
        }
    }

    fun File.getIcons() {
        if (job != null) {
            if (job?.isActive == true) {
                if (isDirectory) {

                    if (path == "." || path == "..") return

                    currentPath = path

                    try {
                        toPath().forEachDirectoryEntry {
                            it.toFile().getIcons()
                        }
                    } catch (e: java.nio.file.AccessDeniedException) {
                        Log.d(TAG, "Access denied for $path")
                    }

                } else {
                    isSvg {

                        val stream = try {
                            val stream = inputStream()
                            if (stream.readAllBytes().isEmpty()) {
                                Log.d(TAG, "File is empty $path")
                                null
                            } else inputStream()
                        } catch (e: java.nio.file.AccessDeniedException) {
                            Log.d(TAG, "File access denied $path")
                            null
                        } ?: return Log.d(TAG, "Stream for $path is null")

                        val painter = try {
                            loadSvgPainter(stream, Density(80f, 1f))
                        } catch (e: Exception) {
                            Log.d(TAG, "Failed to get painter")
                            null
                        } ?: return Log.d(TAG, "Painter for $path is null")

                        val icon = Icon(path, painter, stream)

                        try {
                            icon.composeClassFile = icon.convert(size = Size(24))

                            icons.add(icon)

                            coroutineScope.launch(Dispatchers.Main) {
                                if (iconsState.size < 10) {
                                    iconsState.add(icon)
                                }
                            }

                        } catch (e: Exception) {
                            Log.w(TAG, "Unsupported icon ${icon.path}", e)
                        }


                    }

                }
            } else Log.d(TAG, "Job was canceled")
        } else Log.d(TAG, "Job is empty")

    }

    private inline fun File.isSvg(action: () -> Unit) {
        path.trim().ifBlank { null }?.split("/")?.let { sections ->
            if (sections.last().lowercase().split('.').last() == "svg") {
                action()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("IconsViewModel", "Decomposing Icons... $state")
        iconsState.clear()
        icons.clear()
        currentPath = path.value

    }

    val tempDir by lazy {
        createTempDirectory("ic_converter").toFile()
    }

    fun convert(
        currentIcon: Icon,
        receiverName: String = "Icon",
        packageName: String = "com.example",
        size: Size? = null,
        onComplete: (String) -> Unit
    ) {
        coroutineScope.launch {
            onComplete(
                currentIcon.convert(
                    receiverName,
                    packageName,
                    size
                )
            )
        }
    }

    fun Icon.convert(receiverName: String = "Icon", packageName: String = "com.example", size: Size? = null) =
        File(path).convert(tempDir, receiverName, packageName, size)

    companion object {
        const val TAG = "IconsViewModel"
    }

}