package libetal.applications.assetor.models

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import br.com.devsrsouza.svg2compose.Size
import br.com.devsrsouza.svg2compose.VectorType
import kotlinx.coroutines.*
import libetal.applications.assetor.convert
import libetal.applications.assetor.data.Icon
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.debug.debug
import libetal.kotlin.debug.info
import libetal.kotlin.debug.warn
import java.io.File
import kotlin.io.path.createTempDirectory
import kotlin.io.path.forEachDirectoryEntry

class IconsViewModel : ViewModel() {

    var job: Job? = null

    val path = mutableStateOf(System.getProperty("user.home") ?: "")

    var scrapingState = mutableStateOf(false)

    var regenerationJob: Job? = null

    val iconPackageNameState = mutableStateOf("libetal.applications.assetor")

    var iconPackageName
        get() = iconPackageNameState.value
        set(value) {
            iconPackageNameState.value = value

            regenerationJob?.cancel()

            regenerationJob = coroutineScope.launch(Dispatchers.IO) {
                iconsState.forEach { icon ->
                    icon.composeClassFile = icon.convert()
                }
            }

        }

    val iconReceiverNameState = mutableStateOf("Icons")

    /*Scraping state might affect this but not yet */
    var iconReceiverName
        get() = iconReceiverNameState.value
        set(value) {
            iconReceiverNameState.value = value

            regenerationJob?.cancel()

            regenerationJob = coroutineScope.launch(Dispatchers.IO) {
                iconsState.forEach { icon ->
                    icon.composeClassFile = icon.convert()
                }
            }

        }

    var scraping by scrapingState

    var currentPath: String = path.value

    val iconsState = mutableStateListOf<Icon>()

    val icons = mutableListOf<Icon>()

    override fun onCreate() {
        loadPath()
    }

    override fun onStart() {

    }

    override fun onResume() {

    }

    var onScrapingFinish: () -> Unit = {
        TAG info "Done scraping"
    }

    fun updateIconsState(chunkSize: Int = 10) {

        if (scraping) onScrapingFinish = {
            updateIconsState(chunkSize)
        } else {

            TAG info "Updating icons = ${icons.size} = Icons state= ${iconsState.size}"
            var i = iconsState.size
            val max = i + chunkSize

            while (i < max && i < icons.size) {
                iconsState.add(icons[i++])
            }

        }

    }

    private fun loadPath() {
        job = coroutineScope.launch(Dispatchers.IO) {
            val path = currentPath.trim().ifBlank { null }

            path?.let { activePath ->
                launch(Dispatchers.Main) {
                    scraping = true
                }
                File(activePath).getIcons()
                onScrapingFinish()
                launch(Dispatchers.Main) {
                    scraping = false
                }
            }
        }
    }

    suspend fun File.getIcons(): Unit = withContext(Dispatchers.IO) {
        val isActive = job?.isActive ?: return@withContext TAG info "Job is empty"

        if (isActive) {
            if (isDirectory) {

                if (path == "." || path == "..") return@withContext

                try {
                    toPath().forEachDirectoryEntry {
                        launch(Dispatchers.IO) {
                            it.toFile().getIcons()
                        }
                    }
                } catch (e: java.nio.file.AccessDeniedException) {
                    TAG debug "Access denied for $path"
                }

            } else {
                isSvg {

                    launch(Dispatchers.IO) {
                        val stream = try {
                            val stream = inputStream()
                            if (stream.readAllBytes().isEmpty()) {
                                TAG debug "File is empty $path"
                                null
                            } else inputStream()
                        } catch (e: java.nio.file.AccessDeniedException) {
                            TAG info "File access denied $path"
                            null
                        } catch(e: java.io.FileNotFoundException){
                            null
                        } ?: return@launch TAG debug "Stream for $path is null"

                        val painter = try {
                            loadSvgPainter(stream, Density(80f, 1f))
                        } catch (e: Exception) {
                            TAG debug "Failed to get painter"
                            null
                        } ?: return@launch TAG debug "Painter for $path is null"

                        val icon = Icon(path, painter, stream)

                        try {
                            icon.composeClassFile = icon.convert(size = Size(24))

                            icons.add(icon)

                            launch(Dispatchers.Main) {
                                if (iconsState.size < 10) {
                                    iconsState.add(icon)
                                }
                            }

                        } catch (e: Exception) {
                            warn(TAG, "Unsupported icon ${icon.path}", e)
                        }

                    }

                }

            }
        } else TAG debug "Job was canceled"


    }

    private inline fun File.isSvg(action: () -> Unit) {
        path.trim().ifBlank { null }?.split("/")?.let { sections ->
            if (sections.last().lowercase().split('.').last() == "svg") {
                action()
            }
        }
    }

    override fun onDestroy() {
        iconsState.clear()
        icons.clear()
        currentPath = path.value

    }

    val tempDir by lazy {
        createTempDirectory("ic_converter").toFile()
    }

    fun convert(
        currentIcon: Icon,
        receiverName: String = iconReceiverName,
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

    fun Icon.convert(receiverName: String = iconReceiverName, packageName: String = iconPackageName, size: Size? = null) =
        File(path).convert(VectorType.SVG, tempDir, receiverName, packageName, size)

    companion object {
        const val TAG = "IconsViewModel"
    }

}