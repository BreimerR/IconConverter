package libetal.applications.svg2compose.models;


import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.loadSvgPainter
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.*
import libetal.multiplatform.log.Log
import java.io.File
import java.io.InputStream
import java.nio.file.Files.newDirectoryStream

val LocalFilesProvider = compositionLocalOf {
    FilesProvider()
}

class FilesProvider {

    val files = mutableStateListOf<InputStream>()

    val rootPath = mutableStateOf(System.getProperty("user.home"))

    var currentPath: String? = null
        get() = if (field == null) {
            rootPath.value.also {
                field = it
            }
        } else field

    val icons = mutableStateListOf<Painter>()

    val store = mutableStateMapOf<String, File>()

    val job by lazy {
        SupervisorJob()
    }

    fun load() {
        CoroutineScope(job).launch(Dispatchers.IO) {
            currentPath?.let { parent ->
                getSVGs(parent) { svgFile ->
                    svgFile.inputStream().let { stream ->
                        val painter = try {
                            loadSvgPainter(stream, Density(24f, 1f))
                        } catch (e: Exception) {
                            null
                        }
                        Log.d("GetSVGs", "Found icon")
                        painter?.let { paint ->

                            if (svgFile.path !in store) {
                                store[svgFile.path] = svgFile
                            }
                        }

                    }

                }
            }
        }
    }

    fun cancel() {
        job.cancel()
    }

    private suspend fun getSVGs(rootPath: String, onSvg: suspend (File) -> Unit) = withContext(Dispatchers.IO) {
        traverseDir(File(rootPath)) {
            it.isSvg(onSvg)
        }
    }

    suspend fun File.isSvg(action: suspend File.() -> Unit) {
        if (path.split('.').lastOrNull() == "svg") withContext(Dispatchers.IO) {
            action(this@isSvg)
        }
    }

    suspend fun traverseDir(folder: File, onFile: suspend (File) -> Unit): Unit = withContext(Dispatchers.IO) {

        if (folder.isDirectory) {
            try {
                newDirectoryStream(folder.toPath())?.use { stream ->

                    stream.forEach { path ->
                        val file = path?.toFile()
                        when (file?.path) {
                            "." -> {}
                            ".." -> {}
                            null -> {}
                            else -> traverseDir(file, onFile)

                        }

                    }

                }
            } catch (e: java.nio.file.AccessDeniedException) {
                Log.d("File", "Access Denied")
            }
        } else onFile(folder)
    }


    class Policy : SnapshotMutationPolicy<FilesProvider> {
        override fun equivalent(a: FilesProvider, b: FilesProvider): Boolean = a.icons.size == b.icons.size
    }


}
